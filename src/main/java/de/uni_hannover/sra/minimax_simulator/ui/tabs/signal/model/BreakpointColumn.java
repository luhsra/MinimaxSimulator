package de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;

public class BreakpointColumn implements ToggleColumn, FocusableColumn
{
	@Override
	public boolean isHeaderNameLocalized()
	{
		return true;
	}

	@Override
	public String getHeaderName()
	{
		return "col.breakpoint";
	}

	@Override
	public Object getValue(int rowIndex, SignalRow row)
	{
		return row.isBreakpoint();
	}

	@Override
	public void toggle(int rowIndex, SignalRow row)
	{
		row.setBreakpoint(!row.isBreakpoint());
	}
}