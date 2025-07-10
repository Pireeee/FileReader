package fr.app.ui.view.component;

import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MainContainerComponent extends VBox {
    public final FileNodeTreeTableViewComponent treeTableViewComponent = new FileNodeTreeTableViewComponent();

    public MainContainerComponent() {
        setSpacing(10);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);;
        VBox.setVgrow(treeTableViewComponent, Priority.ALWAYS);
        getChildren().addAll(treeTableViewComponent);
    }
}
