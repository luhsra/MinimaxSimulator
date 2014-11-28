package de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.components;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class FlagIconCellRenderer extends DefaultTableCellRenderer
{
	private final Icon _icon;

	public FlagIconCellRenderer(Icon icon)
	{
		_icon = icon;
		setHorizontalAlignment(SwingConstants.RIGHT);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (Boolean.TRUE.equals(value))
		{
			setIcon(_icon);
		}
		else
		{
			setIcon(null);
		}
		setText(null);
		return this;
	}
}