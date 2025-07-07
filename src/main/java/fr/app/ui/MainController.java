package fr.app.ui;


import fr.app.application.DiskScannerService;
import fr.app.domain.FileNode;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.nio.file.Paths;

public class MainController {

    private final DiskScannerService scannerService;
    private final TreeView<String> treeView;

    public MainController(TreeView<String> treeView, DiskScannerService scannerService) {
        this.treeView = treeView;
        this.scannerService = scannerService;
    }

    public void startScan() {
        String rootPath = "C:/Users/pierr/OneDrive/Images"; // à rendre dynamique plus tard

        scannerService.scan(Paths.get(rootPath), result -> {
            Platform.runLater(() -> treeView.setRoot(convert(result)));
        });
    }

    private TreeItem<String> convert(FileNode node) {
        TreeItem<String> item = new TreeItem<>(node.getName() + " (" + node.getSize() + " bytes)");
        for (FileNode child : node.getChildren()) {
            item.getChildren().add(convert(child));
        }
        return item;
    }
}
