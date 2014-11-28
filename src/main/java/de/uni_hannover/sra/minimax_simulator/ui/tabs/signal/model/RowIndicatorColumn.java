package de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;

public abstract class RowIndicatorColumn implements Column
{
	protected abstract int getCurrentRow();

	@Override
	public boolean isHeaderNameLocalized()
	{
		return false;
	}

	@Override
	public String getHeaderName()
	{
		return "";
	}

	@Override
	public Object getValue(int rowIndex, SignalRow row)
	{
		int currentRow = getCurrentRow();
		return currentRow >= 0 && rowIndex == currentRow;
	}
}