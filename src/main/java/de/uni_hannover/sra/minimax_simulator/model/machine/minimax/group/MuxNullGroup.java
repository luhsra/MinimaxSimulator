package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group;

import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.MuxNullLayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.IngoingPin;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Junction;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Wire;

public class MuxNullGroup extends AbstractGroup
{
	private final String _pinName;
	private final IngoingPin _muxPin;

	public MuxNullGroup(String pinName, IngoingPin muxPin)
	{
		_pinName = pinName;
		_muxPin = muxPin;
	}

	@Override
	public void initialize(MachineTopology cr, FontMetricsProvider fontProvider)
	{
		Junction deadEnd = new Junction(2);
		Wire wire = new Wire(2, deadEnd.getDataOuts().get(0), _muxPin);

		add(deadEnd, _pinName + "_END");
		addWire(wire, _pinName + Parts._WIRE);
	}

	@Override
	public boolean hasLayouts()
	{
		return true;
	}

	@Override
	public LayoutSet createLayouts()
	{
		return new MuxNullLayoutSet(_pinName);
	}
}