package fr.app.ui.view.table;

import fr.app.domain.FileNode;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TreeTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

public class ColoredProgressBarTreeTableCell extends TreeTableCell<FileNode, Double> {

    private final ProgressBar progressBar = new ProgressBar();
    private final Label percentLabel = new Label();
    private final HBox container = new HBox(8, progressBar, percentLabel);

    public ColoredProgressBarTreeTableCell() {
        progressBar.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(progressBar, Priority.ALWAYS);
        percentLabel.getStyleClass().add("percent-label");
        percentLabel.setMinWidth(48);
        percentLabel.setAlignment(Pos.CENTER_RIGHT);
        container.setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(Double value, boolean empty) {
        super.updateItem(value, empty);

        if (empty || value == null) {
            setGraphic(null);
            return;
        }

        double progress = Math.max(0, Math.min(1, value));
        progressBar.setProgress(progress);
        percentLabel.setText(String.format("%.1f%%", progress * 100));

        FileNodeTreeTableViewComponent tableView = (FileNodeTreeTableViewComponent) getTreeTableView();
        Color color = tableView.resolveColor(tableView.getTreeItem(getIndex()));
        progressBar.setStyle("-fx-accent: " + toWeb(color) + ";");

        setGraphic(container);
    }

    private String toWeb(Color color) {
        return String.format("#%02X%02X%02X",
                (int) Math.round(color.getRed() * 255),
                (int) Math.round(color.getGreen() * 255),
                (int) Math.round(color.getBlue() * 255));
    }
}
