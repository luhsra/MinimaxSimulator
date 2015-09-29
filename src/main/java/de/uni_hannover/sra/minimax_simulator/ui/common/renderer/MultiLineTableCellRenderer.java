/* source: http://blog.botunge.dk/post/2009/10/09/JTable-multiline-cell-renderer.aspx */
package de.uni_hannover.sra.minimax_simulator.ui.common.renderer;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.table.TableCellRenderer;

/**
 * Multiline Table Cell Renderer.
 */
@Deprecated
public class MultiLineTableCellRenderer extends JTextArea implements TableCellRenderer
{
	private List<List<Integer>>	_rowColHeight	= new ArrayList<List<Integer>>();
	private TableCellRenderer	_delegate;

	// Nimbus bug, see
	// http://stackoverflow.com/questions/12220413/background-of-multiline-cell-in-jtable
	private JViewport			_viewport;

	public MultiLineTableCellRenderer(TableCellRenderer delegate)
	{
		_delegate = delegate;
		_viewport = new JViewport();
		_viewport.add(this);

		//setLineWrap(true);
		//setWrapStyleWord(true);
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		// configure content
		if (value != null)
		{
			setText(value.toString());
		}
		else
		{
			setText("");
		}

		// get a configured component from the delegate
		JComponent comp = (JComponent) _delegate.getTableCellRendererComponent(table, value, isSelected,
			hasFocus, row, column);
		// configure myself as appropriate
		setBackground(comp.getBackground());
		setForeground(comp.getForeground());
		setBorder(comp.getBorder());
		adjustRowHeight(table, row, column);

		// return the viewport we are added to
		return _viewport;
	}

	/**
	 * Calculate the new preferred height for a given row, and sets the height on the table.
	 */
	private void adjustRowHeight(JTable table, int row, int column)
	{
		// The trick to get this to work properly is to set the width of the column to the
		// textarea. The reason for this is that getPreferredSize(), without a width tries
		// to place all the text in one line. By setting the size with the with of the column,
		// getPreferredSize() returnes the proper height which the row should have in
		// order to make room for the text.
		int cWidth = table.getTableHeader().getColumnModel().getColumn(column).getWidth();
		setSize(new Dimension(cWidth, 1000));
		int prefH = getPreferredSize().height;
		while (_rowColHeight.size() <= row)
		{
			_rowColHeight.add(new ArrayList<Integer>(column));
		}
		List<Integer> colHeights = _rowColHeight.get(row);
		while (colHeights.size() <= column)
		{
			colHeights.add(0);
		}
		colHeights.set(column, prefH);
		int maxH = prefH;
		for (Integer colHeight : colHeights)
		{
			if (colHeight > maxH)
			{
				maxH = colHeight;
			}
		}
		if (table.getRowHeight(row) != maxH)
		{
			table.setRowHeight(row, maxH);
		}
	}
}