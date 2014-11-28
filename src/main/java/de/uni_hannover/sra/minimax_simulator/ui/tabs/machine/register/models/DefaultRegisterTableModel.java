package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.register.models;

import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.common.models.AbstractRegisterTableModel;

public class DefaultRegisterTableModel extends AbstractRegisterTableModel
{
	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		RegisterExtension reg = _registers.get(rowIndex);
		switch (columnIndex)
		{
			case 0:
				return reg.getName();
			case 1:
				return reg.getSize().getName();
			default:
				throw new IllegalArgumentException("Invalid col index in " + getClass().getSimpleName()
					+ ": " + columnIndex);
		}
	}
}