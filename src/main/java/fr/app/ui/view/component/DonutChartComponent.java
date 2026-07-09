package fr.app.ui.view.component;

import fr.app.ui.view.CategorySlice;
import fr.app.ui.view.DonutChartDrawer;
import fr.app.utils.SizeFormatter;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class DonutChartComponent extends StackPane {

    private final Canvas canvas = new ResizableCanvas();
    private final Label totalLabel = new Label();
    private final Label subLabel = new Label();
    private final DonutChartDrawer drawer = new DonutChartDrawer();
    private List<CategorySlice> currentSlices = List.of();

    public DonutChartComponent() {
        getStyleClass().add("donut-chart-component");

        totalLabel.getStyleClass().add("donut-total-label");
        subLabel.getStyleClass().add("donut-sub-label");

        VBox centerBox = new VBox(totalLabel, subLabel);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setMouseTransparent(true);

        widthProperty().addListener((obs, oldW, newW) -> {
            canvas.setWidth(newW.doubleValue());
            redraw();
        });
        heightProperty().addListener((obs, oldH, newH) -> {
            canvas.setHeight(newH.doubleValue());
            redraw();
        });

        getChildren().addAll(canvas, centerBox);
    }

    public void update(List<CategorySlice> slices, long totalBytes, long totalFiles, long totalFolders) {
        currentSlices = slices;
        totalLabel.setText(SizeFormatter.format(totalBytes));
        subLabel.setText(String.format("%,d files · %,d folders", totalFiles, totalFolders));
        redraw();
    }

    private void redraw() {
        drawer.draw(canvas.getGraphicsContext2D(), canvas.getWidth(), canvas.getHeight(), currentSlices);
    }

    // Opts out of Canvas's default non-resizable behavior so the StackPane parent drives its size.
    private static class ResizableCanvas extends Canvas {
        @Override
        public boolean isResizable() {
            return true;
        }

        @Override
        public double prefWidth(double height) {
            return 0;
        }

        @Override
        public double prefHeight(double width) {
            return 0;
        }
    }
}
