package de.uni_hannover.sra.minimax_simulator.util.toposort;

import java.util.Set;

/**
 * A {@code TopologicalSortable} is a class that can be sorted via a {@link TopologicalSorter}.
 *
 * @param <T>
 *          the class of the {@code TopologicalSortable}
 *
 * @author Martin L&uuml;ck
 */
public interface TopologicalSortable<T> {

	/**
	 * Gets the dependencies of the {@code TopologicalSortable}.
	 *
	 * @return
	 *          a set of the dependencies
	 */
	public Set<T> getDependencies();
}