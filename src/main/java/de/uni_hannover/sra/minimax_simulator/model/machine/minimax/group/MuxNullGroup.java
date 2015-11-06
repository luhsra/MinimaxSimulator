package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.MuxNullLayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.IngoingPin;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Junction;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Wire;

/**
 * Groups the parts of a {@link de.uni_hannover.sra.minimax_simulator.model.configuration.mux.NullMuxInput}.
 *
 * @author Martin L&uuml;ck
 */
public class MuxNullGroup extends AbstractGroup {

	private final String pinName;
	private final IngoingPin muxPin;

	/**
	 * Constructs a new {@code MuxNullGroup} with the specified pin name and {@link IngoingPin}.
	 *
	 * @param pinName
	 *          the name of the pin
	 * @param muxPin
	 *          the {@code IngoingPin} which is {@code NullMuxInput}
	 */
	public MuxNullGroup(String pinName, IngoingPin muxPin) {
		this.pinName = pinName;
		this.muxPin = muxPin;
	}

	@Override
	public void initialize(MachineTopology cr, FontMetricsProvider fontProvider) {
		Junction deadEnd = new Junction(2);
		Wire wire = new Wire(2, deadEnd.getDataOuts().get(0), muxPin);

		add(deadEnd, pinName + "_END");
		addWire(wire, pinName + Parts._WIRE);
	}

	@Override
	public boolean hasLayouts() {
		return true;
	}

	@Override
	public LayoutSet createLayouts() {
		return new MuxNullLayoutSet(pinName);
	}
}