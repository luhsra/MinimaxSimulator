package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import static com.google.common.base.Preconditions.*;

import java.awt.Graphics2D;

import de.uni_hannover.sra.minimax_simulator.layout.Bounds;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.SignExtension;
import de.uni_hannover.sra.minimax_simulator.ui.render.RenderEnvironment;

public class SignExtSprite extends CircuitSprite
{
	private final SignExtension _signExt;

	public SignExtSprite(SignExtension signExt)
	{
		_signExt = checkNotNull(signExt);
	}

	@Override
	public void paint(Graphics2D g, RenderEnvironment env)
	{
		Bounds b = _signExt.getBounds();

		debugBounds(g, b);

		g.drawOval(b.x, b.y, b.w, b.h);

		int textWidth = g.getFontMetrics().stringWidth(_signExt.getLabel());
		int textHeight = g.getFontMetrics().getHeight();

		int textX = b.x + b.w / 2 - textWidth / 2;
		int textY = b.y + b.h / 2 + textHeight / 4 + 1;

		g.drawString(_signExt.getLabel(), textX, textY);
	}
}
