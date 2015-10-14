package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import de.uni_hannover.sra.minimax_simulator.Config;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Bounds;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Point;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Pin;
import de.uni_hannover.sra.minimax_simulator.ui.render.RenderEnvironment;
import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

/**
 * A {@link Sprite} of a component of the Minimax machine's circuit.
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public abstract class CircuitSprite implements Sprite {

	/**
	 * A polygon that looks like an arrow head and can be drawn with
	 * {@link GraphicsContext#fillPolygon(double[], double[], int)}.
	 *
	 * @author Philipp Rohde
	 */
	private final static class ArrowHead {
		public final static double[] xPoints = {0, -3, 3};
		public final static double[] yPoints = {0, -5, -5};
		public final static int nPoints = 3;
	}

	protected static final Color RED = new Color(1f, 0f, 0f, 0.8f);

	@Override
	public void paint(GraphicsContext gc, RenderEnvironment env) { paint(gc); }

	/**
	 * Provides a method for drawing a {@code CircuitSprite} without a {@code RenderEnvironment}.
	 *
	 * @param gc
	 *          the {@code GraphicsContext} the {@code CircuitSprite} will be drawn on
	 */
	// TODO: make abstract?
	public void paint(GraphicsContext gc) {}

	/**
	 * Draws the arrow head at the end of a wire.<br>
	 * Therefore the polygon will be translated and rotated.
	 *
	 * @param gc
	 *          the {@code GraphicsContext} the arrow will be drawn on
	 * @param point1
	 *          the starting point
	 * @param point2
	 *          the end point
	 */
	public void drawArrow(GraphicsContext gc, Point point1, Point point2) {
		double angle = Math.atan2(point2.y - point1.y, point2.x - point1.x);
		Affine tx = new Affine();
		tx.setToIdentity();
		tx.appendTranslation(point2.x, point2.y);
		tx.appendRotation(Math.toDegrees(angle) - 90);

		Transform oldTransform = gc.getTransform();
		gc.setTransform(tx);
		gc.fillPolygon(ArrowHead.xPoints, ArrowHead.yPoints, ArrowHead.nPoints);
		gc.setTransform((Affine)oldTransform);

	}

	/**
	 * Debugs {@link Bounds} on a {@code GraphicsContext}.
	 *
	 * @param gc
	 *          the {@code GraphicsContext} the {@code Bounds} will be debugged on
	 * @param bounds
	 *          the {@code Bounds} to debug
	 */
	public void debugBounds(GraphicsContext gc, Bounds bounds) {
		if (Config.DEBUG_SCHEMATICS) {
			gc.setStroke(RED);
			gc.strokeRect(bounds.x, bounds.y, bounds.w, bounds.h);
			gc.setStroke(Color.BLACK);
		}
	}

	/**
	 * Debugs a {@link Pin} on a {@code GraphicsContext}.
	 *
	 * @param gc
	 *          the {@code GraphicsContext} the {@code Pin} will be debugged on
	 * @param pin
	 *          the {@code Pin} to debug
	 */
	public void debugPin(GraphicsContext gc, Pin pin) {
		if (Config.DEBUG_SCHEMATICS) {
			debugPosition(gc, pin.getBounds().x, pin.getBounds().y);
		}
	}

	/**
	 * Debugs a point on a {@code GraphicsContext}.
	 *
	 * @param gc
	 *          the {@code GraphicsContext} the point will be debugged on
	 * @param x
	 *          the x coordinate of the point to debug
	 * @param y
	 *          the y coordinate of the point to debug
	 */
	public void debugPosition(GraphicsContext gc, int x, int y) {
		if (Config.DEBUG_SCHEMATICS) {
			gc.setFill(RED);
			gc.fillOval(x - 2, y - 2, 5, 5);
			gc.setFill(Color.BLACK);
		}
	}
}