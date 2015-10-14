package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.AttributeType;
import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.ConstraintBuilder;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;

public class DefaultRegisterLayoutSet extends DefaultLayoutSet
{
	public DefaultRegisterLayoutSet(String registerid)
	{
		String junctionId = registerid + Parts._JUNCTION;
		String portId = registerid + Parts._PORT;
		String labelId = registerid + Parts._LABEL;

		ConstraintBuilder cb = new ConstraintBuilder();

		addLayout(junctionId, cb.alignVertically(registerid).alignHorizontally(Parts.ALU_LINE));
		addLayout(portId, cb.relative(AttributeType.HORIZONTAL_CENTER, registerid, 20).above(registerid, 17));
		addLayout(labelId, cb.alignHorizontally(portId).above(portId, 3));

		String dataInWire = registerid + Parts._WIRE_DATA_IN;
		addLayout(dataInWire + ".0", cb.align(junctionId));
		addLayout(dataInWire + ".1", cb.alignVertically(registerid).right(registerid));

		String enabledWire = registerid + Parts._WIRE_ENABLED;
		addLayout(enabledWire + ".0", cb.align(portId));
		addLayout(enabledWire + ".1", cb.alignHorizontally(portId).above(registerid));

		String aluWire = registerid + Parts._JUNCTION + Parts._WIRE_DATA_IN;
		addLayout(aluWire + ".0", cb.right(Parts.ALU).relative(AttributeType.VERTICAL_CENTER, Parts.ALU, 4));
		addLayout(aluWire + ".1", cb.alignHorizontally(Parts.ALU_LINE).alignVertically(aluWire + ".0"));
		addLayout(aluWire + ".2", cb.align(junctionId));
	}
}