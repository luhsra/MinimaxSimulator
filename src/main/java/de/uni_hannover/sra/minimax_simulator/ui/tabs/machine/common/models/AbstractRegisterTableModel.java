package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.common.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;

public abstract class AbstractRegisterTableModel extends AbstractTableModel
{
	protected final List<RegisterExtension>	_registers;

	public AbstractRegisterTableModel()
	{
		_registers = new ArrayList<RegisterExtension>();
	}

	@Override
	public int getRowCount()
	{
		return _registers.size();
	}

	@Override
	public int getColumnCount()
	{
		return 2;
	}

	public String getTooltip(int rowIndex)
	{
		RegisterExtension op = _registers.get(rowIndex);
		if (!op.getDescription().contains("\n"))
			return op.getDescription();

		StringBuilder sb = new StringBuilder("<html>");
		sb.append(op.getDescription());
		int index = 0;
		while ((index = sb.indexOf("\n", index)) != -1)
			sb.replace(index, index + 1, "<br>");

		return sb.toString();
	}

	public RegisterExtension get(int index)
	{
		return _registers.get(index);
	}

	public int indexOf(RegisterExtension op)
	{
		return _registers.indexOf(op);
	}

	public int getByName(String name)
	{
		for (int i = 0; i < _registers.size(); i++)
			if (_registers.get(i).getName().equals(name))
				return i;
		return -1;
	}

	public void addRegister(RegisterExtension register)
	{
		int idx = _registers.size();
		_registers.add(register);
		fireTableRowsInserted(idx, idx);
	}

	public void removeRegister(RegisterExtension register)
	{
		int idx = _registers.indexOf(register);
		if (idx < 0)
			return;

		_registers.remove(idx);
		fireTableRowsDeleted(idx, idx);
	}

	public void removeRegister(int index)
	{
		_registers.remove(index);
		fireTableRowsDeleted(index, index);
	}

	public void setRegister(int index, RegisterExtension register)
	{
		_registers.set(index, register);
		fireTableRowsUpdated(index, index);
	}

	public void exchangeRegisters(int index1, int index2)
	{
		Collections.swap(_registers, index1, index2);
		if (index1 == index2 + 1)
		{
			fireTableRowsUpdated(index2, index1);
		}
		else if (index2 == index1 + 1)
		{
			fireTableRowsUpdated(index1, index2);
		}
		else
		{
			fireTableRowsUpdated(index1, index1);
			fireTableRowsUpdated(index2, index2);
		}
	}
}