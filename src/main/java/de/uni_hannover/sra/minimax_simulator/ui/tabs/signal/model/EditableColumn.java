package de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;

public interface EditableColumn extends FocusableColumn
{
	public void setValue(int rowIndex, SignalRow row, Object value);
}