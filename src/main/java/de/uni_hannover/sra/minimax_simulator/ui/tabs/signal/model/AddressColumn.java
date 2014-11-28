package de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;

public class AddressColumn implements Column
{
	@Override
	public String getHeaderName()
	{
		return "col.address";
	}

	@Override
	public Object getValue(int rowIndex, SignalRow row)
	{
		return rowIndex;
	}

	@Override
	public boolean isHeaderNameLocalized()
	{
		return true;
	}
}