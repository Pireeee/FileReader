package fr.app.ui.controller;

import fr.app.application.DiskScannerService;
import fr.app.application.ScanHandle;
import fr.app.domain.FileNode;
import fr.app.domain.ScanResult;
import fr.app.ui.view.CategorySlice;
import fr.app.ui.view.DonutChartDrawer;
import fr.app.ui.view.MainView;
import fr.app.ui.view.component.CategoryLegendComponent;
import fr.app.ui.view.component.DonutChartComponent;
import fr.app.ui.view.component.StatisticsComponent;
import fr.app.ui.view.component.SidebarComponent;
import fr.app.utils.Logger;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainController {

    private final DiskScannerService diskScannerService;
    private final MainView view;
    private final Stage stage;
    private final StatisticsComponent statisticsComponent;
    private final SidebarComponent sidebarComponent;
    private final DonutChartComponent donutChartComponent;
    private final CategoryLegendComponent categoryLegendComponent;
    private final DonutChartDrawer donutChartDrawer = new DonutChartDrawer();

    private enum ViewMode { FOLDERS, FILE_TYPES }

    private String selectedPath = "C:/Users/";
    private FileNode currentRoot;
    private FileNode extensionRoot;
    private Map<String, Color> categoryColors = Map.of();
    private Map<String, Color> fileTypeColors = Map.of();
    private ViewMode viewMode = ViewMode.FOLDERS;
    private ScanHandle currentScan;
    private FileNode selectedNode;

    public MainController(DiskScannerService diskScannerService, MainView view, Stage stage) {
        this.diskScannerService = diskScannerService;
        this.view = view;
        this.sidebarComponent = view.getSidebar();
        this.statisticsComponent = sidebarComponent.statisticsComponent;
        this.donutChartComponent = sidebarComponent.donutChartComponent;
        this.categoryLegendComponent = sidebarComponent.categoryLegendComponent;
        this.stage = stage;
    }

    public void init() {
        statisticsComponent.setPath(selectedPath);
        sidebarComponent.chooseButton.setOnAction(e -> openDirectoryChooser());
        setScanning(false);
        view.getMainContainer().filterField.textProperty()
                .addListener((obs, oldValue, newValue) -> refreshTreeView());
        view.getMainContainer().foldersToggle.setOnAction(e -> switchViewMode(ViewMode.FOLDERS));
        view.getMainContainer().fileTypesToggle.setOnAction(e -> switchViewMode(ViewMode.FILE_TYPES));
        view.getMainContainer().treeTableViewComponent.getSortOrder()
                .addListener((ListChangeListener<TreeTableColumn<FileNode, ?>>) change -> updateSortLabel());
        view.getMainContainer().treeTableViewComponent.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldItem, newItem) -> onSelectionChanged(newItem));
        view.getMainContainer().inspectorToggle.setOnAction(e -> updateInspectorVisibility());
        view.getMainContainer().inspectorPanel.openInExplorerButton.setOnAction(e -> openInExplorer());
        view.getMainContainer().inspectorPanel.copyPathButton.setOnAction(e -> copyPath());
    }

    private void onSelectionChanged(TreeItem<FileNode> item) {
        selectedNode = item != null ? item.getValue() : null;
        if (selectedNode != null) {
            boolean isSyntheticGroup = viewMode == ViewMode.FILE_TYPES
                    && item.getParent() == view.getMainContainer().treeTableViewComponent.getRoot();
            Color color = view.getMainContainer().treeTableViewComponent.resolveColor(item);
            view.getMainContainer().inspectorPanel.show(selectedNode, color, !isSyntheticGroup);
        }
        updateInspectorVisibility();
    }

    private void updateInspectorVisibility() {
        boolean visible = selectedNode != null && view.getMainContainer().inspectorToggle.isSelected();
        view.getMainContainer().inspectorPanel.setVisible(visible);
        view.getMainContainer().inspectorPanel.setManaged(visible);
    }

    private void openInExplorer() {
        if (selectedNode == null) {
            return;
        }
        try {
            Desktop.getDesktop().open(selectedNode.getPath().toFile());
        } catch (IOException e) {
            Logger.warn("Could not open path: " + selectedNode.getPath());
        }
    }

    private void copyPath() {
        if (selectedNode == null) {
            return;
        }
        ClipboardContent content = new ClipboardContent();
        content.putString(selectedNode.getPath().toString());
        Clipboard.getSystemClipboard().setContent(content);
    }

    private void updateSortLabel() {
        List<TreeTableColumn<FileNode, ?>> sortOrder = view.getMainContainer().treeTableViewComponent.getSortOrder();
        String text;
        if (sortOrder.isEmpty()) {
            text = "Sorted by size ↓";
        } else {
            TreeTableColumn<FileNode, ?> column = sortOrder.get(0);
            String arrow = column.getSortType() == TreeTableColumn.SortType.ASCENDING ? "↑" : "↓";
            text = "Sorted by " + column.getText() + " " + arrow;
        }
        view.getMainContainer().sortLabel.setText(text);
    }

    private void switchViewMode(ViewMode mode) {
        viewMode = mode;
        view.getMainContainer().treeTableViewComponent.setCategoryColors(
                mode == ViewMode.FOLDERS ? categoryColors : fileTypeColors);
        refreshTreeView();
    }

    private void openDirectoryChooser() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choisir un dossier à scanner");
        File selectedDirectory = chooser.showDialog(stage);
        if (selectedDirectory != null) {
            selectedPath = selectedDirectory.getAbsolutePath();
            statisticsComponent.setPath(selectedPath);
            startScan();
        }
    }

    private void startScan() {
        Path rootPath = Path.of(selectedPath);
        setScanning(true);

        currentScan = diskScannerService.scan(
                rootPath,
                progressInfo -> Platform.runLater(() -> {
                    statisticsComponent.updateSpeed(progressInfo.getScanSpeed(), progressInfo.getBytesSpeed());
                    statisticsComponent.updateCounts(progressInfo.getFoldersScanned(), progressInfo.getFilesScanned());
                }),
                scanResult -> Platform.runLater(() -> onScanComplete(scanResult)),
                error -> Platform.runLater(() -> onScanError(error)),
                () -> Platform.runLater(this::onScanCancelled)
        );
    }

    private void stopScan() {
        if (currentScan != null) {
            currentScan.cancel();
        }
    }

    private void onScanCancelled() {
        Logger.info("Scan annulé par l'utilisateur");
        setScanning(false);
    }

    private void setScanning(boolean scanning) {
        sidebarComponent.chooseButton.setDisable(scanning);
        sidebarComponent.scanButton.setText(scanning ? "Stop Scan" : "Scan Folder");
        sidebarComponent.scanButton.setOnAction(e -> {
            if (scanning) {
                stopScan();
            } else {
                startScan();
            }
        });
        sidebarComponent.scanButton.getStyleClass().removeAll("scan-folder", "stop-scan");
        sidebarComponent.scanButton.getStyleClass().add(scanning ? "stop-scan" : "scan-folder");
    }

    private void onScanComplete(ScanResult result) {
        setScanning(false);
        currentRoot = result.getRootNode();
        extensionRoot = buildExtensionRoot(currentRoot);

        List<CategorySlice> slices = donutChartDrawer.buildSlices(currentRoot);
        categoryColors = toColorMap(slices);
        fileTypeColors = toColorMap(donutChartDrawer.buildSlices(extensionRoot));

        view.getMainContainer().treeTableViewComponent.setCategoryColors(
                viewMode == ViewMode.FOLDERS ? categoryColors : fileTypeColors);
        refreshTreeView();
        updateDonutChart(currentRoot, slices);
    }

    private Map<String, Color> toColorMap(List<CategorySlice> slices) {
        return slices.stream()
                .collect(Collectors.toMap(CategorySlice::name, CategorySlice::color, (a, b) -> a));
    }

    // Flattens files into per-extension groups without touching the shared FileNode tree.
    private FileNode buildExtensionRoot(FileNode root) {
        Map<String, List<FileNode>> byExtension = new HashMap<>();
        collectFilesByExtension(root, byExtension);

        long totalSize = byExtension.values().stream()
                .flatMap(List::stream)
                .mapToLong(FileNode::getSize)
                .sum();

        List<FileNode> groups = new ArrayList<>();
        for (Map.Entry<String, List<FileNode>> entry : byExtension.entrySet()) {
            List<FileNode> files = entry.getValue();
            long groupSize = files.stream().mapToLong(FileNode::getSize).sum();
            Instant latest = files.stream()
                    .map(FileNode::getLastModified)
                    .max(Instant::compareTo)
                    .orElse(Instant.EPOCH);
            double groupPercent = totalSize == 0 ? 0 : (groupSize * 100.0) / totalSize;

            List<FileNode> filesWithGroupPercent = files.stream()
                    .map(file -> file.withUpdatedPercentOfParent(
                            groupSize == 0 ? 0 : (file.getSize() * 100.0) / groupSize))
                    .toList();

            String label = entry.getKey().isEmpty() ? "(no extension)" : "." + entry.getKey();
            groups.add(new FileNode(label, root.getPath(), groupSize, groupPercent, true, latest,
                    "", filesWithGroupPercent));
        }

        return new FileNode(root.getName(), root.getPath(), totalSize, 100, true,
                root.getLastModified(), "", groups);
    }

    private void collectFilesByExtension(FileNode node, Map<String, List<FileNode>> byExtension) {
        if (node.getChildren() == null) {
            return;
        }
        for (FileNode child : node.getChildren()) {
            if (child.isDirectory()) {
                collectFilesByExtension(child, byExtension);
            } else {
                String extension = child.getExtension() != null ? child.getExtension() : "";
                byExtension.computeIfAbsent(extension, key -> new ArrayList<>()).add(child);
            }
        }
    }

    private void updateDonutChart(FileNode root, List<CategorySlice> slices) {
        NodeCounts counts = countNodes(root);
        long totalBytes = root.getSize();
        donutChartComponent.update(slices, totalBytes, counts.files(), counts.folders());
        categoryLegendComponent.update(slices, totalBytes);
        statisticsComponent.updateCounts(counts.folders(), counts.files());
    }

    private record NodeCounts(long files, long folders) {}

    private NodeCounts countNodes(FileNode node) {
        long files = 0;
        long folders = 0;
        if (node.getChildren() != null) {
            for (FileNode child : node.getChildren()) {
                if (child.isDirectory()) {
                    folders++;
                } else {
                    files++;
                }
                NodeCounts childCounts = countNodes(child);
                files += childCounts.files();
                folders += childCounts.folders();
            }
        }
        return new NodeCounts(files, folders);
    }

    private void onScanError(Throwable error) {
        setScanning(false);
        Logger.error("Erreur pendant le scan", error);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur lors du scan");
        alert.setHeaderText("Une erreur est survenue pendant le scan");
        alert.setContentText(error.getMessage());
        alert.showAndWait();
    }

    private void refreshTreeView() {
        if (currentRoot == null) {
            return;
        }
        FileNode base = viewMode == ViewMode.FOLDERS ? currentRoot : extensionRoot;
        String query = view.getMainContainer().filterField.getText().trim().toLowerCase();
        TreeItem<FileNode> rootItem = buildFilteredTree(base, query);
        if (rootItem == null) {
            rootItem = new TreeItem<>(base);
        }
        view.getMainContainer().treeTableViewComponent.setRoot(rootItem);
        view.getMainContainer().treeTableViewComponent.setShowRoot(false);

        int totalTopLevel = base.getChildren() != null ? base.getChildren().size() : 0;
        int visibleTopLevel = rootItem.getChildren().size();
        view.getMainContainer().itemCountLabel.setText(visibleTopLevel + " of " + totalTopLevel + " items");
    }

    // Returns null when neither this node nor any descendant matches the query.
    private TreeItem<FileNode> buildFilteredTree(FileNode node, String query) {
        List<TreeItem<FileNode>> matchingChildren = new ArrayList<>();
        if (node.getChildren() != null) {
            List<FileNode> sortedChildren = node.getChildren().stream()
                    .sorted((a, b) -> Long.compare(b.getSize(), a.getSize()))
                    .toList();

            for (FileNode child : sortedChildren) {
                TreeItem<FileNode> childItem = buildFilteredTree(child, query);
                if (childItem != null) {
                    matchingChildren.add(childItem);
                }
            }
        }

        boolean selfMatches = query.isEmpty() || node.getName().toLowerCase().contains(query);
        if (!selfMatches && matchingChildren.isEmpty()) {
            return null;
        }

        TreeItem<FileNode> item = new TreeItem<>(node);
        item.getChildren().addAll(matchingChildren);
        if (!query.isEmpty() && !matchingChildren.isEmpty()) {
            item.setExpanded(true);
        }
        return item;
    }

    public void shutdown() {
        diskScannerService.shutdown();
    }
}
