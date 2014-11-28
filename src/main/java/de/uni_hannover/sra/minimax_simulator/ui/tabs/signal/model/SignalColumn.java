package de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model;

import com.google.common.base.Strings;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalType;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalValue;

public class SignalColumn implements EditableColumn
{
	private final SignalTable	_table;
	private final SignalType	_signal;

	public SignalColumn(SignalTable table, SignalType signal)
	{
		_table = table;
		_signal = signal;
	}

	@Override
	public String getHeaderName()
	{
		return _signal.getName();
	}

	@Override
	public Object getValue(int rowIndex, SignalRow row)
	{
		return row.getSignal(_signal.getId(), _signal.getDefault());
	}

	private String binaryToString(int val)
	{
		return Strings.padStart(Integer.toBinaryString(val), _signal.getBitWidth(), '0');
	}

	public String getLongDescription(SignalValue value)
	{
		if (value.isDontCare())
			return "-";
		return binaryToString(value.intValue()) + " " + _signal.getSignalName(value);
	}

	public String getShortDescription(SignalValue value)
	{
		if (value.isDontCare())
			return "-";
		return binaryToString(value.intValue());
	}

	@Override
	public void setValue(int rowIndex, SignalRow row, Object value)
	{
		// check if value should be reset to the default value for this signal
		if (_signal.getDefault().equals(value))
			value = null;

		_table.setRowSignal(rowIndex, _signal.getId(), (SignalValue) value);
	}

	@Override
	public boolean isHeaderNameLocalized()
	{
		return false;
	}

	public SignalType getSignal()
	{
		return _signal;
	}
}