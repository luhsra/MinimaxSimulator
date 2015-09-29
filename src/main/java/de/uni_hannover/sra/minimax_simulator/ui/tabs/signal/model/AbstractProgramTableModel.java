package de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;

@Deprecated
public abstract class AbstractProgramTableModel extends AbstractTableModel
{
	protected abstract List<Column> createColumns();

	private final TextResource	_columnResource;

	private final SignalTable	_signalTable;

	private List<Column>		_columns	= null;
	private String[]			_colNames;

	public AbstractProgramTableModel(TextResource columnResource, SignalTable signalTable)
	{
		_columnResource = columnResource;
		_signalTable = signalTable;
	}

	private void rebuildColumns()
	{
		_columns = createColumns();
		_colNames = new String[_columns.size()];
		int i = 0;
		for (Column col : _columns)
		{
			_colNames[i] = col.isHeaderNameLocalized() ? _columnResource.get(col.getHeaderName())
					: col.getHeaderName();
			i++;
		}
	}

	protected void updateColumns()
	{
		rebuildColumns();
		fireTableStructureChanged();
	}

	public Column getColumn(int columnIndex)
	{
		return _columns.get(columnIndex);
	}

	@Override
	public int getColumnCount()
	{
		return _columns.size();
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		return _colNames[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return _columns.get(columnIndex).getClass();
	}

	@Override
	public int getRowCount()
	{
		return _signalTable.getRowCount();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Column col = _columns.get(columnIndex);
		SignalRow row = _signalTable.getRow(rowIndex);
		return col.getValue(rowIndex, row);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false;
	}

	public boolean toggleCell(int rowIndex, int columnIndex)
	{
		Column col = _columns.get(columnIndex);
		if (col instanceof ToggleColumn)
		{
			SignalRow row = _signalTable.getRow(rowIndex);
			((ToggleColumn) col).toggle(rowIndex, row);
			fireTableCellUpdated(rowIndex, columnIndex);
			return true;
		}
		return false;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		Column col = _columns.get(columnIndex);
		if (!(col instanceof EditableColumn))
			throw new IllegalArgumentException("Column not editable: "
				+ getColumnName(columnIndex));

		SignalRow row = _signalTable.getRow(rowIndex);
		((EditableColumn) col).setValue(rowIndex, row, aValue);
		fireTableCellUpdated(rowIndex, columnIndex);
	}
}