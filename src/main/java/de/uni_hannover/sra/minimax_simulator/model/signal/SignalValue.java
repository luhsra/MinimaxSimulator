package de.uni_hannover.sra.minimax_simulator.model.signal;

import static com.google.common.base.Preconditions.*;

/**
 * This class represents a signal value, that is the value that a machine executing a signal row
 * containing this object should apply to a wire of the circuit. <br>
 * <br>
 * Those are mostly enable-switches and multiplexer controls and therefore the used signal values
 * are presumably in a low non-negative integer range. <br>
 * <br>
 * To avoid unnecessary object creation, and since a SignalValue contains almost only an integer as
 * its state, these instances are cached and can be obtained by the static factory method
 * {@link #valueOf(int)}. <br>
 * <br>
 * To mark that a signal value is not needed and therefore unspecified, the constant
 * {@link #DONT_CARE} is to be used.
 * 
 * @author Martin
 * 
 */
public final class SignalValue
{
	/**
	 * An unspecified signal. This object is the only instance whose method {@link #isDontCare()}
	 * returns <tt>true</tt>.
	 */
	public final static SignalValue		DONT_CARE	= new SignalValue();

	private final static SignalValue	cache[];
	static
	{
		cache = new SignalValue[20];
		for (int i = 0; i < cache.length; i++)
		{
			cache[i] = new SignalValue(i);
		}
	}

	private final int					_value;
	private final boolean				_isDontCare;

	private SignalValue()
	{
		_isDontCare = true;
		_value = 0;
	}

	private SignalValue(int value)
	{
		_isDontCare = false;
		_value = value;
	}

	/**
	 * Returns if this SignalValue is unspecified.<br>
	 * If this is the case, any arbitrary value can be returned by the {@link #intValue()} method.
	 */
	public boolean isDontCare()
	{
		return _isDontCare;
	}

	/**
	 * Returns the non-negative int value corresponding to this signal value. <br>
	 * If the signal is the {@link #DONT_CARE} signal, the return value of this method is
	 * unspecified.
	 */
	public int intValue()
	{
		return _value;
	}

	/**
	 * Retrieves a SignalValue for the given non-negative integer value.<br>
	 * The resulting signal value will not be equal to {@link #DONT_CARE}, but it will be equal to
	 * every other {@link SignalValue} constructed by this method with the same int parameter.
	 */
	public static SignalValue valueOf(int value)
	{
		checkArgument(value >= 0);

		if (value >= cache.length)
			return new SignalValue(value);
		return cache[value];
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (_isDontCare ? 1231 : 1237);
		result = prime * result + _value;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		SignalValue other = (SignalValue) obj;
		if (_isDontCare != other._isDontCare)
			return false;
		if (_value != other._value)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "SignalValue[" + (_isDontCare ? "DONT_CARE" : _value) + "]";
	}
}