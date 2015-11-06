package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.ConstraintBuilder;

/**
 * Groups the {@link de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.Layout}s
 * of the components of a {@link de.uni_hannover.sra.minimax_simulator.model.configuration.mux.ConstantMuxInput}.
 *
 * @author Martin L&uuml;ck
 */
public class MuxConstantLayoutSet extends DefaultLayoutSet {

	/**
	 * Constructs a new {@code MuxConstantLayoutSet} for the specified pin.
	 *
	 * @param pinName
	 *          the name of the pin
	 */
	public MuxConstantLayoutSet(String pinName) {
		String constName = pinName + "_CONSTANT";
		String wireName = pinName + Parts._WIRE;

		ConstraintBuilder cb = new ConstraintBuilder();

		addLayout(constName, cb.left(pinName, 20).alignVertically(pinName));
		addLayout(wireName + ".0", cb.alignVertically(constName).right(constName, 5));
		addLayout(wireName + ".1", cb.align(pinName));
	}
}