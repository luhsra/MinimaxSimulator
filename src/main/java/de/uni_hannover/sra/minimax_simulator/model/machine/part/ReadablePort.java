package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.ResultPort;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;

import java.util.Collections;
import java.util.Set;

public class ReadablePort extends Part implements ResultPort
{
	private final IngoingPin _in;

	private int _value;

	public ReadablePort()
	{
		_in = new IngoingPin(this);
	}

	@Override
	public void update()
	{
		_value = _in.read();
	}

	public IngoingPin getIn()
	{
		return _in;
	}

	@Override
	public Set<Circuit> getSuccessors()
	{
		return Collections.emptySet();
	}

	@Override
	public int read()
	{
		return _value;
	}

	@Override
	public void reset()
	{
		_value = 0;
	}

	@Override
	public String toString()
	{
		return "ReadablePort[name=" + getName() + ", value=" + _value + "]";
	}
}