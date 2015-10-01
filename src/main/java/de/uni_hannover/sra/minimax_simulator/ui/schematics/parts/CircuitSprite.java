package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

import de.uni_hannover.sra.minimax_simulator.Config;
import de.uni_hannover.sra.minimax_simulator.layout.Bounds;
import de.uni_hannover.sra.minimax_simulator.layout.Point;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Pin;
import de.uni_hannover.sra.minimax_simulator.ui.render.RenderEnvironment;
import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

public abstract class CircuitSprite implements Sprite
{
	@Deprecated
	private final static Polygon	arrowHead;
	static
	{
		arrowHead = new Polygon();
		arrowHead.addPoint(0, 0);
		arrowHead.addPoint(-3, -5);
		arrowHead.addPoint(3, -5);
	}

	private final static class ArrowHead {
		public final static double[] xPoints = {0, -3, 3};
		public final static double[] yPoints = {0, -5, -5};
		public final static int nPoints = 3;
	}

	protected static final javafx.scene.paint.Color RED = new javafx.scene.paint.Color(1f, 0f, 0f, 0.8f);

	@Override
	@Deprecated
	public void paint(Graphics2D g, RenderEnvironment env)
	{
		paint(g);
	}

	@Deprecated
	public void paint(Graphics2D g)
	{
	}

	@Override
	public void paint(GraphicsContext gc, RenderEnvironment env) { paint(gc); }

	// TODO: make abstract?
	public void paint(GraphicsContext gc) {}

	@Deprecated
	public void drawArrow(Graphics2D g, Point point1, Point point2)
	{
		double angle = Math.atan2(point2.y - point1.y, point2.x - point1.x);
		AffineTransform tx = new AffineTransform();
		tx.setToIdentity();
		tx.translate(point2.x, point2.y);
		tx.rotate(angle - Math.PI / 2d);

		Graphics2D g2 = (Graphics2D) g.create();
		g2.transform(tx);
		g2.fill(arrowHead);
		g2.dispose();
	}

	public void drawArrow(GraphicsContext gc, Point point1, Point point2) {
		double angle = Math.atan2(point2.y - point1.y, point2.x - point1.x);
		Affine tx = new Affine();
		tx.setToIdentity();
		tx.appendTranslation(point2.x, point2.y);
		tx.appendRotation(Math.toDegrees(angle) - 90);
System.out.println("The angle is: " + angle + "\t set rotation to: " + (angle - Math.PI / 2d));
		Transform oldTransform = gc.getTransform();
		//gc.transform(tx);		// makes everything white
		gc.setTransform(tx);
		gc.fillPolygon(ArrowHead.xPoints, ArrowHead.yPoints, ArrowHead.nPoints);
		gc.setTransform((Affine)oldTransform);

	}

	@Deprecated
	public void debugBounds(Graphics g, Bounds bounds)
	{
		if (Config.DEBUG_SCHEMATICS)
		{
			g.setColor(new Color(1f, 0f, 0f, 0.8f));
			g.drawRect(bounds.x, bounds.y, bounds.w, bounds.h);
			g.setColor(Color.BLACK);
		}
	}

	public void debugBounds(GraphicsContext gc, Bounds bounds) {
		if (Config.DEBUG_SCHEMATICS) {
			gc.setStroke(RED);
			gc.strokeRect(bounds.x, bounds.y, bounds.w, bounds.h);
			gc.setStroke(javafx.scene.paint.Color.BLACK);
		}
	}

	@Deprecated
	public void debugPin(Graphics g, Pin pin)
	{
		if (Config.DEBUG_SCHEMATICS)
		{
			debugPosition(g, pin.getBounds().x, pin.getBounds().y);
		}
	}

	public void debugPin(GraphicsContext gc, Pin pin) {
		if (Config.DEBUG_SCHEMATICS) {
			debugPosition(gc, pin.getBounds().x, pin.getBounds().y);
		}
	}

	@Deprecated
	public void debugPosition(Graphics g, int x, int y)
	{
		if (Config.DEBUG_SCHEMATICS)
		{
			g.setColor(new Color(1f, 0f, 0f, 0.8f));
			g.fillOval(x - 2, y - 2, 5, 5);
			g.setColor(Color.BLACK);
		}
	}

	public void debugPosition(GraphicsContext gc, int x, int y) {
		if (Config.DEBUG_SCHEMATICS) {
			gc.setFill(RED);
			gc.fillOval(x - 2, y - 2, 5, 5);
			gc.setFill(javafx.scene.paint.Color.BLACK);
		}
	}
}