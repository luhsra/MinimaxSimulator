package de.uni_hannover.sra.minimax_simulator.model.machine.base.memory;

import static com.google.common.base.Preconditions.*;

import java.util.Arrays;

public class PagedArrayMemory extends AbstractMemory
{
	private class PagedMemoryState implements MemoryState
	{
		private final int[][] _pages;

		PagedMemoryState()
		{
			_pages = new int[_pageCount][];
		}

		@Override
		public int getInt(int address)
		{
			int value = page(address)[address & _pageAddressMask];
			fireReadAccess(address, value);
			return value;
		}

		@Override
		public void setInt(int address, int value)
		{
			page(address)[address & _pageAddressMask] = value;
			fireWriteAccess(address, value);
		}

		@Override
		public void zero()
		{
			Arrays.fill(_pages, null);
			fireMemoryChanged();
		}

		int[] page(int addr)
		{
			int[] p = _pages[addr >>> _pageAddressWidth];
			if (p == null)
			{
				_pages[addr >>> _pageAddressWidth] = p = new int[_pageSize]; 
			}
			return p;
		}

		public MemoryState copy()
		{
			PagedMemoryState c = new PagedMemoryState();
			for (int i = 0; i < _pages.length; i++)
			{
				if (_pages[i] != null)
					c._pages[i] = Arrays.copyOf(_pages[i], _pageSize);
			}
			return c;
		}
	}

	private final int _pageCount;
	private final int _pageSize;

	private final int _pageAddressWidth;
	private final int _pageAddressMask;

	public PagedArrayMemory(int addressWidth, int pageAddressWidth)
	{
		super(addressWidth);
		checkArgument(pageAddressWidth <= addressWidth, "too big pages");

		_pageSize = 1 << pageAddressWidth;
		_pageCount = 1 << (addressWidth - pageAddressWidth);

		_pageAddressWidth = pageAddressWidth;
		_pageAddressMask = (1 << pageAddressWidth) - 1;

		setupMemoryState();
	}

	@Override
	protected MemoryState createMemoryState()
	{
		return new PagedMemoryState();
	}

	@Override
	protected MemoryState cloneState(MemoryState state)
	{
		return ((PagedMemoryState) state).copy();
	}
}