package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.ConstraintBuilder;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;

public class ExtendedRegisterLayoutSet extends DefaultRegisterLayoutSet
{
	public ExtendedRegisterLayoutSet(String registerId)
	{
		super(registerId);

		String outJunctionId = registerId + Parts._OUT_JUNCTION;
		String anchorId = registerId + Parts._OUT_JUNCTION + Parts._ANCHOR;

		ConstraintBuilder cb = new ConstraintBuilder();

		addLayout(outJunctionId, cb.alignHorizontally(anchorId).alignVertically(registerId));

		String dataOutWire = registerId + Parts._WIRE_DATA_OUT;
		addLayout(dataOutWire + ".0", cb.left(registerId).alignVertically(registerId));
		addLayout(dataOutWire + ".1", cb.align(outJunctionId));
	}
}