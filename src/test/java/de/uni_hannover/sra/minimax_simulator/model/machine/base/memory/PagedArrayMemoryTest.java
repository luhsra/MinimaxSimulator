package de.uni_hannover.sra.minimax_simulator.model.machine.base.memory;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the implementation of a paged array memory.
 *
 * @see PagedArrayMemory
 *
 * @author Martin L&uuml;ck
 */
public class PagedArrayMemoryTest {

	/**
	 * Tests the paged array memory.
	 */
	@Test
	public void checkPagedMemory() {
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