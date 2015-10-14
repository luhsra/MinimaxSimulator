package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.ui.layout.Point;
import de.uni_hannover.sra.minimax_simulator.ui.layout.PointComponent;

public class WirePointComponent extends PointComponent
{
	private final Wire _wire;
	private final int _index;

	public WirePointComponent(Wire wire, int index)
	{
		_wire = wire;
		_index = index;
	}

	@Override
	public void doLayout()
	{
		_wire.getPoints()[_index] = new Point(getBounds().x, getBounds().y);
	}

	@Override
	public String toString()
	{
		return _wire.toString() + "." + _index;
	}
}