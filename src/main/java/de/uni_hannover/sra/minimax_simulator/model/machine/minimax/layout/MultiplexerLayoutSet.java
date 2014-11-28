package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.layout.constraint.ConstraintBuilder;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;

public class MultiplexerLayoutSet extends DefaultLayoutSet
{
	public MultiplexerLayoutSet(String muxName, boolean labelAtTop)
	{
		String portName = muxName + Parts._PORT;
		String labelName = muxName + Parts._LABEL;

		ConstraintBuilder cb = new ConstraintBuilder();
		cb.alignHorizontally(muxName);
		if (labelAtTop)
			cb.above(muxName, 15);
		else
			cb.below(muxName, 15);
		addLayout(portName, cb);

		
		cb.alignHorizontally(muxName);
		if (labelAtTop)
			cb.above(portName, 2);
		else
			cb.below(portName, 2);
		addLayout(labelName, cb);


		String selectWire = muxName + Parts._WIRE_SELECT;
		addLayout(selectWire + ".0", cb.align(portName));

		cb.alignHorizontally(muxName);
		if (labelAtTop)
			cb.above(muxName);
		else
			cb.below(muxName);
		addLayout(selectWire + ".1", cb);
	}
}
