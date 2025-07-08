package fr.app.ui.view;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import fr.app.domain.FileNode;
import fr.app.ui.view.squarify.SquarifyData;
import fr.app.ui.view.squarify.SquarifyData.DataPoint;
import fr.app.ui.view.squarify.SquarifyRect;
import fr.app.ui.view.squarify.Squarify;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TreemapDrawer {

    public ArrayList<SquarifyRect> buildTreemap(FileNode root, float x, float y, float width, float height) {
        ArrayList<Float> sizes = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();

        for (FileNode child : root.getChildren()) {
            if (child.getSize() > 0) {
                sizes.add((float) child.getSize());
                ids.add(child.getName());
            }
        }

        SquarifyData data = new SquarifyData(sizes, ids, width, height);

        // Ici tu récupères la liste ordonnée de DataPoint
        ArrayList<DataPoint> points = data.getDataPoints();

        // Construire les rectangles avec ton algo Squarify
        Squarify squarify = new Squarify(sizes,ids, x, y, width, height);
        ArrayList<SquarifyRect> rects = squarify.getRects();


        return rects;
    }


    public void drawTreemap(GraphicsContext gc, ArrayList<SquarifyRect> rects) {
        for (SquarifyRect rect : rects) {
            gc.setFill(Color.LIGHTBLUE);
            gc.fillRect(rect.getX(), rect.getY(), rect.getDx(), rect.getDy());

            gc.setStroke(Color.BLACK);
            gc.strokeRect(rect.getX(), rect.getY(), rect.getDx(), rect.getDy());

            gc.setFill(Color.BLACK);
            gc.fillText(rect.getId(), rect.getX() + 2, rect.getY() + 12);
        }
    }
}
