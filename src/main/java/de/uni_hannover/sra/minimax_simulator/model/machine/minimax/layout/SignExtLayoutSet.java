package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.ConstraintBuilder;

/**
 * Container for the {@link de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.Layout}s
 * of a {@link de.uni_hannover.sra.minimax_simulator.model.machine.part.SignExtension}.
 *
 * @author Martin L&uuml;ck
 */
public class SignExtLayoutSet extends DefaultLayoutSet {

	/**
	 * Initializes the {@code SignExtLayoutSet}.
	 */
	public SignExtLayoutSet() {
		String junctionName = Parts.IR + Parts._WIRE + Parts._JUNCTION;
		String portName = Parts.SIGN_EXTENSION + Parts._CU + Parts._PORT;
		String labelName = Parts.SIGN_EXTENSION + Parts._CU + Parts._LABEL;

		ConstraintBuilder cb = new ConstraintBuilder();

		addLayout(junctionName, cb.alignVertically(Parts.IR).left(Parts.IR, 80));
		addLayout(Parts.SIGN_EXTENSION, cb.alignHorizontally(junctionName).above(junctionName, 27));
		addLayout(portName, cb.alignHorizontally(junctionName).below(junctionName, 27));
		addLayout(labelName, cb.alignHorizontally(portName).below(portName));

		String dataInWire = Parts.SIGN_EXTENSION + Parts._JUNCTION + Parts._WIRE_DATA_IN;
		addLayout(dataInWire + ".0", cb.alignVertically(Parts.IR).left(Parts.IR));
		addLayout(dataInWire + ".1", cb.align(junctionName));

		String toCuWire = Parts.IR + Parts._CU + Parts._WIRE_DATA_IN;
		addLayout(toCuWire + ".0", cb.align(junctionName));
		addLayout(toCuWire + ".1", cb.align(portName));

		String toExtWire = Parts.SIGN_EXTENSION + Parts._WIRE_DATA_IN;
		addLayout(toExtWire + ".0", cb.align(junctionName));
		addLayout(toExtWire + ".1", cb.below(Parts.SIGN_EXTENSION).alignHorizontally(Parts.SIGN_EXTENSION));
	}
}