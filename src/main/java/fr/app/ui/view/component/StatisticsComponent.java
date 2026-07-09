package fr.app.ui.view.component;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class StatisticsComponent extends VBox {

    public final Label pathLabel = new Label();

    private final Label foldersValue = new Label("0");
    private final Label filesValue = new Label("0");
    private final Label speedValue = new Label("0 files/s");
    private final Label readValue = new Label("0 B/s");

    public StatisticsComponent() {
        setSpacing(6);
        getStyleClass().add("statistics-component");
        pathLabel.getStyleClass().add("path-label");
        pathLabel.setMinWidth(0);
        pathLabel.setMaxWidth(Double.MAX_VALUE);
        pathLabel.setTextOverrun(OverrunStyle.LEADING_ELLIPSIS);

        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(50);

        GridPane grid = new GridPane();
        grid.getStyleClass().add("stats-grid");
        grid.setHgap(12);
        grid.setVgap(8);
        grid.getColumnConstraints().addAll(column, column);
        grid.add(statTile("Folders", foldersValue), 0, 0);
        grid.add(statTile("Files", filesValue), 1, 0);
        grid.add(statTile("Speed", speedValue), 0, 1);
        grid.add(statTile("Read", readValue), 1, 1);

        getChildren().addAll(pathLabel, grid);
    }

    private VBox statTile(String label, Label valueLabel) {
        Label titleLabel = new Label(label);
        titleLabel.getStyleClass().add("stat-tile-label");
        valueLabel.getStyleClass().add("stat-tile-value");
        return new VBox(2, titleLabel, valueLabel);
    }

    public void setPath(String path) {
        Platform.runLater(() -> pathLabel.setText(path));
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
}
