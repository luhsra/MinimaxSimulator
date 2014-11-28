package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.common.models;

import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.util.Util;

public class AluTableModel2 extends AbstractAluTableModel
{
	public AluTableModel2(TextResource aluDescriptionResource)
	{
		super(aluDescriptionResource);
	}

	@Override
	public int getColumnCount()
	{
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		switch (columnIndex)
		{
			case 0:
				return Util.toBinaryAddress(rowIndex, _aluOperations.size() - 1);
			case 1:
				return getOperationName(rowIndex);
			default:
				throw new IllegalArgumentException("Invalid col index in " + getClass().getSimpleName()
					+ ": " + columnIndex);
		}
	}

	@Override
	public void addOperation(AluOperation alu)
	{
		super.addOperation(alu);

		// binary operation codes of other entries may have changed
		fireTableRowsUpdated(0, _aluOperations.size() - 2);
	}

	@Override
	public void addOperation(int index, AluOperation alu)
	{
		super.addOperation(index, alu);

		// binary operation codes of other entries may have changed
		fireTableRowsUpdated(0, _aluOperations.size() - 1);
	}

	@Override
	public void removeOperation(AluOperation alu)
	{
		super.removeOperation(alu);

		// binary operation codes of other entries may have changed
		fireTableRowsUpdated(0, _aluOperations.size() - 1);
	}

	@Override
	public void removeOperation(int index)
	{
		super.removeOperation(index);

		// binary operation codes of other entries may have changed
		fireTableRowsUpdated(0, _aluOperations.size() - 1);
	}
}