package de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;

public class JumpLabelSelector extends JComboBox
{
	private static class Target
	{
		public int		row;
		public String	label;

		public Target(int row, String label)
		{
			this.row = row;
			this.label = label;
		}

		@Override
		public String toString()
		{
			return label + " (" + row + ")";
		}
	}

	private class ChangeListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Target selected = (Target) getSelectedItem();
			if (selected == null)
			{
				_rowField.setText("");
			}
			else
			{
				_rowField.setText(Integer.toString(selected.row));
			}
		}
	}

	private class TargetListModel extends AbstractListModel implements ComboBoxModel
	{
		private Target	_selected;

		@Override
		public int getSize()
		{
			return _targets.size();
		}

		@Override
		public Object getElementAt(int index)
		{
			return _targets.get(index);
		}

		@Override
		public void setSelectedItem(Object anItem)
		{
			_selected = (Target) anItem;
		}

		@Override
		public Object getSelectedItem()
		{
			return _selected;
		}
	}

	private final JTextField	_rowField;
	private final List<Target>	_targets;

	public JumpLabelSelector(JTextField rowField, SignalTable signalTable, int currentTargetRow)
	{
		_rowField = rowField;

		// int estLabelCount = signalTable.getRowCount() >> 2;

		Target selected = null;

		Builder<Target> b = ImmutableList.builder();
		for (int i = 0, n = signalTable.getRowCount(); i < n; i++)
		{
			SignalRow row = signalTable.getRow(i);
			if (row.getLabel() != null)
			{
				Target t = new Target(i, row.getLabel());
				b.add(t);
				if (i == currentTargetRow)
					selected = t;
			}
		}
		_targets = b.build();

		setModel(new TargetListModel());
		addActionListener(new ChangeListener());
		getModel().setSelectedItem(selected);
	}
}