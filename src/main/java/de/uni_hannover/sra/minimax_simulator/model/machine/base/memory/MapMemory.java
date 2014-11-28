package de.uni_hannover.sra.minimax_simulator.model.machine.base.memory;

import static com.google.common.base.Preconditions.*;

import java.util.HashMap;
import java.util.Map;

public class MapMemory extends AbstractMemory
{
	private final static int	DEFAULT_VALUE	= 0;

	private class MapMemoryState implements MemoryState
	{
		private final int					_maxAddress;
		private final Map<Integer, Integer>	_valueMap;

		MapMemoryState(int maxAddress)
		{
			_maxAddress = maxAddress;
			_valueMap = new HashMap<Integer, Integer>();
		}

		private MapMemoryState(int maxAddress, Map<Integer, Integer> valueMap)
		{
			_maxAddress = maxAddress;
			_valueMap = new HashMap<Integer, Integer>(valueMap);
		}

		@Override
		public int getInt(int address)
		{
			checkElementIndex(address, _maxAddress + 1);
			Integer entry = _valueMap.get(Integer.valueOf(address));
			if (entry == null)
				return DEFAULT_VALUE;

			int value = entry.intValue();
			fireReadAccess(address, value);
			return value;
		}

		@Override
		public void setInt(int address, int value)
		{
			checkElementIndex(address, _maxAddress + 1);
			if (value == DEFAULT_VALUE)
				_valueMap.remove(Integer.valueOf(address));
			else
				_valueMap.put(Integer.valueOf(address), Integer.valueOf(value));
			fireWriteAccess(address, value);
		}

		MapMemoryState copy()
		{
			return new MapMemoryState(_maxAddress, _valueMap);
		}

		@Override
		public void zero()
		{
			_valueMap.clear();
			fireMemoryChanged();
		}
	}

	public MapMemory(int addressWidth)
	{
		super(addressWidth);
		setupMemoryState();
	}

	@Override
	protected MemoryState createMemoryState()
	{
		return new MapMemoryState(getMaxAddress());
	}

	@Override
	protected MemoryState cloneState(MemoryState state)
	{
		return ((MapMemoryState) state).copy();
	}
}