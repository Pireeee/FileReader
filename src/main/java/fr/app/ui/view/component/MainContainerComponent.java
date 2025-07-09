package fr.app.ui.view.component;

import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MainContainerComponent extends VBox {
    public final ProgressComponent progressComponent = new ProgressComponent();
    public final TreemapComponent treemapComponent = new TreemapComponent(800, 600);

    public MainContainerComponent() {
        setSpacing(10);
        getChildren().addAll(progressComponent, treemapComponent);
        VBox.setVgrow(treemapComponent, Priority.ALWAYS);
    }
}
