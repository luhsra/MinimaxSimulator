package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.alu.components;

import java.awt.event.MouseEvent;

import javax.swing.JTable;

import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.ui.common.DoubleClickListener;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.common.models.AbstractAluTableModel;

abstract class AluOperationDoubleClickListener extends DoubleClickListener
{
	private final JTable				_table;
	private final AbstractAluTableModel	_model;

	public AluOperationDoubleClickListener(JTable table, AbstractAluTableModel model)
	{
		_table = table;
		_model = model;
	}

	public abstract void aluDblClicked(AluOperation operation, int index);

	@Override
	public void doubleClicked(MouseEvent e)
	{
		int row = _table.rowAtPoint(e.getPoint());
		if (row == -1)
			return;

		AluOperation operation = _model.get(row);
		if (operation == null)
			return;

		aluDblClicked(operation, row);
	}
}