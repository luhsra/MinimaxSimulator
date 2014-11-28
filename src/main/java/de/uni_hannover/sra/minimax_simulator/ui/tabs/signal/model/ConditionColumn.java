package de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;

public class ConditionColumn implements Column
{
	@Override
	public boolean isHeaderNameLocalized()
	{
		return true;
	}

	@Override
	public String getHeaderName()
	{
		return "col.condition";
	}

	@Override
	public Object getValue(int rowIndex, SignalRow row)
	{
		Jump j = row.getJump();
		if (j.getTargetRow(rowIndex, 0) == j.getTargetRow(rowIndex, 1))
		{
			return "-";
		}
		else
		{
			return "1\n0";
		}
	}
}