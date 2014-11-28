package de.uni_hannover.sra.minimax_simulator.model.machine.base.memory;

public interface AddressRange
{
	public int getMinAddress();

	public int getMaxAddress();

	public int getAddressWidth();
}