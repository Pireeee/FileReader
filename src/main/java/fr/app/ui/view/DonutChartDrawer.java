package fr.app.ui.view;

import fr.app.domain.FileNode;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DonutChartDrawer {

    private static final double GAP_DEGREES = 2.5;
    private static final double RING_THICKNESS = 40;
    private static final double MIN_ROUND_CAP_DEGREES = 8;

    private static final Color[] PALETTE = {
            Color.web("#9C27B0"), // purple
            Color.web("#4CAF50"), // green
            Color.web("#2E9BB5"), // teal/blue
            Color.web("#D9A441"), // gold
    };
    private static final Color OTHER_COLOR = Color.web("#9E9E9E"); // gray

    public List<CategorySlice> buildSlices(FileNode root, int maxCategories) {
        List<CategorySlice> slices = new ArrayList<>();
        if (root == null || root.getChildren() == null || root.getChildren().isEmpty()) {
            return slices;
        }

        List<FileNode> sorted = root.getChildren().stream()
                .sorted(Comparator.comparingLong(FileNode::getSize).reversed())
                .toList();

        int topCount = Math.min(maxCategories, sorted.size());
        for (int i = 0; i < topCount; i++) {
            FileNode node = sorted.get(i);
            slices.add(new CategorySlice(node.getName(), node.getSize(), 1, PALETTE[i % PALETTE.length]));
        }

        if (sorted.size() > topCount) {
            List<FileNode> rest = sorted.subList(topCount, sorted.size());
            long otherSize = rest.stream().mapToLong(FileNode::getSize).sum();
            slices.add(new CategorySlice("Other (" + rest.size() + " items)", otherSize, rest.size(), OTHER_COLOR));
        }

        return slices;
    }

    public void draw(GraphicsContext ctx, double width, double height, List<CategorySlice> slices) {
        ctx.clearRect(0, 0, width, height);
        if (slices == null || slices.isEmpty()) {
            return;
        }

        double total = slices.stream().mapToLong(CategorySlice::size).sum();
        if (total <= 0) {
            return;
        }

        double size = Math.min(width, height) - RING_THICKNESS;
        if (size <= 0) {
            return;
        }
        double x = (width - size) / 2.0;
        double y = (height - size) / 2.0;

        ctx.setLineWidth(RING_THICKNESS);

        double cumulative = 0;
        for (CategorySlice slice : slices) {
            double startDeg = 90 - (cumulative / total) * 360.0;
            double sweepDeg = (slice.size() / total) * 360.0;
            cumulative += slice.size();

            boolean fullCircle = sweepDeg >= 359.9;
            double drawSweep = fullCircle ? -359.9 : -Math.max(0, sweepDeg - GAP_DEGREES);
            double drawStart = fullCircle ? startDeg : startDeg - GAP_DEGREES / 2.0;

            ctx.setStroke(slice.color());
            ctx.setLineCap(sweepDeg < MIN_ROUND_CAP_DEGREES ? StrokeLineCap.BUTT : StrokeLineCap.ROUND);
            ctx.strokeArc(x, y, size, size, drawStart, drawSweep, ArcType.OPEN);
        }
    }
}
