package de.uni_hannover.sra.minimax_simulator.util.toposort;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * "In computer science, a topological sort (sometimes abbreviated topsort or toposort) or
 * topological ordering of a directed graph is a linear ordering of its vertices such that for every
 * directed edge uv from vertex u to vertex v, u comes before v in the ordering. For instance, the
 * vertices of the graph may represent tasks to be performed, and the edges may represent
 * constraints that one task must be performed before another; in this application, a topological
 * ordering is just a valid sequence for the tasks. A topological ordering is possible if and only
 * if the graph has no directed cycles, that is, if it is a directed acyclic graph (DAG)."<br>
 * <br>
 * http://en.wikipedia.org/wiki/Topological_sorting<br>
 * <br>
 * Implementations of this class can compute a topological sorting on an arbitrary collection of
 * elements.<br>
 * For a topological sorting, there must exist a dependency relation between the elements which must
 * be supplied to the various {@code sort} methods.
 * 
 * @author Martin L&uuml;ck
 */
public interface TopologicalSorter {

	/**
	 * Sorts the collection of elements into a newly created list.<br>
	 * <br>
	 * The elements are ordered w.r.t. their natural topological ordering, represented by their
	 * {@link TopologicalSortable#getDependencies()} result.
	 *
	 * @param elements
	 *          the {@code TopologicalSortable}s to sort
	 * @param <T>
	 *          the class of the {@code TopologicalSortable}
	 * @return
	 *          a sorted list of the {@code TopologicalSortable}s
	 * @throws IllegalArgumentException
	 *             thrown if there are cyclic dependencies between some of the elements
	 * @throws IllegalArgumentException
	 *             thrown if the given collection is not closed w.r.t dependencies, that means if some
	 *             elements depend on others not contained in the collection
	 */
	public <T extends TopologicalSortable<T>> List<T> sort(Collection<T> elements);

	/**
	 * Sorts the collection of elements into the specified list, which is cleared before the insertion.<br>
	 * <br>
	 * The elements are ordered w.r.t. their natural topological ordering, represented by their
	 * {@link TopologicalSortable#getDependencies()} result.
	 *
	 * @param elements
	 *          the {@code TopologicalSortable}s to sort
	 * @param list
	 *          the list to hold the sorted order
	 * @param <T>
	 *          the class of the {@code TopologicalSortable}
	 * @throws IllegalArgumentException
	 *             thrown if there are cyclic dependencies between some of the elements
	 * @throws IllegalArgumentException
	 *             thrown if the given collection is not closed w.r.t dependencies, that means if some
	 *             elements depend on others not contained in the collection
	 */
	public <T extends TopologicalSortable<T>> void sort(Collection<T> elements, List<? super T> list);

	/**
	 * Sorts the collection of elements into a newly created list.<br>
	 * <br>
	 * The elements are ordered w.r.t. their dependencies provided by the
	 * {@link TopologicalDependencySets#dependenciesOf(Object)} method of the parameter.
	 *
	 * @param elements
	 *          the {@code TopologicalSortable}s to sort
	 * @param dependencySets
	 *          the {@code TopologicalDependencySets} for sorting
	 * @param <T>
	 *          the class of the {@code TopologicalSortable}
	 * @return
	 *          a sorted list of the {@code TopologicalSortable}s
	 * @throws IllegalArgumentException
	 *             thrown if there are cyclic dependencies between some of the elements
	 * @throws IllegalArgumentException
	 *             thrown if the given collection is not closed w.r.t dependencies, that means if some
	 *             elements depend on others not contained in the collection
	 */
	public <T> List<T> sort(Collection<T> elements, TopologicalDependencySets<T> dependencySets);

	/**
	 * Sorts the collection of elements into the specified list, which is cleared before the insertion.<br>
	 * <br>
	 * The elements are ordered w.r.t. their dependencies provided by the
	 * {@link TopologicalDependencySets#dependenciesOf(Object)} method of the parameter.
	 *
	 * @param elements
	 *          the {@code TopologicalSortable}s to sort
	 * @param dependencySets
	 *          the {@code TopologicalDependencySets} for sorting
	 * @param list
	 *          the list to hold the sorted order
	 * @param <T>
	 *          the class of the {@code TopologicalSortable}
	 * @throws IllegalArgumentException
	 *             thrown if there are cyclic dependencies between some of the elements
	 * @throws IllegalArgumentException
	 *             thrown if the given collection is not closed w.r.t dependencies, that means if some
	 *             elements depend on others not contained in the collection
	 */
	public <T> void sort(Collection<T> elements, TopologicalDependencySets<T> dependencySets, List<? super T> list);

