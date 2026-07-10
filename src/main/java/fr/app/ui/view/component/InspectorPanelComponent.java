package fr.app.ui.view.component;

import fr.app.domain.FileNode;
import fr.app.utils.SizeFormatter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class InspectorPanelComponent extends VBox {

    public final Button openInExplorerButton = new Button("Open in Explorer");
    public final Button scanFolderButton = new Button("Scan This Folder");
    public final Button copyPathButton = new Button("Copy Path");

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Circle categoryDot = new Circle(5);
    private final Label nameLabel = new Label();
    private final Label pathLabel = new Label();
    private final Label sizeValue = new Label();
    private final Label typeValue = new Label();
    private final Label itemsValue = new Label();
    private final Label modifiedValue = new Label();

    public InspectorPanelComponent() {
        setSpacing(10);
        setPadding(new Insets(15));
        setPrefWidth(220);
        getStyleClass().add("inspector-panel");

        nameLabel.getStyleClass().add("inspector-name");
        nameLabel.setWrapText(true);
        HBox nameRow = new HBox(6, categoryDot, nameLabel);
        nameRow.setAlignment(Pos.CENTER_LEFT);

        pathLabel.getStyleClass().add("inspector-path");
        pathLabel.setWrapText(true);

        VBox details = new VBox(6,
                detailRow("Size", sizeValue),
                detailRow("Type", typeValue),
                detailRow("Items", itemsValue),
                detailRow("Modified", modifiedValue)
        );

        openInExplorerButton.setMaxWidth(Double.MAX_VALUE);
        scanFolderButton.setMaxWidth(Double.MAX_VALUE);
        copyPathButton.setMaxWidth(Double.MAX_VALUE);
        openInExplorerButton.getStyleClass().addAll("inspector-action", "inspector-action-primary");
        scanFolderButton.getStyleClass().add("inspector-action");
        copyPathButton.getStyleClass().add("inspector-action");

        getChildren().addAll(nameRow, pathLabel, details, openInExplorerButton, scanFolderButton, copyPathButton);
    }

    private HBox detailRow(String label, Label valueLabel) {
        Label titleLabel = new Label(label);
        titleLabel.getStyleClass().add("inspector-detail-label");
        valueLabel.getStyleClass().add("inspector-detail-value");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return new HBox(titleLabel, spacer, valueLabel);
    }

    public void show(FileNode node, Color color, boolean actionsEnabled) {
        categoryDot.setFill(color);
        nameLabel.setText(node.getName());
        pathLabel.setText(node.getPath().toString());
        sizeValue.setText(SizeFormatter.format(node.getSize()));
        typeValue.setText(fileType(node));
        itemsValue.setText(String.valueOf(node.getChildrenCount()));
        modifiedValue.setText(DATE_FORMATTER.withZone(ZoneId.systemDefault()).format(node.getLastModified()));

        openInExplorerButton.setDisable(!actionsEnabled);
        copyPathButton.setDisable(!actionsEnabled);
        scanFolderButton.setDisable(!actionsEnabled || !node.isDirectory());
    }

    private String fileType(FileNode node) {
        if (node.isDirectory()) {
            return "Folder";
        }
        String extension = node.getExtension();
        return extension == null || extension.isEmpty() ? "File" : extension.toUpperCase() + " File";
    }
}
