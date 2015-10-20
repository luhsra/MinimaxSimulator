package de.uni_hannover.sra.minimax_simulator.model.machine.base.memory;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Implementation of the {@link AbstractMemory} using a paged array (two-dimensional array) to store values.
 *
 * @author Martin L&uuml;ck
 */
public class PagedArrayMemory extends AbstractMemory {

	/**
	 * The {@link MemoryState} of a {@link PagedArrayMemory}.
	 */
	private class PagedMemoryState implements MemoryState {

		/** The paged array holding the values stored in memory. */
		private final int[][] _pages;

		/**
		 * Constructs a new {@code PagedMemoryState} with {@link PagedArrayMemory#_pageCount} pages.
		 */
		PagedMemoryState() {
			_pages = new int[_pageCount][];
		}

		@Override
		public int getInt(int address) {
			int value = page(address)[address & _pageAddressMask];
			fireReadAccess(address, value);
			return value;
		}

		@Override
		public void setInt(int address, int value) {
			page(address)[address & _pageAddressMask] = value;
			fireWriteAccess(address, value);
		}

		@Override
		public void zero() {
			Arrays.fill(_pages, null);
			fireMemoryChanged();
		}

		/**
		 * Gets the entire page the specified address belongs to.
		 *
		 * @param addr
		 *          the address
		 * @return
		 *          the page the address belongs to
		 */
		int[] page(int addr) {
			int[] p = _pages[addr >>> _pageAddressWidth];
			if (p == null) {
				_pages[addr >>> _pageAddressWidth] = p = new int[_pageSize]; 
			}
			return p;
		}

		/**
		 * Creates a copy of the {@code PagedMemoryState}.
		 *
		 * @return
		 *          the copy
		 */
		public MemoryState copy() {
			PagedMemoryState c = new PagedMemoryState();
			for (int i = 0; i < _pages.length; i++) {
				if (_pages[i] != null) {
					c._pages[i] = Arrays.copyOf(_pages[i], _pageSize);
				}
			}
			return c;
		}
	}

	private final int _pageCount;
	private final int _pageSize;

	private final int _pageAddressWidth;
	private final int _pageAddressMask;

	/**
	 * Constructs a new {@code PagedArrayMemory} of the specified length with the
	 * specified page size.
	 *
	 * @param addressWidth
	 *          the length of the new {@code PagedArrayMemory}
	 * @param pageAddressWidth
	 *          the length of a page
	 */
	public PagedArrayMemory(int addressWidth, int pageAddressWidth) {
		super(addressWidth);
		checkArgument(pageAddressWidth <= addressWidth, "too big pages");

		_pageSize = 1 << pageAddressWidth;
		_pageCount = 1 << (addressWidth - pageAddressWidth);

		_pageAddressWidth = pageAddressWidth;
		_pageAddressMask = (1 << pageAddressWidth) - 1;

		setupMemoryState();
	}

	@Override
	protected MemoryState createMemoryState() {
		return new PagedMemoryState();
	}

	@Override
	protected MemoryState cloneState(MemoryState state) {
		return ((PagedMemoryState) state).copy();
	}
}