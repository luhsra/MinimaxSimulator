package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.alu.components;

import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.common.models.AbstractAluTableModel;

class AluOperationTable extends JTable
{
	private final AbstractAluTableModel	_model;

	public AluOperationTable(AbstractAluTableModel model)
	{
		super();
		_model = model;
		setModel(_model);
		setTableHeader(null);
		setFillsViewportHeight(true);
		setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

		//getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		if (getColumnCount() == 2)
		{
			getColumnModel().getColumn(0).setPreferredWidth(60);
			getColumnModel().getColumn(0).setMinWidth(60);
			getColumnModel().getColumn(1).setPreferredWidth(150);
			getColumnModel().getColumn(1).setMinWidth(150);
		}
		else
		{
			getColumnModel().getColumn(0).setPreferredWidth(150);
			getColumnModel().getColumn(0).setMinWidth(150);
		}
	}

	@Override
	public String getToolTipText(MouseEvent m)
	{
		// TODO: no tooltip for empty string
		int row = rowAtPoint(m.getPoint());
		if (row == -1)
			return null;
		String tip = _model.getTooltip(row);
		if (tip == null || tip.isEmpty())
			return null;
		return tip;
	}
}
