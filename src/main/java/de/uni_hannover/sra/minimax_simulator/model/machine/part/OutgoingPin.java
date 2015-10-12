package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;

import java.util.HashSet;
import java.util.Set;

public class OutgoingPin extends Pin
{
	private final Set<Wire> _wires;

	public OutgoingPin(Part part)
	{
		super(part);
		_wires = new HashSet<Wire>();
	}

	public void write(int value)
	{
		setValue(value);
	}

	public Set<? extends Circuit> getSuccessors()
	{
		return _wires;
	}

	public Set<Wire> getWires()
	{
		return _wires;
	}
}