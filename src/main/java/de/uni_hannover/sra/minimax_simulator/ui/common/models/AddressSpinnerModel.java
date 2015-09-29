package de.uni_hannover.sra.minimax_simulator.ui.common.models;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.AddressRange;

@Deprecated
public class AddressSpinnerModel extends WrapSpinnerModel
{
	public AddressSpinnerModel(AddressRange range)
	{
		super(range.getMinAddress(), range.getMinAddress(), range.getMaxAddress());
	}
}