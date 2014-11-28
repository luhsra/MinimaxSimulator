package de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;

public class JumpTargetColumn implements FocusableColumn
{
	@Override
	public boolean isHeaderNameLocalized()
	{
		return true;
	}

	@Override
	public String getHeaderName()
	{
		return "col.jumptarget";
	}

	@Override
	public Object getValue(int rowIndex, SignalRow row)
	{
		Jump j = row.getJump();
		int target0 = j.getTargetRow(rowIndex, 0);
		int target1 = j.getTargetRow(rowIndex, 1);
		if (target0 == target1)
		{
			return Integer.toString(target0);
		}
		else
		{
			return target1 + "\n" + target0;
		}
	}
}