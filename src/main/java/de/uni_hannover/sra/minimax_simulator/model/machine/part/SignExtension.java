package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.parts.SignExtSprite;

public class SignExtension extends SimplePart implements SpriteOwner
{
	private final IngoingPin	_dataIn;
	private final String _label;

	public SignExtension(String label)
	{
		_dataIn = new IngoingPin(this);
		_label = label;
	}

	public IngoingPin getDataIn()
	{
		return _dataIn;
	}

	@Override
	public void update()
	{
		int value = _dataIn.read();
		
		int bit24 = value & 0x00800000;

		if (bit24 == 0)
			value = value & 0x00FFFFFF; // zero first 8 bits
		else
			value = value | 0xFF000000; // set first 8 bits
		
		getDataOut().write(value);
	}

	public String getLabel()
	{
		return _label;
	}

	@Override
	public Sprite createSprite()
	{
		return new SignExtSprite(this);
	}
}