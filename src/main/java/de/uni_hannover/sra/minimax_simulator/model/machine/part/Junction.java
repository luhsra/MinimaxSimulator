package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;
import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.parts.JunctionSprite;

public class Junction extends Part implements SpriteOwner
{
	private final ArrayList<OutgoingPin> _dataOuts;

	private final IngoingPin _dataIn;

	public Junction()
	{
		this(0);
	}

	public Junction(int pinCount)
	{
		_dataIn = new IngoingPin(this);

		_dataOuts = new ArrayList<OutgoingPin>(pinCount);
		for (int i = 0; i < pinCount; i++)
			_dataOuts.add(new OutgoingPin(this));
	}

	public List<OutgoingPin> getDataOuts()
	{
		return _dataOuts;
	}

	public IngoingPin getDataIn()
	{
		return _dataIn;
	}

	@Override
	public void update()
	{
		int value = _dataIn.read();
		for (OutgoingPin out : _dataOuts)
			out.write(value);
	}

	@Override
	public Set<? extends Circuit> getSuccessors()
	{
		Set<Circuit> successors = new HashSet<Circuit>();
		for (OutgoingPin pin : _dataOuts)
		{
			successors.addAll(pin.getSuccessors());
		}
		return successors;
	}

	@Override
	public Sprite createSprite()
	{
		return new JunctionSprite(this);
	}

	@Override
	public void reset()
	{
	}
}
