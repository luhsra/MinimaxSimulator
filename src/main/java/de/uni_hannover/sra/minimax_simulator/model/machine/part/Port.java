package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.ControlPort;

public class Port extends SimplePart implements ControlPort
{
	private int _value;

	public Port(String name)
	{
		setName(name);
	}

	public String getPortId()
	{
		return getName();
	}

	@Override
	public void write(int value)
	{
		_value = value;
	}

	@Override
	public void update()
	{
		getDataOut().write(_value);
	}

	@Override
	public void reset()
	{
		_value = 0;
	}

	@Override
	public String toString()
	{
		return "Port[name=" + getName() + ", value=" + _value + "]";
	}
}