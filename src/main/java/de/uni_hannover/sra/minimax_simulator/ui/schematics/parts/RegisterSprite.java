package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import static com.google.common.base.Preconditions.*;

import java.awt.Color;
import java.awt.Graphics2D;

import de.uni_hannover.sra.minimax_simulator.layout.Bounds;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Register;

public class RegisterSprite extends CircuitSprite
{
	public final static Color EXTENDED_REGISTER = new Color(0.95f, 0.95f, 0.95f);

	private final Register _register;

	public RegisterSprite(Register reg)
	{
		_register = checkNotNull(reg);
	}

	@Override
	public void paint(Graphics2D g)
	{
		debugBounds(g, _register.getBounds());

		String name = _register.getLabel();

		final Bounds b = _register.getBounds();

		int textWidth = g.getFontMetrics().stringWidth(name);
		int textHeight = g.getFontMetrics().getHeight();

		int drawHeight = b.h; //textHeight + REGISTER_PADDING;
		int drawWidth = b.w; //Math.max(textWidth + REGISTER_PADDING, REGISTER_MIN_WIDTH);

		// Difference between estimated size (bounds) and font metrics
		int deltaW = drawWidth - b.w;
		int deltaH = drawHeight - b.h;

		int x0 = b.x - deltaW / 2;
		int y0 = b.y - deltaH / 2;

		if (_register.isExtended())
		{
			g.setColor(EXTENDED_REGISTER);
			g.fillRect(x0, y0, drawWidth, drawHeight);
			g.setColor(Color.BLACK);
		}

		int xCenter = x0 + drawWidth / 2;
		int yCenter = y0 + drawHeight / 2;

		g.drawRect(x0, y0, drawWidth, drawHeight);
		g.drawString(name, xCenter - textWidth / 2, yCenter + textHeight / 4 + 1);

		debugPin(g, _register.getDataIn());
		debugPin(g, _register.getDataOut());
		debugPin(g, _register.getWriteEnabled());
	}
}