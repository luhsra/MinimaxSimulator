package de.uni_hannover.sra.minimax_simulator.ui.tabs.project.memory.model;

import static com.google.common.base.Preconditions.*;

import javax.swing.table.AbstractTableModel;

import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MemoryAccessListener;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.common.Disposable;
import de.uni_hannover.sra.minimax_simulator.util.Util;

@Deprecated
public class MemoryTableModel extends AbstractTableModel implements MemoryAccessListener,
		Disposable
{
	private final MachineMemory	_memory;
	private final int			_pageSize;
	private final int			_pageCount;

	private final String		_addressFormatString;
//	private final String		_addressFormatStringHexPrefix;
//	private final String		_valueFormatString	= "0x%08X";

	private int					_page;
	private int					_cachedPageStart;

	private final String[]		_colNames;

	public MemoryTableModel(MachineMemory memory)
	{
		this(memory, 16);
	}

	public MemoryTableModel(MachineMemory memory, int pageSize)
	{
		_memory = memory;
		_memory.addMemoryAccessListener(this);
		_pageSize = pageSize;

		// TODO: move to class MemorySize
		int addressRange = memory.getMaxAddress() - memory.getMinAddress();
		_pageCount = (addressRange - 1) / pageSize + 1;

		_addressFormatString = Util.createHexFormatString(memory.getAddressWidth(), false);
//		_addressFormatStringHexPrefix = "0x" + _addressFormatString;

		_page = _cachedPageStart = 0;

		TextResource res = Application.getTextResource("project");

		_colNames = new String[] { res.get("memtable.address"), res.get("memtable.dec"),
				res.get("memtable.hex") };
	}

	public MachineMemory getMemory()
	{
		return _memory;
	}

	public int getPage()
	{
		return _page;
	}

	public void setPage(int page)
	{
		checkArgument(page >= 0 && page < _pageCount);

		_page = page;

		_cachedPageStart = _page * _pageSize + _memory.getMinAddress();
		fireTableRowsUpdated(0, _pageSize);
	}

	public int rowToAddress(int row)
	{
		return _cachedPageStart + row;
	}

	public int addressToRow(int address)
	{
		return address - _cachedPageStart;
	}

	public int getPageCount()
	{
		return _pageCount;
	}

	public int getPageSize()
	{
		return _pageSize;
	}

	@Override
	public int getRowCount()
	{
		return _pageSize;
	}

	@Override
	public int getColumnCount()
	{
		return 3;
	}

	@Override
	public String getColumnName(int colIndex)
	{
		return _colNames[colIndex];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		int address = rowToAddress(rowIndex);
		if (address > _memory.getMaxAddress())
		{
			return "";
		}

		int value = _memory.getMemoryState().getInt(address);
		switch (columnIndex)
		{
			case 0:
				return String.format(_addressFormatString, address);
			case 1:
				return Integer.toString(value);
			case 2:
				return String.format("0x%08X", value);
			default:
				throw new AssertionError();
		}
	}
//
//	public String formatHexAddress(int address, boolean hexPrefix)
//	{
//		return String.format(hexPrefix ? _addressFormatStringHexPrefix
//				: _addressFormatString, address);
//	}
//
//	public String formatHexValue(int value)
//	{
//		return String.format(_valueFormatString, value);
//	}

	@Override
	public void memoryReadAccess(int address, int value)
	{
	}

	@Override
	public void memoryWriteAccess(int address, int value)
	{
		int row = addressToRow(address);
		if (row >= 0 && row < _pageSize)
			fireTableRowsUpdated(row, row);
	}

	@Override
	public void memoryReset()
	{
		fireTableRowsUpdated(0, _pageSize - 1);
	}

	@Override
	public void memoryChanged()
	{
		fireTableRowsUpdated(0, _pageSize - 1);
	}

	@Override
	public void dispose()
	{
		_memory.removeMemoryAccessListener(this);
	}
}