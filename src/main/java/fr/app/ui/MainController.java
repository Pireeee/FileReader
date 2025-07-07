package fr.app.ui;


import fr.app.application.DiskScannerService;
import fr.app.domain.FileNode;
import fr.app.infrastructure.FileSystemScanner;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MainController {

    private final DiskScannerService scannerService;
    private final TreeView<String> treeView;

    public MainController(TreeView<String> treeView) {
        this.treeView = treeView;
        this.scannerService = new DiskScannerService(new FileSystemScanner());
    }

    public void startScan(Path rootPath) {
        scannerService.scanAsync(rootPath).thenAccept(fileNode -> {
            TreeItem<String> rootItem = createTreeItem(fileNode);
            Platform.runLater(() -> treeView.setRoot(rootItem));
        });
    }

    private TreeItem<String> createTreeItem(FileNode node) {
        String label = node.getName() + " (" + node.getSize() + " bytes)";
        TreeItem<String> item = new TreeItem<>(label);
        for (FileNode child : node.getChildren()) {
            item.getChildren().add(createTreeItem(child));
        }
        return item;
    }

    public void shutdown() {
        scannerService.shutdown();
    }
}
