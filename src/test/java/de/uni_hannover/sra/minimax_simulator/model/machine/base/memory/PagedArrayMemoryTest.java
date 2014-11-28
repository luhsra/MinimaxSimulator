package de.uni_hannover.sra.minimax_simulator.model.machine.base.memory;

import static org.junit.Assert.*;

import org.junit.Test;

public class PagedArrayMemoryTest
{
	@Test
	public void checkPagedMemory()
	{
		PagedArrayMemory mem = new PagedArrayMemory(24, 12);

		MemoryState state = mem.getMemoryState();

		int val = state.getInt(1000);
		assertEquals(0, val);

		state.setInt(1000, 10001);
		val = state.getInt(1000);
		assertEquals(10001, val);

		state.setInt(4096, 2);
		val = state.getInt(4096);
		assertEquals(2, val);
	}
}