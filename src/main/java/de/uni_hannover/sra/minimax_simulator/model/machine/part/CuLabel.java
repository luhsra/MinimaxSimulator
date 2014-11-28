package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.parts.CuLabelSprite;

public class CuLabel extends Label
{
	public CuLabel()
	{
		super("CU");
	}

	@Override
	public Sprite createSprite()
	{
		return new CuLabelSprite(this);
	}
}