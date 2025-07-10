package fr.app.ui.controller;

import fr.app.application.DiskScannerService;
import fr.app.domain.FileNode;
import fr.app.domain.ProgressInfo;
import fr.app.domain.ScanResult;
import fr.app.ui.view.MainView;
import fr.app.ui.view.TreemapDrawer;
import fr.app.ui.view.component.StatisticsComponent;
import fr.app.ui.view.component.SidebarComponent;
import fr.app.ui.view.component.TreemapComponent;
import fr.app.utils.Logger;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.List;

public class MainController {

    private final DiskScannerService diskScannerService;
    private final MainView view;
    private final Stage stage;
    private final TreemapDrawer treemapDrawer = new TreemapDrawer();
    private final StatisticsComponent statisticsComponent;
    private final SidebarComponent sidebarComponent;
    private final TreemapComponent treemapComponent;

    private String selectedPath = "C:/Users/";

    public MainController(DiskScannerService diskScannerService, MainView view, Stage stage) {
        this.diskScannerService = diskScannerService;
        this.view = view;
        this.sidebarComponent = view.getSidebar();
        this.statisticsComponent = sidebarComponent.statisticsComponent;
        this.treemapComponent = sidebarComponent.treemapComponent;
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
                progressInfo -> Platform.runLater(() -> {
                    String foldersText = "Dossiers analysés : " + progressInfo.getFoldersScanned();
                    String filesText = "Fichiers analysés : " + progressInfo.getFilesScanned();
                    String sizeText = "Taille cumulée : " + progressInfo.getTotalSize();
                    String filesSpeedText = "Vitesse fichiers : " + progressInfo.getScanSpeed();
                    String bytesSpeedText = "Vitesse lecture : " + progressInfo.getBytesSpeed();
                    String durationText = "Durée : " + progressInfo.getDurationFormatted();

                    statisticsComponent.updateStats(
                            foldersText,
                            filesText,
                            sizeText,
                            filesSpeedText,
                            bytesSpeedText,
                            durationText
                    );
                }),
                scanResult -> Platform.runLater(() -> onScanComplete(scanResult)),
                error -> Platform.runLater(() -> onScanError(error))
        );
    }

    private void onScanComplete(ScanResult result) {
        drawTreemap(result.getRootNode());
        updateTreeView(result.getRootNode());
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

    private void drawTreemap(FileNode root) {
        Canvas canvas = treemapComponent.getCanvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        treemapDrawer.drawTreemap(gc, treemapDrawer.buildTreemap(root, 5, 5, (float) canvas.getWidth(), (float) canvas.getHeight()));
    }

    public void shutdown() {
        diskScannerService.shutdown();
    }
}
