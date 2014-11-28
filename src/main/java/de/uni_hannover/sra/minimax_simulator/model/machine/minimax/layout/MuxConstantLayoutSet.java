package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import de.uni_hannover.sra.minimax_simulator.layout.constraint.ConstraintBuilder;

public class MuxConstantLayoutSet extends DefaultLayoutSet
{
	public MuxConstantLayoutSet(String pinName)
	{
		String constName = pinName + "_CONSTANT";
		String wireName = pinName + Parts._WIRE;

		ConstraintBuilder cb = new ConstraintBuilder();

		addLayout(constName, cb.left(pinName, 20).alignVertically(pinName));
		addLayout(wireName + ".0", cb.alignVertically(constName).right(constName, 5));
		addLayout(wireName + ".1", cb.align(pinName));
	}
}