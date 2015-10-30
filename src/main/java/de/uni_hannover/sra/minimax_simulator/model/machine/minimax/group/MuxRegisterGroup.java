package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.MuxRegisterLayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.IngoingPin;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Junction;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Wire;

/**
 * Groups the parts of a {@link de.uni_hannover.sra.minimax_simulator.model.configuration.mux.RegisterMuxInput}.
 *
 * @author Martin L&uuml;ck
 */
// TODO: unused <-> delete?
public class MuxRegisterGroup extends AbstractGroup {

	private final String pinId;
	private final IngoingPin pin;
	private final String registerId;

	private final int registerJunctionPin;

	/**
	 * Constructs a new {@code MuxRegisterGroup} with the specified pin name, pin, register ID and register pin.
	 *
	 * @param pinId
	 *          the ID of the pin
	 * @param muxPin
	 *          the multiplexer {@code IngoingPin}
	 * @param registerId
	 *          the ID of the register
	 * @param registerJunctionPin
	 *          the register junction pin
	 */
	public MuxRegisterGroup(String pinId, IngoingPin muxPin, String registerId, int registerJunctionPin) {
		this.pinId = pinId;
		pin = muxPin;
		this.registerId = registerId;
		this.registerJunctionPin = registerJunctionPin;
	}

	@Override
	public void initialize(MachineTopology cr, FontMetricsProvider fontProvider) {
		Junction junction = new Junction(2);

		Junction registerJunction = cr.getCircuit(Junction.class, registerId + Parts._OUT_JUNCTION);

		Wire wireOut = new Wire(2, junction.getDataOuts().get(0), pin);
		Wire wireIn = new Wire(2, registerJunction.getDataOuts().get(registerJunctionPin), junction.getDataIn());

		add(junction, pinId + Parts._JUNCTION);
		addWire(wireOut, pinId + Parts._WIRE_DATA_OUT);
		addWire(wireIn, pinId + Parts._WIRE_DATA_IN);
	}

	@Override
	public boolean hasLayouts() {
		return true;
	}

	@Override
	public LayoutSet createLayouts() {
		return new MuxRegisterLayoutSet(pinId, registerId);
	}
}