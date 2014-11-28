package de.uni_hannover.sra.minimax_simulator.model.signal;

public interface DescriptionFactory
{
	public String createDescription(int rowIndex, SignalRow row);
}