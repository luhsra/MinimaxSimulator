package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.ConstraintBuilder;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;

public class MuxRegisterLayoutSet extends DefaultLayoutSet
{
	public MuxRegisterLayoutSet(String pinId, String registerId)
	{
		String junctionId = pinId + Parts._JUNCTION;
		String wireOutId = pinId + Parts._WIRE_DATA_OUT;
		String wireInId = pinId + Parts._WIRE_DATA_IN;
		String registerJunction = registerId + Parts._OUT_JUNCTION;

		ConstraintBuilder cb = new ConstraintBuilder();

		addLayout(junctionId, cb.alignVertically(pinId).alignHorizontally(registerJunction));

		addLayout(wireOutId + ".0", cb.align(junctionId));
		addLayout(wireOutId + ".1", cb.align(pinId));

		addLayout(wireInId + ".0", cb.align(registerJunction));
		addLayout(wireInId + ".1", cb.align(junctionId));
	}
}