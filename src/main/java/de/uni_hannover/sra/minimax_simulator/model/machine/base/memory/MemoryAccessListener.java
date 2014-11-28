package de.uni_hannover.sra.minimax_simulator.model.machine.base.memory;

public interface MemoryAccessListener
{
	public void memoryReadAccess(int address, int value);

	public void memoryWriteAccess(int address, int value);

	public void memoryReset();

	public void memoryChanged();
}