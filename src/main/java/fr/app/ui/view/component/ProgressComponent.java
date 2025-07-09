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
    public final Label filesScannedLabel = new Label("Files scanned: ");
    public final Label filesScannedCountLabel = new Label("0");
    public final Label durationLabel = new Label("Duration: ");
    public final Label durationValueLabel = new Label("0s");

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

        HBox filesScannedContainer = new HBox(filesScannedLabel, filesScannedCountLabel);
        HBox durationContainer = new HBox(durationLabel, durationValueLabel);

        getChildren().addAll(pathField, progressBarContainer, filesScannedContainer, durationContainer);
    }

    public void setPath(String path) {
        pathField.setText(path);
    }

    public void setProgress(double progress) {
        progressBar.setProgress(progress);
    }
}
