package fr.app.ui.view.component;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class MainContainerComponent extends VBox {
    public final TextField filterField = new TextField();
    public final ToggleButton foldersToggle = new ToggleButton("Folders");
    public final ToggleButton fileTypesToggle = new ToggleButton("File types");
    public final ToggleButton inspectorToggle = new ToggleButton("Details");
    public final FileNodeTreeTableViewComponent treeTableViewComponent = new FileNodeTreeTableViewComponent();
    public final InspectorPanelComponent inspectorPanel = new InspectorPanelComponent();
    public final Label itemCountLabel = new Label("0 of 0 items");
    public final Label sortLabel = new Label("Sorted by size ↓");

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

        inspectorToggle.setSelected(true);
        inspectorToggle.getStyleClass().add("inspector-toggle-button");

        HBox topBar = new HBox(10, filterField, viewToggleGroup, inspectorToggle);

        itemCountLabel.getStyleClass().add("status-label");
        sortLabel.getStyleClass().add("status-label");
        Region statusSpacer = new Region();
        HBox.setHgrow(statusSpacer, Priority.ALWAYS);
        HBox statusBar = new HBox(itemCountLabel, statusSpacer, sortLabel);
        statusBar.getStyleClass().add("status-bar");

        inspectorPanel.setVisible(false);
        inspectorPanel.setManaged(false);
        HBox.setHgrow(treeTableViewComponent, Priority.ALWAYS);
        HBox contentRow = new HBox(treeTableViewComponent, inspectorPanel);
        VBox.setVgrow(contentRow, Priority.ALWAYS);

        getChildren().addAll(topBar, contentRow, statusBar);
    }
}
