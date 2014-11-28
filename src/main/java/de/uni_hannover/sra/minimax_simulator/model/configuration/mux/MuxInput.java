package de.uni_hannover.sra.minimax_simulator.model.configuration.mux;

public interface MuxInput
{
	public String getName();

	@Override
	public boolean equals(Object o);

	@Override
	public int hashCode();
}