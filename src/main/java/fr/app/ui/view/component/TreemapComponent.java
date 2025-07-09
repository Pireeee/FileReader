package fr.app.ui.view.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TreemapComponent extends VBox {
    private final Canvas treemapCanvas;

    public TreemapComponent(int width, int height) {
        treemapCanvas = new Canvas(width, height);
        VBox.setVgrow(treemapCanvas, Priority.ALWAYS);
        getChildren().add(treemapCanvas);
    }

    public Canvas getCanvas() {
        return treemapCanvas;
    }
}
