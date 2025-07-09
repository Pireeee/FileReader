package fr.app.ui.view.component;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ProgressComponent extends VBox {
    public final TextField pathField = new TextField();
    public final ProgressBar progressBar = new ProgressBar(0);

    public ProgressComponent() {
        setSpacing(10);
        getStyleClass().add("progress-component");

        pathField.setEditable(false);
        pathField.getStyleClass().add("path-field");

        progressBar.getStyleClass().add("progress-bar");
        progressBar.setMaxWidth(Double.MAX_VALUE);

        Label progressLabel = new Label("Progress :");
        HBox progressBarContainer = new HBox(progressLabel, progressBar);
        HBox.setHgrow(progressBar, Priority.ALWAYS);

        getChildren().addAll(pathField, progressBarContainer);
    }

    public void setPath(String path) {
        pathField.setText(path);
    }

    public void setProgress(double progress) {
        progressBar.setProgress(progress);
    }
}
