package de.uni_hannover.sra.minimax_simulator.util.toposort;

import java.util.*;
import java.util.Map.Entry;

/**
 * Simple implementation of {@link TopologicalSorter}.
 *
 * @see TopologicalSorter
 * 
 * @author Martin L&uuml;ck
 */
public class SimpleTopologicalSorter implements TopologicalSorter {

    @Override
    public <T extends TopologicalSortable<T>> List<T> sort(Collection<T> elements) {
        return sort(elements, element -> element.getDependencies());
    }

    @Override
    public <T> List<T> sort(Collection<T> elements, TopologicalDependencySets<T> dependencyRelation) {
        Map<T, Set<T>> dependencies = new HashMap<>();

        for (T element : elements) {
            dependencies.put(element, dependencyRelation.dependenciesOf(element));
        }

        return resolveDependencyMap(dependencies);
    }

    @Override
    public <T> List<T> sort(Collection<T> elements, TopologicalDependencyRelation<T> dependencyRelation) {
        Map<T, Set<T>> dependencies = new HashMap<>();

        for (T element1 : elements) {
            HashSet<T> depsOfElement1 = new HashSet<>();
            dependencies.put(element1, depsOfElement1);
            for (T element2 : elements) {
                if (dependencyRelation.dependsOn(element1, element2)) {
                    depsOfElement1.add(element2);
                }
            }
        }

        return resolveDependencyMap(dependencies);
    }

    @Override
    public <T> List<T> sort(Map<T, Set<T>> elementsAndDependencies) {
        return resolveDependencyMap(new HashMap<>(elementsAndDependencies));
    }

    @Override
    public <T extends TopologicalSortable<T>> void sort(Collection<T> elements, List<? super T> list) {
        sort(elements, element -> element.getDependencies(), list);
    }

    @Override
    public <T> void sort(Collection<T> elements, TopologicalDependencySets<T> dependencySets, List<? super T> list) {
        Map<T, Set<T>> dependencies = new HashMap<>();

        for (T element : elements) {
            dependencies.put(element, dependencySets.dependenciesOf(element));
        }

        resolveDependencyMap(dependencies, list);
    }

    @Override
    public <T> void sort(Collection<T> elements, TopologicalDependencyRelation<T> dependencyRelation, List<? super T> list) {
        Map<T, Set<T>> dependencies = new HashMap<>();

        for (T element1 : elements) {
            HashSet<T> depsOfElement1 = new HashSet<>();
            dependencies.put(element1, depsOfElement1);
            for (T element2 : elements) {
                if (dependencyRelation.dependsOn(element1, element2)) {
                    depsOfElement1.add(element2);
                }
            }
        }

        resolveDependencyMap(dependencies, list);
    }

    @Override
    public <T> void sort(Map<T, Set<T>> elementsAndDependencies, List<? super T> list) {
        resolveDependencyMap(new HashMap<>(elementsAndDependencies), list);
    }

    /**
     * Resolves the specified dependency map into the specified list.
     *
     * @param dependencies
     *          a map of the {@code TopologicalSortable}s and their set of dependencies
     * @param list
     *          the list to hold the resolved dependencies
     * @param <T>
     *          the class of the {@code TopologicalSortable}
     */
    private static  <T> void resolveDependencyMap(Map<T, Set<T>> dependencies, List<? super T> list) {
        Set<T> resolved = new HashSet<>();
        if (!list.isEmpty()) {
            list.clear();
        }

        while (!dependencies.isEmpty()) {
            for (Iterator<Entry<T, Set<T>>> iter = dependencies.entrySet().iterator(); iter.hasNext();) {
                Entry<T, Set<T>> entry = iter.next();
                if (entry.getValue().isEmpty()) {
                    // attribute is resolved, it has no further dependencies
                    iter.remove();
                    resolved.add(entry.getKey());
                    list.add(entry.getKey());
                }
            }

            // remove resolved attributes from all remaining dependencies
            for (Set<T> depSet : dependencies.values()) {
                depSet.removeAll(resolved);
            }

            if (resolved.isEmpty()) {
                for (Entry<T, Set<T>> entry : dependencies.entrySet()) {
                    for (T element : entry.getValue()) {
                        if (!dependencies.keySet().contains(element)) {
                            throw new IllegalArgumentException(entry.getKey() + " depends on " + element + " but no dependencies are given for " + element);
                        }
                    }
                }

                throw new IllegalArgumentException("Cannot resolve dependencies, " + "there is a cyclic dependency in: " + dependencies.keySet());
            }

            resolved.clear();
        }
    }

    /**
     * Resolves the specified dependency map into the newly created list.
     *
     * @param dependencies
     *          a map of the {@code TopologicalSortable}s and their set of dependencies
     * @param <T>
     *          the class of the {@code TopologicalSortable}
     * @return
     *          a list of the resolved dependencies
     */
    private static <T> List<T> resolveDependencyMap(Map<T, Set<T>> dependencies) {
        List<T> result = new ArrayList<>(dependencies.size());
        resolveDependencyMap(dependencies, result);
        return result;
    }
}