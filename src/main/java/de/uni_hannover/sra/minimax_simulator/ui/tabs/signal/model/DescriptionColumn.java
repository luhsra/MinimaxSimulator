package de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;

public class DescriptionColumn implements Column
{
	@Override
	public String getHeaderName()
	{
		return "col.description";
	}

	@Override
	public Object getValue(int rowIndex, SignalRow row)
	{
		return row.getDescription();
	}

	@Override
	public boolean isHeaderNameLocalized()
	{
		return true;
	}
}