package fr.app.ui.controller;

import fr.app.application.DiskScannerService;
import fr.app.domain.FileNode;
import fr.app.ui.model.FileNodeTreeCell;
import fr.app.ui.view.MainView;
import fr.app.ui.view.TreemapDrawer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
    private final TreemapDrawer treemapDrawer = new TreemapDrawer();

    private String selectedPath = "C:/";

    public MainController(DiskScannerService diskScannerService, MainView view, Stage stage) {
        this.diskScannerService = diskScannerService;
        this.view = view;
        this.stage = stage;
    }

    public void init() {
        view.pathField.setText(selectedPath);
        view.chooseButton.setOnAction(e -> openDirectoryChooser());
        view.scanButton.setOnAction(e -> startScan());

        // ✅ Applique la TreeCell custom une seule fois !
        view.treeView.setCellFactory(tv -> new FileNodeTreeCell());
    }

    private void openDirectoryChooser() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choisir un dossier à scanner");
        File selectedDirectory = chooser.showDialog(stage);
        if (selectedDirectory != null) {
            selectedPath = selectedDirectory.getAbsolutePath();
            view.pathField.setText(selectedPath);
        }
    }

    private void startScan() {
        Path rootPath = Path.of(selectedPath);
        diskScannerService.scan(rootPath, fileNode -> Platform.runLater(() -> onScanComplete(fileNode)));
    }

    private void onScanComplete(FileNode rootNode) {
        Platform.runLater(() -> {
            drawTreemap(rootNode);
            updateTreeView(rootNode);
        });
    }

    private void updateTreeView(FileNode rootNode) {
        TreeItem<FileNode> rootItem = new TreeItem<>(rootNode);
        buildTree(rootNode, rootItem);
        view.treeView.setRoot(rootItem);
    }

    private void buildTree(FileNode node, TreeItem<FileNode> parentItem) {
        if (node.getChildren() != null) {
            // Tri par taille décroissante
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
        Canvas canvas = view.getTreemapCanvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        treemapDrawer.drawTreemap(gc, treemapDrawer.buildTreemap(root, 0, 0, (float) canvas.getWidth(), (float) canvas.getHeight()));
    }

    public void shutdown() {
        diskScannerService.shutdown();
    }
}
