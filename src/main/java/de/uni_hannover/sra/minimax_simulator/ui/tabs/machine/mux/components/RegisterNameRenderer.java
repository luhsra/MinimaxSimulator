package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.mux.components;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.RegisterMuxInput;

@Deprecated
class RegisterNameRenderer extends JLabel implements ListCellRenderer
{
	public RegisterNameRenderer()
	{
		setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus)
	{
		if (isSelected)
		{
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		}
		else
		{
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		if (value == null)
			setText("");
		else
			setText(((RegisterMuxInput) value).getName());

		return this;
	}
}