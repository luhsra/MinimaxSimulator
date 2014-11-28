package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.register.components;

import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.common.models.AbstractRegisterTableModel;

class RegisterExtensionsTable extends JTable
{
	private final static TextResource			res	= Application.getTextResource("machine");

	private final AbstractRegisterTableModel	_model;

	public RegisterExtensionsTable(AbstractRegisterTableModel model)
	{
		super();
		_model = model;
		setModel(_model);
		setTableHeader(null);
		setFillsViewportHeight(true);
		setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
			return res.get("register.description.none");
		return tip;
	}
}