	/**
	 * Sorts the collection of elements into a newly created list.<br>
	 * <br>
	 * The elements are ordered w.r.t. their dependencies provided by the
	 * {@link TopologicalDependencyRelation#dependsOn(Object, Object)} method which is queried for every pair
	 * of elements in the collection parameter of this method.
	 *
	 * @param elements
	 *          the {@code TopologicalSortable}s to sort
	 * @param dependencyRelation
	 *          the {@code TopologicalDependencyRelation} for sorting
	 * @param <T>
	 *          the class of the {@code TopologicalSortable}s
	 * @return
	 *          a sorted list of the {@code TopologicalSortable}s
	 * @throws IllegalArgumentException
	 *             thrown if there are cyclic dependencies between some of the elements
	 * @throws IllegalArgumentException
	 *             thrown if the given collection is not closed w.r.t dependencies, that means if some
	 *             elements depend on others not contained in the collection
	 */
	public <T> List<T> sort(Collection<T> elements, TopologicalDependencyRelation<T> dependencyRelation);

	/**
	 * Sorts the collection of elements into the specified list, which is cleared before the insertion.<br>
	 * <br>
	 * The elements are ordered w.r.t. their dependencies provided by the
	 * {@link TopologicalDependencyRelation#dependsOn(Object, Object)} method which is queried for every pair
	 * of elements in the collection parameter of this method.
	 *
	 * @param elements
	 *          the {@code TopologicalSortable}s to sort
	 * @param dependencyRelation
	 *          the {@code TopologicalDependencyRelation} for sorting
	 * @param list
	 *          the list to hold the sorted order
	 * @param <T>
	 *          the class of the {@code TopologicalSortable}
	 * @throws IllegalArgumentException
	 *             thrown if there are cyclic dependencies between some of the elements
	 * @throws IllegalArgumentException
	 *             thrown if the given collection is not closed w.r.t dependencies, that means if some
	 *             elements depend on others not contained in the collection
	 */
	public <T> void sort(Collection<T> elements, TopologicalDependencyRelation<T> dependencyRelation, List<? super T> list);

	/**
	 * Sorts the key set of the specified map into a newly created list.<br>
	 * <br>
	 * The elements are ordered w.r.t. their dependencies provided by their value, which is a Set of
	 * dependencies of the respective key.
	 *
	 * @param elementsAndDependencies
	 *          a map with the {@code TopologicalSortable}s to sort and a set of their dependencies
	 * @param <T>
	 *          the class of the {@code TopologicalSortable}
	 * @return
	 *          a sorted list of the {@code TopologicalSortable}s
	 * @throws IllegalArgumentException
	 *             thrown if there are cyclic dependencies between some of the elements
	 * @throws IllegalArgumentException
	 *             thrown if the given collection is not closed w.r.t dependencies, that means if some
	 *             elements depend on others not contained in the collection
	 */
	public <T> List<T> sort(Map<T, Set<T>> elementsAndDependencies);

	/**
	 * Sorts the key set of the specified map into the specified list, which is cleared before the
	 * insertion.<br>
	 * <br>
	 * The elements are ordered w.r.t. their dependencies provided by their value, which is a Set of
	 * dependencies of the respective key.
	 *
	 * @param elementsAndDependencies
	 *          a map with the {@code TopologicalSortable}s to sort and a set of their dependencies
	 * @param list
	 *          the list to hold the sorted order
	 * @param <T>
	 *          the class of the {@code TopologicalSortable}
	 * @throws IllegalArgumentException
	 *             thrown if there are cyclic dependencies between some of the elements
	 * @throws IllegalArgumentException
	 *             thrown if the given key set is not closed w.r.t dependencies, that means if some
	 *             elements depend on others not contained in the key set
	 */
	public <T> void sort(Map<T, Set<T>> elementsAndDependencies, List<? super T> list);
}