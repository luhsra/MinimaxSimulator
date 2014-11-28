package de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.components;

import static com.google.common.base.Preconditions.*;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalValue;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.SignalColumn;

class SignalEditorRenderer extends JLabel implements ListCellRenderer
{
	private final SignalColumn _signalColumn;

	public SignalEditorRenderer(SignalColumn signalColumn)
	{
		_signalColumn = checkNotNull(signalColumn);
		
		setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index,
			boolean isSelected, boolean cellHasFocus)
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

		// No entry selected
		if (value == null)
		{
			setText("");
		}
		else
		{
			SignalValue signal = (SignalValue) value;
			setText(_signalColumn.getLongDescription(signal));
		}
		return this;
	}
}