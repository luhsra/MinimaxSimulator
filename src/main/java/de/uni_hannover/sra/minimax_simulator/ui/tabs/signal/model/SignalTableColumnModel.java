package de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.components.SignalTableHeaderRenderer;

public class SignalTableColumnModel extends DefaultTableColumnModel
{
	public SignalTableColumnModel()
	{
	}

	@Override
	public void addColumn(TableColumn col)
	{
		col.setHeaderRenderer(new SignalTableHeaderRenderer());
		super.addColumn(col);
	}
}