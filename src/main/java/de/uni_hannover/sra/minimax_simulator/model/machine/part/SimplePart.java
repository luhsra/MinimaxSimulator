package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;

import java.util.Set;

public abstract class SimplePart extends Part
{
	private final OutgoingPin	_dataOut;

	public SimplePart()
	{
		_dataOut = new OutgoingPin(this);
	}

	public OutgoingPin getDataOut()
	{
		return _dataOut;
	}

	@Override
	public Set<? extends Circuit> getSuccessors()
	{
		return _dataOut.getSuccessors();
	}

	@Override
	public void reset()
	{
	}
}