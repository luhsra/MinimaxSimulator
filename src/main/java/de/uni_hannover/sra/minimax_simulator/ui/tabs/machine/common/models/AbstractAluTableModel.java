package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.common.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;

public abstract class AbstractAluTableModel extends AbstractTableModel
{
	protected final List<AluOperation>	_aluOperations;

	private final TextResource _aluDescriptionResource;

	public AbstractAluTableModel(TextResource aluDescriptionResource)
	{
		_aluOperations = new ArrayList<AluOperation>();
		_aluDescriptionResource = aluDescriptionResource;
	}

	@Override
	public int getRowCount()
	{
		return _aluOperations.size();
	}

	public String getTooltip(int rowIndex)
	{
		AluOperation op = _aluOperations.get(rowIndex);
		return op.getDescription(_aluDescriptionResource);
	}

	public AluOperation get(int index)
	{
		return _aluOperations.get(index);
	}

	public int indexOf(AluOperation op)
	{
		return _aluOperations.indexOf(op);
	}

	protected String getOperationName(int rowIndex)
	{
		AluOperation operation = _aluOperations.get(rowIndex);
		return operation.getOperationName();
	}

	public void addOperation(AluOperation alu)
	{
		int idx = _aluOperations.size();
		_aluOperations.add(alu);
		fireTableRowsInserted(idx, idx);
	}

	public void addOperation(int index, AluOperation alu)
	{
		_aluOperations.add(index, alu);
		fireTableRowsInserted(index, index);
	}

	public void removeOperation(AluOperation alu)
	{
		int idx = _aluOperations.indexOf(alu);
		if (idx < 0)
			return;

		_aluOperations.remove(idx);
		fireTableRowsDeleted(idx, idx);
	}

	public void removeOperation(int index)
	{
		_aluOperations.remove(index);
		fireTableRowsDeleted(index, index);
	}

	public void setOperation(int index, AluOperation operation)
	{
		_aluOperations.set(index, operation);
		fireTableRowsUpdated(index, index);
	}
}