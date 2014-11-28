package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.layout.constraint.ConstraintBuilder;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;

public class AluLayoutSet extends DefaultLayoutSet
{
	public AluLayoutSet()
	{
		String wire = Parts.ALU + Parts._WIRE_CTRL;

		ConstraintBuilder cb = new ConstraintBuilder();

		addLayout(Parts.ALU + Parts._PORT, cb.alignHorizontally(Parts.ALU_CTRL_IN).above(Parts.ALU_CTRL_IN, 30));
		addLayout(Parts.ALU + Parts._LABEL, cb.alignHorizontally(Parts.ALU + Parts._PORT).above(Parts.ALU + Parts._PORT, 2));
		addLayout(wire + ".0", cb.align(Parts.ALU + Parts._PORT));
		addLayout(wire + ".1", cb.align(Parts.ALU_CTRL_IN));

		addLayout(Parts.ALU_COND_PORT, cb.alignHorizontally(Parts.ALU_LINE).above(Parts.ALU));
		addLayout(Parts.ALU_COND_PORT + Parts._LABEL, cb.alignHorizontally(Parts.ALU_LINE).above(Parts.ALU_COND_PORT));

		String aluCuWire = Parts.ALU + Parts._CU + Parts._WIRE;
		addLayout(aluCuWire + ".0", cb.align(Parts.ALU_COND_OUT));
		addLayout(aluCuWire + ".1", cb.alignVertically(Parts.ALU_COND_OUT).alignHorizontally(Parts.ALU_LINE));
		addLayout(aluCuWire + ".2", cb.align(Parts.ALU_COND_PORT));

		addLayout(Parts.ALU_DATA_OUT + Parts._LABEL, cb.right(Parts.ALU, 25).below(Parts.ALU_DATA_OUT, 3));
		addLayout(Parts.ALU_COND_OUT + Parts._LABEL, cb.right(Parts.ALU, 25).above(Parts.ALU_COND_OUT, 3));
	}
}