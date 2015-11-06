package de.uni_hannover.sra.minimax_simulator.model.machine.base.memory;

import java.util.Arrays;

/**
 * Implementation of the {@link AbstractMemory} using an array to store values.
 *
 * @author Martin L&uuml;ck
 */
public class ArrayMemory extends AbstractMemory {

	/**
	 * The {@link MemoryState} for an {@link ArrayMemory}.
	 */
	private class ArrayMemoryState implements MemoryState {

		/** The array holding the values stored in memory. */
		private int[] values;

		/**
		 * Constructs a new {@code ArrayMemoryState} of the specified length.
		 *
		 * @param length
		 *          the length of the new {@code ArrayMemoryState}
		 */
		ArrayMemoryState(int length) {
			values = new int[length];
		}

		/**
		 * Constructs a new {@code ArrayMemoryState} with the specified array of values.
		 *
		 * @param values
		 *          the value array of the new {@code ArrayMemoryState}
		 */
		private ArrayMemoryState(int[] values) {
			this.values = values;
		}

		@Override
		public int getInt(int address) {
			int value = values[address];
			fireReadAccess(address, value);
			return value;
		}

		@Override
		public void setInt(int address, int value) {
			values[address] = value;
			fireWriteAccess(address, value);
		}

		/**
		 * Creates a copy of the {@code ArrayMemoryState}.
		 *
		 * @return
		 *          the copy
		 */
		ArrayMemoryState copy() {
			return new ArrayMemoryState(values);
		}

		@Override
		public void zero() {
			Arrays.fill(values, 0);
			fireMemoryChanged();
		}
	}

	/**
	 * Constructs a new {@code ArrayMemory} of the specified length.
	 *
	 * @param addressWidth
	 *          the length of the new {@code ArrayMemory}
	 */
	public ArrayMemory(int addressWidth) {
		super(addressWidth);
		setupMemoryState();
	}

	@Override
	protected MemoryState createMemoryState() {
		return new ArrayMemoryState(getMaxAddress() + 1);
	}

	@Override
	protected MemoryState cloneState(MemoryState state) {
		return ((ArrayMemoryState) state).copy();
	}
}