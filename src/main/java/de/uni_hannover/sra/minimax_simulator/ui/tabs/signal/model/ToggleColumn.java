package de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;

public interface ToggleColumn extends Column
{
	public void toggle(int rowIndex, SignalRow row);
}
