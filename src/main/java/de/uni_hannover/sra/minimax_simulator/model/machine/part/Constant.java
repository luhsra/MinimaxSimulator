package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.parts.ConstantSprite;

public class Constant extends SimplePart implements SpriteOwner
{
	private final int		_constant;

	private final String	_constantStr;

	public Constant(int constantValue)
	{
		_constant = constantValue;

		// Later: Let the user decide when he creates the constant in the GUI?
		if ((0xFFFFFFFFL & constantValue) >= 0x0000FFFFL)
			_constantStr = String.format("0x%X", constantValue);
		else
			_constantStr = Integer.toString(constantValue);
	}

	public int getConstant()
	{
		return _constant;
	}

	public String getConstantStr()
	{
		return _constantStr;
	}

	@Override
	public void update()
	{
		getDataOut().write(_constant);
	}

	@Override
	public Sprite createSprite()
	{
		return new ConstantSprite(this);
	}

	@Override
	public String toString()
	{
		return "Constant[" + _constantStr + "]";
	}
}
