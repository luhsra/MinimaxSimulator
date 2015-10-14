package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.MuxRegisterLayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.IngoingPin;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Junction;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Wire;

public class MuxRegisterGroup extends AbstractGroup
{
	private final String		_pinId;
	private final IngoingPin	_pin;
	private final String		_registerId;

	private final int			_registerJunctionPin;

	public MuxRegisterGroup(String pinId, IngoingPin muxPin, String registerId,
			int registerJunctionPin)
	{
		_pinId = pinId;
		_pin = muxPin;
		_registerId = registerId;
		_registerJunctionPin = registerJunctionPin;
	}

	@Override
	public void initialize(MachineTopology cr, FontMetricsProvider fontProvider)
	{
		Junction junction = new Junction(2);

		Junction registerJunction = cr.getCircuit(Junction.class, _registerId
			+ Parts._OUT_JUNCTION);

		Wire wireOut = new Wire(2, junction.getDataOuts().get(0), _pin);
		Wire wireIn = new Wire(2,
			registerJunction.getDataOuts().get(_registerJunctionPin),
			junction.getDataIn());

		add(junction, _pinId + Parts._JUNCTION);
		addWire(wireOut, _pinId + Parts._WIRE_DATA_OUT);
		addWire(wireIn, _pinId + Parts._WIRE_DATA_IN);
	}

	@Override
	public boolean hasLayouts()
	{
		return true;
	}

	@Override
	public LayoutSet createLayouts()
	{
		return new MuxRegisterLayoutSet(_pinId, _registerId);
	}
}