package fr.app.ui.view.squarify;

/**
 * SquarifiedRect is a class that holds the geometry
 * of each rectangles in the layout
 * @author agathelenclen
 *
 */
public class SquarifyRect {
	private float x, y, dx, dy, value;
	private String id; // ← c’est maintenant un String

	public SquarifyRect(float x, float y, float dx, float dy, String id, float value) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.id = id;
		this.value = value;
	}

	public SquarifyRect(float x, float y, float dx, float dy) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
	}

	public float getX() { return x; }
	public float getY() { return y; }
	public float getDx() { return dx; }
	public float getDy() { return dy; }
	public float getValue() { return value; }
	public String getId() { return id; } // ← maintenant ça retourne bien ton nom
}