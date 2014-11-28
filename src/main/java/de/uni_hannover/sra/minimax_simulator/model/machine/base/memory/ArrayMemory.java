package de.uni_hannover.sra.minimax_simulator.model.machine.base.memory;

import java.util.Arrays;

public class ArrayMemory extends AbstractMemory
{
	private class ArrayMemoryState implements MemoryState
	{
		private int[] _values;

		ArrayMemoryState(int length)
		{
			_values = new int[length];
		}

		private ArrayMemoryState(int[] values)
		{
			_values = values;
		}

		@Override
		public int getInt(int address)
		{
			int value = _values[address];
			fireReadAccess(address, value);
			return value;
		}

		@Override
		public void setInt(int address, int value)
		{
			_values[address] = value;
			fireWriteAccess(address, value);
		}

		ArrayMemoryState copy()
		{
			return new ArrayMemoryState(_values);
		}

		@Override
		public void zero()
		{
			Arrays.fill(_values, 0);
			fireMemoryChanged();
		}
	}

	public ArrayMemory(int addressWidth)
	{
		super(addressWidth);
		setupMemoryState();
	}

	@Override
	protected MemoryState createMemoryState()
	{
		return new ArrayMemoryState(getMaxAddress() + 1);
	}

	@Override
	protected MemoryState cloneState(MemoryState state)
	{
		return ((ArrayMemoryState) state).copy();
	}
}