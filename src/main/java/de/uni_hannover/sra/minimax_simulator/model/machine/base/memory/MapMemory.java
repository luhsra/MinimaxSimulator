package de.uni_hannover.sra.minimax_simulator.model.machine.base.memory;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkElementIndex;

/**
 * Implementation of the {@link AbstractMemory} using a map to store values.
 *
 * @author Martin L&uuml;ck
 */
public class MapMemory extends AbstractMemory {

	private final static int	DEFAULT_VALUE	= 0;

	/**
	 * The {@link MemoryState} of a {@link MapMemory}.
	 */
	private class MapMemoryState implements MemoryState {

		private final int					_maxAddress;
		/** The map holding the values stored in memory. */
		private final Map<Integer, Integer>	_valueMap;

		/**
		 * Constructs a new {@code MapMemoryState} with the specified maximum address.
		 *
		 * @param maxAddress
		 *          the highest accessible address
		 */
		MapMemoryState(int maxAddress) {
			_maxAddress = maxAddress;
			_valueMap = new HashMap<Integer, Integer>();
		}

		/**
		 * Constructs a new {@code MapMemoryState} with the specified maximum address
		 * and map of values.
		 *
		 * @param maxAddress
		 *          the highest accessible address
		 * @param valueMap
		 *          the value map of the new {@code MapMemoryState}
		 */
		private MapMemoryState(int maxAddress, Map<Integer, Integer> valueMap) {
			_maxAddress = maxAddress;
			_valueMap = new HashMap<Integer, Integer>(valueMap);
		}

		@Override
		public int getInt(int address) {
			checkElementIndex(address, _maxAddress + 1);
			Integer entry = _valueMap.get(Integer.valueOf(address));
			if (entry == null) {
				return DEFAULT_VALUE;
			}

			int value = entry.intValue();
			fireReadAccess(address, value);
			return value;
		}

		@Override
		public void setInt(int address, int value) {
			checkElementIndex(address, _maxAddress + 1);
			if (value == DEFAULT_VALUE) {
				_valueMap.remove(Integer.valueOf(address));
			}
			else {
				_valueMap.put(Integer.valueOf(address), Integer.valueOf(value));
			}
			fireWriteAccess(address, value);
		}

		/**
		 * Creates a copy of the {@code MapMemoryState}.
		 *
		 * @return
		 *          the copy
		 */
		MapMemoryState copy() {
			return new MapMemoryState(_maxAddress, _valueMap);
		}

		@Override
		public void zero() {
			_valueMap.clear();
			fireMemoryChanged();
		}
	}

	/**
	 * Constructs a new {@code MapMemory} of the specified length.
	 *
	 * @param addressWidth
	 *          the length of the new {@code MapMemory}
	 */
	public MapMemory(int addressWidth) {
		super(addressWidth);
		setupMemoryState();
	}

	@Override
	protected MemoryState createMemoryState() {
		return new MapMemoryState(getMaxAddress());
	}

	@Override
	protected MemoryState cloneState(MemoryState state) {
		return ((MapMemoryState) state).copy();
	}
}