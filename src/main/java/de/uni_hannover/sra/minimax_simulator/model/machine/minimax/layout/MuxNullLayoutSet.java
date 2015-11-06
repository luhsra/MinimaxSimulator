package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.ConstraintBuilder;

/**
 * Container for the {@link de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.Layout}s
 * of a {@link de.uni_hannover.sra.minimax_simulator.model.configuration.mux.NullMuxInput}.
 *
 * @author Martin L&uuml;ck
 */
public class MuxNullLayoutSet extends DefaultLayoutSet {

	/**
	 * Initializes the {@code MuxNullLayoutSet}.
	 *
	 * @param pinName
	 *          the name of the {@link de.uni_hannover.sra.minimax_simulator.model.machine.part.Pin}
	 */
	public MuxNullLayoutSet(String pinName) {
		String endName = pinName + "_END";
		String wireName = pinName + Parts._WIRE;

		ConstraintBuilder cb = new ConstraintBuilder();

		addLayout(endName, cb.left(pinName, 15).alignVertically(pinName));
		addLayout(wireName + ".0", cb.align(endName));
		addLayout(wireName + ".1", cb.align(pinName));
	}
}