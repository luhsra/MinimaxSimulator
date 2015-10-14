package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.ui.layout.AbstractComponent;
import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.parts.LabelSprite;

public class Label extends AbstractComponent implements SpriteOwner
{
	private final String _message;

	public Label(String message)
	{
		_message = message;
	}

	public String getMessage()
	{
		return _message;
	}

	@Override
	public Sprite createSprite()
	{
		return new LabelSprite(this);
	}
}