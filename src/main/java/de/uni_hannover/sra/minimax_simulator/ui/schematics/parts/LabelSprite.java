package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import static com.google.common.base.Preconditions.*;

import java.awt.Graphics2D;

import de.uni_hannover.sra.minimax_simulator.layout.Bounds;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Label;
import de.uni_hannover.sra.minimax_simulator.ui.render.RenderEnvironment;

public class LabelSprite extends CircuitSprite
{
	private final Label					_label;

	public LabelSprite(Label label)
	{
		_label = checkNotNull(label);
	}

	@Override
	public void paint(Graphics2D g, RenderEnvironment env)
	{
		Bounds b = _label.getBounds();

		debugBounds(g, b);

		g.drawString(_label.getMessage(), b.x, b.y + b.h);
	}
}