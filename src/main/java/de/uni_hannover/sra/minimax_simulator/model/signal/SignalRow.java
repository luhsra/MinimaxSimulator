package de.uni_hannover.sra.minimax_simulator.model.signal;

import de.uni_hannover.sra.minimax_simulator.model.signal.jump.DefaultJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public final class SignalRow
{
	private final Map<String, SignalValue>	_values;

	private String							_label;
	private boolean							_isBreakpoint;
	private Jump							_jump;

	private String							_description;

	public SignalRow()
	{
		_values = new HashMap<String, SignalValue>();
		_label = null;
		_isBreakpoint = false;
		_jump = DefaultJump.INSTANCE;
	}

	public String getLabel()
	{
		return _label;
	}

	public void setLabel(String label)
	{
		_label = label;
	}

	public Map<String, SignalValue> getSignalValues()
	{
		return Collections.unmodifiableMap(_values);
	}

	public int getSignalValue(String signal)
	{
		SignalValue val = _values.get(signal);
		if (val == null)
			return 0;
		return val.intValue();
	}

	public SignalValue getSignal(String signal, SignalValue dflt)
	{
		SignalValue val = _values.get(signal);
		if (val == null)
			return dflt;
		return val;
	}

	public SignalValue getSignal(SignalType signalType)
	{
		return getSignal(signalType.getId(), signalType.getDefault());
	}

	public void setSignalValue(String signal, int value)
	{
		_values.put(signal, SignalValue.valueOf(value));
	}

	public void setSignal(String signal, SignalValue value)
	{
		if (value == null)
			_values.remove(signal);
		else
			_values.put(signal, value);
	}

	public void setSignal(SignalType signalType, SignalValue value)
	{
		setSignal(signalType.getId(), value);
	}

	public void resetSignal(String signal)
	{
		_values.remove(signal);
	}

	public boolean isGotoOp()
	{
		return false;
	}

	public Jump getJump()
	{
		return _jump;
	}

	public void setJump(Jump jump)
	{
		_jump = checkNotNull(jump);
	}

	public boolean isBreakpoint()
	{
		return _isBreakpoint;
	}

	public void setBreakpoint(boolean isBreakpoint)
	{
		_isBreakpoint = isBreakpoint;
	}

	public String getDescription()
	{
		return _description;
	}

	public void setDescription(String description)
	{
		_description = description;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		if (_isBreakpoint)
			sb.append('*');
		sb.append('<');
		if (_label != null)
			sb.append(_label);
		sb.append(">: ");
		sb.append(_values.toString());
		return sb.toString();
	}
}