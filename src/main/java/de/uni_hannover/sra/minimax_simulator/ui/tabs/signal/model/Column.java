package de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;

public interface Column
{
	public boolean isHeaderNameLocalized();

	public String getHeaderName();

	public Object getValue(int rowIndex, SignalRow row);
}