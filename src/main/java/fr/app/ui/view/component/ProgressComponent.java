package fr.app.ui.view.component;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ProgressComponent extends VBox {
    public final TextField pathField = new TextField();
    public final ProgressBar progressBar = new ProgressBar(0);
    public final Label filesScannedLabel = new Label("Files : ");
    public final Label filesScannedCountLabel = new Label("0");
    public final Label durationLabel = new Label("Duration: ");
    public final Label durationValueLabel = new Label("0s");

    private Timeline timer;
    private long startTimeMillis = 0;

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

    public void startTimer() {
        stopTimer();
        startTimeMillis = System.currentTimeMillis();

        timer = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            long elapsedMillis = System.currentTimeMillis() - startTimeMillis;
            durationValueLabel.setText(formatDurationMillis(elapsedMillis));
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    public void stopTimer() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    private String formatDurationMillis(long millis) {
        long totalSeconds = millis / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        long ms = millis % 1000;

        return String.format("%dmin %02ds %03dms", minutes, seconds, ms);
    }

    public void setPath(String path) {
        pathField.setText(path);
    }

    public void setProgress(double progress) {
        progressBar.setProgress(progress);
    }
}
