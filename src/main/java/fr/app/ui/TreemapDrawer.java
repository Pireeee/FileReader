package fr.app.ui;

import fr.app.domain.FileNode;
import fr.app.utils.Logger;
import fr.app.utils.SizeFormatter;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class TreemapDrawer {

    public void drawTreemap(Pane treemapPane, FileNode node) {
        Logger.info("Drawing treemap for node: " + node.getName() + " with size: " + node.getSize());
        Platform.runLater(() -> {
            treemapPane.getChildren().clear();
            drawTreemapRecursive(treemapPane, node, 0, 0, treemapPane.getWidth(), treemapPane.getHeight(), true);
            Logger.info("Treemap drawing completed for node: " + node.getName());
        });
    }

    private void drawTreemapRecursive(Pane treemapPane, FileNode node, double x, double y, double width, double height, boolean horizontal) {
        if (node.getChildren().isEmpty()) {
            drawLeafNode(treemapPane, node, x, y, width, height);
        } else {
            drawParentNode(treemapPane, node, x, y, width, height, horizontal);
        }
    }

    private void drawLeafNode(Pane treemapPane, FileNode node, double x, double y, double width, double height) {
        Rectangle rect = createRectangle(x, y, width, height);
        Text text = createText(x, y, node.getName(), SizeFormatter.format(node.getSize()));
        treemapPane.getChildren().addAll(rect, text);
    }

    private void drawParentNode(Pane treemapPane, FileNode node, double x, double y, double width, double height, boolean horizontal) {
        long totalSize = node.getSize();
        double offset = 0;

        for (FileNode child : node.getChildren()) {
            double childDimension = calculateChildDimensions(child.getSize(), totalSize, horizontal ? width : height);

            if (horizontal) {
                drawTreemapRecursive(treemapPane, child, x + offset, y, childDimension, height, !horizontal);
                offset += childDimension;
            } else {
                drawTreemapRecursive(treemapPane, child, x, y + offset, width, childDimension, !horizontal);
                offset += childDimension;
            }
        }
    }

    private Rectangle createRectangle(double x, double y, double width, double height) {
        Rectangle rect = new Rectangle(x, y, width, height);
        rect.setFill(Color.color(Math.random(), Math.random(), Math.random(), 0.5));
        rect.setStroke(Color.BLACK);
        return rect;
    }

    private Text createText(double x, double y, String name, String fileSize) {
        return new Text(x + 5, y + 15, name + " (" + fileSize + ")") {{
            setFill(Color.BLACK);
            setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        }};
    }

    private double calculateChildDimensions(long childSize, long totalSize, double dimension) {
        double ratio = (double) childSize / totalSize;
        double normalizedRatio = Math.max(0.1, Math.min(ratio, 0.5));
        return dimension * normalizedRatio;
    }

}