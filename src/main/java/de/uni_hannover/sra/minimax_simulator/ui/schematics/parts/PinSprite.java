package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import static com.google.common.base.Preconditions.*;

import java.awt.Color;
import java.awt.Graphics2D;

import de.uni_hannover.sra.minimax_simulator.model.machine.part.Pin;
import de.uni_hannover.sra.minimax_simulator.ui.render.RenderEnvironment;
import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

// TODO: delete because it is unused?
public class PinSprite implements Sprite
{
	private final Pin _pin;

	public PinSprite(Pin pin)
	{
		_pin = checkNotNull(pin);
	}

	@Override
	@Deprecated
	public void paint(Graphics2D g, RenderEnvironment env)
	{
		int x = _pin.getBounds().x;
		int y = _pin.getBounds().y;

		g.setColor(new Color(1f, 0f, 0f, 0.8f));
		g.fillOval(x - 2, y - 2, 5, 5);
		//g.setColor(Color.BLACK);
	}

	@Override
	public void paint(GraphicsContext gc, RenderEnvironment env) {
		int x = _pin.getBounds().x;
		int y = _pin.getBounds().y;

		Paint fill = gc.getFill();
		gc.setFill(new javafx.scene.paint.Color(1f, 0f, 0f, 0.8f));
		gc.fillOval(x - 2, y - 2, 5, 5);
		gc.setFill(fill);
	}
}