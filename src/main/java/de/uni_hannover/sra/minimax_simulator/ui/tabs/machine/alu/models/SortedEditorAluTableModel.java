package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.alu.models;

import java.util.ListIterator;

import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.common.models.AluTableModel1;

public class SortedEditorAluTableModel extends AluTableModel1
{
	public SortedEditorAluTableModel(TextResource aluDescriptionResource)
	{
		super(aluDescriptionResource);
	}

	@Override
	public void addOperation(AluOperation alu)
	{
		// shortcut
		if (_aluOperations.isEmpty() || alu.ordinal() > _aluOperations.get(_aluOperations.size() - 1).ordinal())
		{
			_aluOperations.add(alu);
			fireTableRowsInserted(_aluOperations.size() - 1, _aluOperations.size() - 1);
			return;
		}

		// keep the table sorted by enumeration ID
		ListIterator<AluOperation> iter = _aluOperations.listIterator();
		int index = -1;
		while (iter.hasNext())
		{
			if (iter.next().ordinal() > alu.ordinal())
			{
				iter.previous();
				iter.add(alu);
				index = iter.nextIndex();
				break;
			}
		}

		// insert at the end if all elements are smaller
		if (index == -1)
		{
			index = _aluOperations.size();
			_aluOperations.add(alu);
		}
		fireTableRowsInserted(index, index);
	}
}