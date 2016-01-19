package de.uni_hannover.sra.minimax_simulator.util.toposort;

import java.util.Set;

/**
 * A set of dependencies of a {@link TopologicalSortable}.
 *
 * @param <T>
 *          the class of the {@code TopologicalSortable}
 *
 * @author Martin L&uuml;ck
 */
public interface TopologicalDependencySets<T> {

    /**
     * Gets the dependencies of the specified {@code TopologicalSortable}.
     *
     * @param element
     *          the {@code TopologicalSortable}
     * @return
     *          a set of the dependencies of the {@code TopologicalSortable}
     */
    public Set<T> dependenciesOf(T element);
}