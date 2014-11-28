package de.uni_hannover.sra.minimax_simulator.model.machine.base.memory;

public interface MachineMemory extends AddressRange
{
	public MemoryState getMemoryState();

//	public void pushMemoryState();
//
//	public void popMemoryState();

	public void markMemoryState();

	public void resetMemoryState();

	public boolean getNotifiesListeners();

	public void setNotifiesListeners(boolean notify);

	public void addMemoryAccessListener(MemoryAccessListener l);

	public void removeMemoryAccessListener(MemoryAccessListener l);
}