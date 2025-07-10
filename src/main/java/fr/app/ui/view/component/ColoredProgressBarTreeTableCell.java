package fr.app.ui.view.component;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TreeTableCell;
import javafx.scene.layout.StackPane;

public class ColoredProgressBarTreeTableCell<S> extends TreeTableCell<S, Double> {

    private final ProgressBar progressBar = new ProgressBar();
    private final Label percentLabel = new Label();
    private final StackPane stackPane = new StackPane(progressBar, percentLabel);

    public ColoredProgressBarTreeTableCell() {
        progressBar.setMaxWidth(Double.MAX_VALUE);
        percentLabel.setStyle("-fx-font-weight: bold;");
        stackPane.setPrefHeight(20);
    }

    @Override
    protected void updateItem(Double value, boolean empty) {
        super.updateItem(value, empty);

        if (empty || value == null) {
            setGraphic(null);
        } else {
            double progress = Math.max(0, Math.min(1, value));
            progressBar.setProgress(progress);

            percentLabel.setText(String.format("%.1f%%", progress * 100));

            percentLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");

            String color;
            if (progress < 0.33) {
                color = "#4CAF50"; // vert
            } else if (progress < 0.66) {
                color = "#FFC107"; // jaune
            } else {
                color = "#F44336"; // rouge
            }
            progressBar.setStyle("-fx-accent: " + color + ";");

            setGraphic(stackPane);
        }
    }

    public static <S> TreeTableCell<S, Double> forTreeTableColumn() {
        return new ColoredProgressBarTreeTableCell<>();
    }
}
