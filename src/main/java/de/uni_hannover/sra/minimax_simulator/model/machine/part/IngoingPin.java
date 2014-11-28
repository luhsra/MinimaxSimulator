package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;

public class IngoingPin extends Pin
{
	private Wire _wire;
	private Set<? extends Circuit> _thePart;

	public IngoingPin(Part part)
	{
		super(part);
		_thePart = ImmutableSet.of(part); 
	}

	public Wire getWire()
	{
		return _wire;
	}

	public void setWire(Wire wire)
	{
		_wire = wire;
	}

	public int read()
	{
		return getValue();
	}

	public Set<? extends Circuit> getSuccessors()
	{
		return _thePart;
	}
}