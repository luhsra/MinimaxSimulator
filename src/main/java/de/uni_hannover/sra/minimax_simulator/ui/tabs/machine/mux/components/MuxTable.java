package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.mux.components;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.mux.model.MuxTableModel;

@Deprecated
class MuxTable extends JTable
{
	private final MuxTableModel _model;

	public MuxTable(MuxTableModel model)
	{
		_model = model;
		setModel(_model);
		setTableHeader(null);
		setFillsViewportHeight(true);
		setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

		//getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		getColumnModel().getColumn(0).setPreferredWidth(40);
		getColumnModel().getColumn(0).setMinWidth(40);
//		getColumnModel().getColumn(1).setPreferredWidth(150);
//		getColumnModel().getColumn(1).setMinWidth(150);
//		getColumnModel().getColumn(2).setPreferredWidth(150);
//		getColumnModel().getColumn(2).setMinWidth(150);
	}
}