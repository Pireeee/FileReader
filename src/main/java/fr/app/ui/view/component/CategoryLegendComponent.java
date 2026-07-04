package fr.app.ui.view.component;

import fr.app.ui.view.CategorySlice;
import fr.app.utils.SizeFormatter;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;

public class CategoryLegendComponent extends VBox {

    public CategoryLegendComponent() {
        getStyleClass().add("category-legend-component");
        setSpacing(8);
    }

    public void update(List<CategorySlice> slices, long totalBytes) {
        getChildren().clear();
        if (slices == null || totalBytes <= 0) {
            return;
        }
        for (CategorySlice slice : slices) {
            getChildren().add(buildRow(slice, totalBytes));
        }
    }

    private HBox buildRow(CategorySlice slice, long totalBytes) {
        Circle dot = new Circle(5, slice.color());

        Label nameLabel = new Label(slice.name());
        nameLabel.getStyleClass().add("legend-name");
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        nameLabel.setMaxWidth(Double.MAX_VALUE);

        Label sizeLabel = new Label(SizeFormatter.format(slice.size()));
        sizeLabel.getStyleClass().add("legend-size");

        double fraction = (double) slice.size() / totalBytes;
        ProgressBar bar = new ProgressBar(fraction);
        bar.getStyleClass().add("legend-bar");
        bar.setPrefWidth(100);
        bar.setStyle("-fx-accent: " + toWeb(slice.color()) + ";");

        Label percentLabel = new Label(String.format("%.1f%%", fraction * 100));
        percentLabel.getStyleClass().add("legend-percent");
        percentLabel.setMinWidth(48);
        percentLabel.setAlignment(Pos.CENTER_RIGHT);

        HBox row = new HBox(10, dot, nameLabel, sizeLabel, bar, percentLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("legend-row");
        return row;
    }

    private String toWeb(Color color) {
        return String.format("#%02X%02X%02X",
                (int) Math.round(color.getRed() * 255),
                (int) Math.round(color.getGreen() * 255),
                (int) Math.round(color.getBlue() * 255));
    }
}
