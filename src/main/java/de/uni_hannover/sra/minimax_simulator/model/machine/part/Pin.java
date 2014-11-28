package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import static com.google.common.base.Preconditions.*;
import de.uni_hannover.sra.minimax_simulator.model.layout.component.PointComponent;

public abstract class Pin extends PointComponent
{
	private final Part _part;

	private int _value;

	public Pin(Part part)
	{
		_part = checkNotNull(part);
	}

	public Part getPart()
	{
		return _part;
	}

	public int getValue()
	{
		return _value;
	}

	public void setValue(int value)
	{
		_value = value;
	}
}