package fr.app.ui.view.component;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class StatisticsComponent extends VBox {

    public final Label pathLabel = new Label();
    public final Label scanSummaryLabel = new Label("Scan: 0:00.000   0 files/s   0 B/s");

    public StatisticsComponent() {
        this.setSpacing(6);
        getStyleClass().add("statistics-component");
        scanSummaryLabel.getStyleClass().add("scan-summary-label");
        this.getChildren().addAll(pathLabel, scanSummaryLabel);
    }

    public void setPath(String path) {
        Platform.runLater(() -> pathLabel.setText("Chemin : " + path));
    }

    public void updateStats(String durationText, String filesSpeedText, String bytesSpeedText) {
        Platform.runLater(() ->
                scanSummaryLabel.setText(String.format("Scan: %s   %s   %s", durationText, filesSpeedText, bytesSpeedText))
        );
    }
}
