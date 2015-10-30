package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.MuxConstantLayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Constant;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.IngoingPin;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Wire;
import de.uni_hannover.sra.minimax_simulator.model.machine.shape.ConstantShape;

/**
 * Groups the parts of a {@link de.uni_hannover.sra.minimax_simulator.model.configuration.mux.ConstantMuxInput}.
 *
 * @author Martin L&uuml;ck
 */
public class MuxConstantGroup extends AbstractGroup {

	private final String pinName;
	private final IngoingPin muxPin;

	private final int value;

	/**
	 * Constructs a new {@code MuxConstantGroup} for the specified pin with the specified value.
	 *
	 * @param pinName
	 *          the name of the pin
	 * @param muxPin
	 *          the multiplexer pin
	 * @param value
	 *          the value of the constant
	 */
	public MuxConstantGroup(String pinName, IngoingPin muxPin, int value) {
		this.pinName = pinName;
		this.muxPin = muxPin;

		this.value = value;
	}

	@Override
	public void initialize(MachineTopology cr, FontMetricsProvider fontProvider) {
		Constant constant = new Constant(value);
		constant.setShape(new ConstantShape(fontProvider));

		Wire wire = new Wire(2, constant.getDataOut(), muxPin);

		add(constant, pinName + Parts._CONSTANT);
		addWire(wire, pinName + Parts._WIRE);
	}

	@Override
	public boolean hasLayouts() {
		return true;
	}

	@Override
	public LayoutSet createLayouts() {
		return new MuxConstantLayoutSet(pinName);
	}
}