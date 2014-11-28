package de.uni_hannover.sra.minimax_simulator.util.toposort;

public interface TopologicalDependencyRelation<T>
{
	public boolean dependsOn(T element, T dependency);
}