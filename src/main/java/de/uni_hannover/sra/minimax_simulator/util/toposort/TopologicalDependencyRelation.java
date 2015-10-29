package de.uni_hannover.sra.minimax_simulator.util.toposort;

/**
 * A dependency relation between two {@link TopologicalSortable}s.
 *
 * @param <T>
 *          the class of the {@code TopologicalSortable}
 *
 * @author Martin L&uuml;ck
 */
public interface TopologicalDependencyRelation<T> {

	/**
	 * Checks if the specified {@link TopologicalSortable} depends on the other specified {@link TopologicalSortable}.
	 *
	 * @param element
	 *          the {@code TopologicalSortable} to check the dependency for
	 * @param dependency
	 *          the dependency {@code TopologicalSortable}
	 * @return
	 *          {@code true} if {@code element} depends on {@code dependency}, {@code false} otherwise
	 */
	public boolean dependsOn(T element, T dependency);
}