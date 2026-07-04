package fr.app.ui.controller;

import fr.app.application.DiskScannerService;
import fr.app.domain.FileNode;
import fr.app.domain.ProgressInfo;
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
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class MainController {

    private final DiskScannerService diskScannerService;
    private final MainView view;
    private final Stage stage;
    private final StatisticsComponent statisticsComponent;
    private final SidebarComponent sidebarComponent;
    private final DonutChartComponent donutChartComponent;
    private final CategoryLegendComponent categoryLegendComponent;
    private final DonutChartDrawer donutChartDrawer = new DonutChartDrawer();

    private String selectedPath = "C:/Users/";

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
        sidebarComponent.scanButton.setOnAction(e -> startScan());
    }

    private void openDirectoryChooser() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choisir un dossier à scanner");
        File selectedDirectory = chooser.showDialog(stage);
        if (selectedDirectory != null) {
            selectedPath = selectedDirectory.getAbsolutePath();
            statisticsComponent.setPath(selectedPath);
        }
    }

    private void startScan() {
        Path rootPath = Path.of(selectedPath);

        diskScannerService.scan(
                rootPath,
                progressInfo -> Platform.runLater(() -> statisticsComponent.updateStats(
                        progressInfo.getDurationFormatted(),
                        progressInfo.getScanSpeed(),
                        progressInfo.getBytesSpeed()
                )),
                scanResult -> Platform.runLater(() -> onScanComplete(scanResult)),
                error -> Platform.runLater(() -> onScanError(error))
        );
    }

    private void onScanComplete(ScanResult result) {
        updateTreeView(result.getRootNode());
        updateDonutChart(result.getRootNode());
    }

    private void updateDonutChart(FileNode root) {
        NodeCounts counts = countNodes(root);
        List<CategorySlice> slices = donutChartDrawer.buildSlices(root);
        long totalBytes = root.getSize();
        donutChartComponent.update(slices, totalBytes, counts.files(), counts.folders());
        categoryLegendComponent.update(slices, totalBytes);
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
        Logger.error("Erreur pendant le scan", error);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur lors du scan");
        alert.setHeaderText("Une erreur est survenue pendant le scan");
        alert.setContentText(error.getMessage());
        alert.showAndWait();
    }

    private void updateTreeView(FileNode rootNode) {
        TreeItem<FileNode> rootItem = new TreeItem<>(rootNode);
        buildTree(rootNode, rootItem);
        view.getMainContainer().treeTableViewComponent.setRoot(rootItem);
        view.getMainContainer().treeTableViewComponent.setShowRoot(true);
    }

    private void buildTree(FileNode node, TreeItem<FileNode> parentItem) {
        if (node.getChildren() != null) {
            List<FileNode> sortedChildren = node.getChildren().stream()
                    .sorted((a, b) -> Long.compare(b.getSize(), a.getSize()))
                    .toList();

            for (FileNode child : sortedChildren) {
                TreeItem<FileNode> childItem = new TreeItem<>(child);
                parentItem.getChildren().add(childItem);
                buildTree(child, childItem);
            }
        }
    }

    public void shutdown() {
        diskScannerService.shutdown();
    }
}
