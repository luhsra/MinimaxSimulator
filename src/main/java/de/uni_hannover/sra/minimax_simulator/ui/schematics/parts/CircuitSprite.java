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

public abstract class CircuitSprite implements Sprite
{
	private final static Polygon	arrowHead;
	static
	{
		arrowHead = new Polygon();
		arrowHead.addPoint(0, 0);
		arrowHead.addPoint(-3, -5);
		arrowHead.addPoint(3, -5);
	}

	@Override
	public void paint(Graphics2D g, RenderEnvironment env)
	{
		paint(g);
	}

	public void paint(Graphics2D g)
	{
	}

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

	public void debugBounds(Graphics g, Bounds bounds)
	{
		if (Config.DEBUG_SCHEMATICS)
		{
			g.setColor(new Color(1f, 0f, 0f, 0.8f));
			g.drawRect(bounds.x, bounds.y, bounds.w, bounds.h);
			g.setColor(Color.BLACK);
		}
	}

	public void debugPin(Graphics g, Pin pin)
	{
		if (Config.DEBUG_SCHEMATICS)
		{
			debugPosition(g, pin.getBounds().x, pin.getBounds().y);
		}
	}

	public void debugPosition(Graphics g, int x, int y)
	{
		if (Config.DEBUG_SCHEMATICS)
		{
			g.setColor(new Color(1f, 0f, 0f, 0.8f));
			g.fillOval(x - 2, y - 2, 5, 5);
			g.setColor(Color.BLACK);
		}
	}
}