package de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model;

import static com.google.common.base.Preconditions.*;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;

public class LabelColumn implements EditableColumn
{
	@Override
	public String getHeaderName()
	{
		return "col.label";
	}

	@Override
	public Object getValue(int rowIndex, SignalRow row)
	{
		return row.getLabel();
	}

	@Override
	public void setValue(int rowIndex, SignalRow row, Object value)
	{
		checkArgument(value instanceof String, "can only set a String as label");
		String label = (String) value;
		row.setLabel(label.isEmpty() ? null : label);
	}

	@Override
	public boolean isHeaderNameLocalized()
	{
		return true;
	}
}