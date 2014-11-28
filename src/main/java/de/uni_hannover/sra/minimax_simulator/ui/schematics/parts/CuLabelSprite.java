package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import static com.google.common.base.Preconditions.*;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;

import de.uni_hannover.sra.minimax_simulator.layout.Bounds;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Label;
import de.uni_hannover.sra.minimax_simulator.ui.render.RenderEnvironment;

public class CuLabelSprite extends CircuitSprite
{
	private final Label					_label;

	private final static float			dash[]		= { 10.0f };
	private final static BasicStroke	dashStroke	= new BasicStroke(1.0f,
														BasicStroke.CAP_BUTT,
														BasicStroke.JOIN_MITER, 10.0f,
														dash, 0.0f);

	public CuLabelSprite(Label label)
	{
		_label = checkNotNull(label);
	}

	@Override
	public void paint(Graphics2D g, RenderEnvironment env)
	{
		Bounds b = _label.getBounds();

		debugBounds(g, b);

		String message = _label.getMessage();

		int textWidth = g.getFontMetrics().stringWidth(message);
		int textHeight = g.getFontMetrics().getHeight();

		int textX = b.x + b.w / 2 - textWidth / 2;
		int textY = b.y + b.h / 2 + textHeight / 4 + 1;

		g.drawString(message, textX, textY);

		Stroke stroke = g.getStroke();
		g.setStroke(dashStroke);
		g.drawRect(b.x, b.y, b.w, b.h);
		g.setStroke(stroke);
	}
}