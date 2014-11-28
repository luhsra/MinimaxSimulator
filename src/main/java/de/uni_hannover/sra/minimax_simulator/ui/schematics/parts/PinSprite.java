package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import static com.google.common.base.Preconditions.*;

import java.awt.Color;
import java.awt.Graphics2D;

import de.uni_hannover.sra.minimax_simulator.model.machine.part.Pin;
import de.uni_hannover.sra.minimax_simulator.ui.render.RenderEnvironment;
import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;

public class PinSprite implements Sprite
{
	private final Pin _pin;

	public PinSprite(Pin pin)
	{
		_pin = checkNotNull(pin);
	}

	@Override
	public void paint(Graphics2D g, RenderEnvironment env)
	{
		int x = _pin.getBounds().x;
		int y = _pin.getBounds().y;

		g.setColor(new Color(1f, 0f, 0f, 0.8f));
		g.fillOval(x - 2, y - 2, 5, 5);
		//g.setColor(Color.BLACK);
	}
}