package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.common.models;

import de.uni_hannover.sra.minimax_simulator.resources.TextResource;

public class AluTableModel1 extends AbstractAluTableModel
{
	public AluTableModel1(TextResource aluDescriptionResource)
	{
		super(aluDescriptionResource);
	}

	@Override
	public int getColumnCount()
	{
		return 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		switch (columnIndex)
		{
			case 0:
				return getOperationName(rowIndex);
			default:
				throw new IllegalArgumentException("Invalid col index in " + getClass().getSimpleName()
					+ ": " + columnIndex);
		}
	}
}