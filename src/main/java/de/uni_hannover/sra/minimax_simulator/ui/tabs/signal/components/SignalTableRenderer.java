package de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.components;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalValue;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.SignalColumn;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.SignalTableModel;

@Deprecated
class SignalTableRenderer extends DefaultTableCellRenderer
{
	private final SignalTableModel _model;

	public SignalTableRenderer(SignalTableModel model)
	{
		_model = model;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		SignalColumn col = (SignalColumn) _model.getColumn(column);

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
			row, column);

		setText(col.getShortDescription((SignalValue) value));
		return this;
	}
}