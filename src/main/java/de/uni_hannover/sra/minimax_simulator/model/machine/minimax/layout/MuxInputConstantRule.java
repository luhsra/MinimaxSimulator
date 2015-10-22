package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.ConstraintBuilder;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;

/**
 * Container for a {@link de.uni_hannover.sra.minimax_simulator.model.configuration.mux.ConstantMuxInput}'s
 * {@link de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.Layout}.
 *
 * @author Martin L&uuml;ck
 */
public class MuxInputConstantRule extends DefaultLayoutSet {

	/**
	 * Initializes the {@code MuxInputConstantRule}.
	 *
	 * @param muxName
	 *          the name of the multiplexer
	 * @param pinName
	 *          the name of the {@link de.uni_hannover.sra.minimax_simulator.model.machine.part.Pin}
	 */
	public MuxInputConstantRule(String muxName, String pinName) {
		ConstraintBuilder cb = new ConstraintBuilder();

		String constName = pinName + "_CONSTANT";
		String wireName = pinName + Parts._WIRE;

		addLayout(constName, cb.left(muxName, 15).alignVertically(pinName));
		addLayout(wireName + ".0", cb.right(constName).alignVertically(pinName));
		addLayout(wireName + ".1", cb.align(pinName));
	}
}