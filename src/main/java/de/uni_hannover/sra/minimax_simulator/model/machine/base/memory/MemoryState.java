package de.uni_hannover.sra.minimax_simulator.model.machine.base.memory;

public interface MemoryState
{
	//public int[] getInts(int fromAddress, int length);

	public int getInt(int address);

	public void setInt(int address, int value);

	public void zero();
}