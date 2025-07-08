package fr.app.ui.controller;

import fr.app.application.DiskScannerService;
import fr.app.domain.FileNode;
import fr.app.ui.view.MainView;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;

public class MainController {

    private final DiskScannerService diskScannerService;
    private final MainView view;
    private final Stage stage;

    private String selectedPath = "C:/"; // chemin par défaut

    public MainController(DiskScannerService diskScannerService, MainView view, Stage stage) {
        this.diskScannerService = diskScannerService;
        this.view = view;
        this.stage = stage;
    }

    public void init() {
        view.pathField.setText(selectedPath);

        view.chooseFolderButton.setOnAction(e -> openDirectoryChooser());
        view.scanButton.setOnAction(e -> startScan());
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
        diskScannerService.scan(rootPath, fileNode -> Platform.runLater(() -> updateTreeView(fileNode)));
    }

    private void updateTreeView(FileNode rootNode) {
        TreeItem<String> rootItem = new TreeItem<>(rootNode.getName() + " (" + rootNode.getSize() + ")");
        buildTree(rootNode, rootItem);
        view.treeView.setRoot(rootItem);
    }

    private void buildTree(FileNode node, TreeItem<String> parentItem) {
        if (node.getChildren() != null) {
            for (FileNode child : node.getChildren()) {
                TreeItem<String> childItem = new TreeItem<>(child.getName() + " (" + child.getSize() + ")");
                parentItem.getChildren().add(childItem);
                buildTree(child, childItem);
            }
        }
    }

    public void shutdown() {
        diskScannerService.shutdown();
    }
}
