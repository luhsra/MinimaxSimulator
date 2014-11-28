package de.uni_hannover.sra.minimax_simulator.util.toposort;

import java.util.Set;

public interface TopologicalDependencySets<T>
{
	public Set<T> dependenciesOf(T element);
}