package de.uni_hannover.sra.minimax_simulator.util.toposort;

import java.util.Set;

public interface TopologicalSortable<T>
{
	public Set<T> getDependencies();
}