package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.layout.constraint.ConstraintBuilder;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;

public class MuxInputConstantRule extends DefaultLayoutSet
{
	public MuxInputConstantRule(String muxName, String pinName)
	{
		ConstraintBuilder cb = new ConstraintBuilder();

		String constName = pinName + "_CONSTANT";
		String wireName = pinName + Parts._WIRE;

		addLayout(constName, cb.left(muxName, 15).alignVertically(pinName));
		addLayout(wireName + ".0", cb.right(constName).alignVertically(pinName));
		addLayout(wireName + ".1", cb.align(pinName));
	}
}