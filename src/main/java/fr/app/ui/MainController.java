package fr.app.ui;


import fr.app.application.DiskScannerService;
import fr.app.domain.FileNode;
import fr.app.infrastructure.FileSystemScanner;
import fr.app.utils.SizeFormatter;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MainController {

    private final DiskScannerService scannerService;
    private final TreeView<String> treeView;
    private final Pane treemapPane;
    private final TreemapDrawer treemapDrawer;


    public MainController(TreeView<String> treeView, Pane treemapPane) {
        this.treeView = treeView;
        this.scannerService = new DiskScannerService(new FileSystemScanner());
        this.treemapPane = treemapPane;
        this.treemapDrawer = new TreemapDrawer();
    }

    public void startScan(Path rootPath) {
        scannerService.scan(rootPath, result -> {
            Platform.runLater(() -> {
                treeView.setRoot(convert(result));
                treemapDrawer.drawTreemap(treemapPane, result);
            });
        });
    }

    private void drawTreemap(FileNode node) {
        treemapPane.getChildren().clear();
        drawTreemapRecursive(node, 0, 0, treemapPane.getWidth(), treemapPane.getHeight(), true);
    }

    private void drawTreemapRecursive(FileNode node, double x, double y, double width, double height, boolean horizontal) {
        if (node.getChildren().isEmpty()) {
            // Cas terminal : un fichier ou dossier vide → dessine bloc
            Rectangle rect = new Rectangle(x, y, width, height);
            rect.setFill(Color.color(Math.random(), Math.random(), Math.random(), 0.5));
            rect.setStroke(Color.BLACK);

            Text text = new Text(x + 5, y + 15, node.getName());
            treemapPane.getChildren().addAll(rect, text);
        } else {
            // Découper espace pour enfants
            long totalSize = node.getSize();
            double offset = 0;

            for (FileNode child : node.getChildren()) {
                double ratio = (double) child.getSize() / totalSize;

                if (horizontal) {
                    double childWidth = width * ratio;
                    drawTreemapRecursive(child, x + offset, y, childWidth, height, !horizontal);
                    offset += childWidth;
                } else {
                    double childHeight = height * ratio;
                    drawTreemapRecursive(child, x, y + offset, width, childHeight, !horizontal);
                    offset += childHeight;
                }
            }
        }

    }

    private TreeItem<String> convert(FileNode node) {
        String sizeString = SizeFormatter.format(node.getSize());
        String label = node.getName() + " (" + sizeString + ")";
        TreeItem<String> item = new TreeItem<>(label);
        for (FileNode child : node.getChildren()) {
            item.getChildren().add(convert(child));
        }
        return item;
    }

    public void shutdown() {
        scannerService.shutdown();
    }
}
