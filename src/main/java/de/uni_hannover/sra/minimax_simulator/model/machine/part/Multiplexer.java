package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import java.util.ArrayList;
import java.util.List;

import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.parts.MultiplexerSprite;

public class Multiplexer extends SimplePart implements SpriteOwner
{
	private final IngoingPin _select = new IngoingPin(this);
	private final List<IngoingPin> _dataIns;

	public Multiplexer()
	{
		this(0);
	}

	public Multiplexer(int numberOfInputs)
	{
		_dataIns = new ArrayList<IngoingPin>(numberOfInputs);
		for (int i = 0; i < numberOfInputs; i++)
			_dataIns.add(new IngoingPin(this));
	}

	public List<IngoingPin> getDataInputs()
	{
		return _dataIns;
	}

	public IngoingPin getSelectPin()
	{
		return _select;
	}

	@Override
	public void update()
	{
		int index = _select.read();
		if (index < _dataIns.size())
		{
			getDataOut().write(_dataIns.get(index).read());	
		}
		else
		{
			getDataOut().write(0);
		}
	}

	@Override
	public Sprite createSprite()
	{
		return new MultiplexerSprite(this);
	}
}