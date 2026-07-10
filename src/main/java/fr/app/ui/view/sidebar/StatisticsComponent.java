package fr.app.ui.view.sidebar;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class StatisticsComponent extends VBox {

    private final Label foldersValue = new Label("0");
    private final Label filesValue = new Label("0");
    private final Label speedValue = new Label("0 files/s");
    private final Label readValue = new Label("0 B/s");
    private final Label scanTimeValue = new Label("0:00.000");

    public StatisticsComponent() {
        setSpacing(8);
        getStyleClass().add("statistics-component");

        ColumnConstraints twoColumn = new ColumnConstraints();
        twoColumn.setPercentWidth(50);
        GridPane topRow = new GridPane();
        topRow.setHgap(12);
        topRow.getColumnConstraints().addAll(twoColumn, twoColumn);
        topRow.add(statTile("Folders", foldersValue), 0, 0);
        topRow.add(statTile("Files", filesValue), 1, 0);

        ColumnConstraints threeColumn = new ColumnConstraints();
        threeColumn.setPercentWidth(100.0 / 3);
        GridPane bottomRow = new GridPane();
        bottomRow.setHgap(12);
        bottomRow.getColumnConstraints().addAll(threeColumn, threeColumn, threeColumn);
        bottomRow.add(statTile("Speed", speedValue), 0, 0);
        bottomRow.add(statTile("Read", readValue), 1, 0);
        bottomRow.add(statTile("Scan time", scanTimeValue), 2, 0);

        getChildren().addAll(topRow, bottomRow);
    }

    private VBox statTile(String label, Label valueLabel) {
        Label titleLabel = new Label(label);
        titleLabel.getStyleClass().add("stat-tile-label");
        valueLabel.getStyleClass().add("stat-tile-value");
        return new VBox(2, titleLabel, valueLabel);
    }

    public void updateSpeed(String filesSpeedText, String bytesSpeedText) {
        Platform.runLater(() -> {
            speedValue.setText(filesSpeedText);
            readValue.setText(bytesSpeedText);
        });
    }

    public void updateCounts(long folders, long files) {
        Platform.runLater(() -> {
            foldersValue.setText(String.format("%,d", folders));
            filesValue.setText(String.format("%,d", files));
        });
    }

    public void updateScanTime(String durationText) {
        Platform.runLater(() -> scanTimeValue.setText(durationText));
    }
}