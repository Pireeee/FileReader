package fr.app.ui.view.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TreemapComponent extends VBox {
    private final Canvas treemapCanvas;

    public TreemapComponent() {
        getStyleClass().add("treemap-component");

        treemapCanvas = new Canvas();
        treemapCanvas.getStyleClass().add("treemap-canvas");
        widthProperty().addListener((obs, oldW, newW) -> treemapCanvas.setWidth(newW.doubleValue()-10));
        heightProperty().addListener((obs, oldH, newH) -> treemapCanvas.setHeight(newH.doubleValue()-10));
        //getChildren().add(treemapCanvas);
        setVgrow(treemapCanvas, Priority.ALWAYS);
    }

    public Canvas getCanvas() {
        return treemapCanvas;
    }
}
