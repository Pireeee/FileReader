package fr.app.ui.view;

import javafx.scene.paint.Color;

/**
 * Shared color palette for top-level category coloring, used by both the
 * donut chart and the tree table so a folder's color stays consistent
 * across views.
 */
public final class CategoryPalette {

    public static final Color[] COLORS = {
            Color.web("#9C27B0"), // purple
            Color.web("#4CAF50"), // green
            Color.web("#2E9BB5"), // teal/blue
            Color.web("#D9A441"), // gold
            Color.web("#E4572E"), // orange-red
            Color.web("#3F51B5"), // indigo
            Color.web("#00897B"), // dark teal
    };

    public static final Color OTHER = Color.web("#9E9E9E"); // gray

    private CategoryPalette() {
    }
}