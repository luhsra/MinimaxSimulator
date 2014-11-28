package de.uni_hannover.sra.minimax_simulator.ui.common;

import java.awt.event.*;
import javax.swing.*;

public class EditableCellFocusAction extends WrappedAction
{
	private JTable	table;

	/* Specify the component and KeyStroke for the Action we want to wrap */
	public EditableCellFocusAction(JTable table, KeyStroke keyStroke)
	{
		super(table, keyStroke);
		this.table = table;
	}

	protected boolean shouldFocus(int row, int column)
	{
		return table.isCellEditable(row, column);
	}

	/* Provide the custom behaviour of the Action */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		int originalRow = table.getSelectedRow();
		int originalColumn = table.getSelectedColumn();

		invokeOriginalAction(e);

		int row = table.getSelectedRow();
		int column = table.getSelectedColumn();

		// Keep invoking the original action until we find an editable cell

		while (!shouldFocus(row, column))
		{
			invokeOriginalAction(e);

			// We didn't move anywhere, reset cell selection and get out.

			if (row == table.getSelectedRow() && column == table.getSelectedColumn())
			{
				table.changeSelection(originalRow, originalColumn, false, false);
				break;
			}

			row = table.getSelectedRow();
			column = table.getSelectedColumn();

			// Back to where we started, get out.

			if (row == originalRow && column == originalColumn)
			{
				break;
			}
		}
	}
}