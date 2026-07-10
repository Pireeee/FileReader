package fr.app.ui.view.sidebar;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class PathBarComponent extends HBox {
    private final Label pathLabel = new Label();
    private final Label sizeLabel = new Label();

    public PathBarComponent() {
        getStyleClass().add("path-bar-component");
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(8);

        pathLabel.getStyleClass().add("path-label");
        pathLabel.setMinWidth(0);
        pathLabel.setMaxWidth(Double.MAX_VALUE);
        pathLabel.setTextOverrun(OverrunStyle.LEADING_ELLIPSIS);
        HBox.setHgrow(pathLabel, Priority.ALWAYS);

        sizeLabel.getStyleClass().add("path-size-label");

        getChildren().addAll(pathLabel, sizeLabel);
    }

    public void setPath(String path) {
        pathLabel.setText(path);
    }

    public void setSize(String sizeText) {
        sizeLabel.setText(sizeText);
    }
}