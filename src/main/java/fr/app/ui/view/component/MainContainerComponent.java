package fr.app.ui.view.component;

import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MainContainerComponent extends VBox {
    public final TextField filterField = new TextField();
    public final ToggleButton foldersToggle = new ToggleButton("Folders");
    public final ToggleButton fileTypesToggle = new ToggleButton("File types");
    public final FileNodeTreeTableViewComponent treeTableViewComponent = new FileNodeTreeTableViewComponent();

    private final ToggleGroup viewModeGroup = new ToggleGroup();

    public MainContainerComponent() {
        setSpacing(10);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        filterField.setPromptText("Filter files and folders...");
        filterField.getStyleClass().add("filter-field");
        HBox.setHgrow(filterField, Priority.ALWAYS);

        foldersToggle.setToggleGroup(viewModeGroup);
        fileTypesToggle.setToggleGroup(viewModeGroup);
        foldersToggle.setSelected(true);
        foldersToggle.getStyleClass().add("view-toggle-button");
        fileTypesToggle.getStyleClass().add("view-toggle-button");
        // A ToggleGroup allows deselecting the active toggle by re-clicking it;
        // keep exactly one of the two view modes selected at all times.
        viewModeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null && oldToggle != null) {
                oldToggle.setSelected(true);
            }
        });

        HBox viewToggleGroup = new HBox(foldersToggle, fileTypesToggle);
        viewToggleGroup.getStyleClass().add("view-toggle-group");

        HBox topBar = new HBox(10, filterField, viewToggleGroup);

        VBox.setVgrow(treeTableViewComponent, Priority.ALWAYS);
        getChildren().addAll(topBar, treeTableViewComponent);
    }
}
