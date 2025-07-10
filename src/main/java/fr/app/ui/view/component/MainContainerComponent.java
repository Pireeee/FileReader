package fr.app.ui.view.component;

import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MainContainerComponent extends VBox {
    public final ProgressComponent progressComponent = new ProgressComponent();
    public final TreemapComponent treemapComponent = new TreemapComponent();

    public MainContainerComponent() {
        setSpacing(10);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);;
        VBox.setVgrow(treemapComponent, Priority.ALWAYS);
        getChildren().addAll(progressComponent, treemapComponent);
    }
}
