package de.uni_hannover.sra.minimax_simulator.model.signal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import java.util.List;

/**
 * Default implementation of a {@link SignalType}.
 *
 * @author Martin L&uuml;ck
 */
public class DefaultSignalType implements SignalType {

	private final String						_id;
	private final String						_name;
	private final ImmutableList<SignalValue>	_values;
	private final int							_bitWidth;

	/**
	 * Constructs a new {@code DefaultSignalType} with the specified ID, name, value count and
	 * {@code allows don't care} property.
	 *
	 * @param id
	 *          the ID of the signal
	 * @param name
	 *          the name of the signal
	 * @param valueCount
	 *          the number of values
	 * @param allowsDontCare
	 *          whether the signal can be {@code don't care} or not
	 */
	public DefaultSignalType(String id, String name, int valueCount, boolean allowsDontCare) {
		_id = id;
		_name = name;
		_values = getValues(allowsDontCare, valueCount);

		int values = _values.size();
		if (values > 0 && _values.get(0).isDontCare())
			values--;

		if (values == 0)
			values = 1;

		int width = 32 - Integer.numberOfLeadingZeros(values - 1);
		if (width == 0)
			width = 1;

		_bitWidth = width;
	}

	/**
	 * Gets the {@link SignalValue}s of the {@code DefaultSignalType}.<br>
	 * If {@code allowsDontCare} is {@code true} the {@link SignalValue#DONT_CARE} will be included.
	 * The return will contain up to {@code valueCount} entries.
	 *
	 * @param allowsDontCare
	 *          whether the {@code SignalValue.DONT_CARE} should be included or not
	 * @param valueCount
	 *          the number of {@code SignalValue}s to return
	 * @return
	 *          a list of the {@code SignalValue}s
	 */
	private static ImmutableList<SignalValue> getValues(boolean allowsDontCare, int valueCount) {
		Builder<SignalValue> b = ImmutableList.builder();
		if (allowsDontCare || valueCount == 0) {
			b.add(SignalValue.DONT_CARE);
		}
		for (int v = 0; v < valueCount; v++) {
			b.add(SignalValue.valueOf(v));
		}
		return b.build();
	}

	@Override
	public String getId() {
		return _id;
	}

	@Override
	public final String getName() {
		return _name;
	}

	@Override
	public String toString() {
		return "Signal[" + _name + "]";
	}

	@Override
	public String getSignalName(int index) {
		return "";
	}

	@Override
	public String getSignalName(SignalValue value) {
		return getSignalName(value.intValue());
	}

	@Override
	public SignalValue getDefault() {
		return _values.get(0);
	}

	@Override
	public List<SignalValue> getValues() {
		return _values;
	}

	@Override
	public int getBitWidth() {
		return _bitWidth;
	}
}