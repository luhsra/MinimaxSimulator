package de.uni_hannover.sra.minimax_simulator.model.machine.base.topology;

import java.util.Set;

/**
 * Represents the topology of a machine.
 *
 * @author Martin L&uuml;ck
 */
public interface MachineTopology {

    /**
     * Adds a {@link Circuit} to the machine's topology.
     *
     * @param id
     *          the ID of the {@code Circuit}
     * @param c
     *          the {@code Circuit} to add
     */
    public void addCircuit(String id, Circuit c);

    /**
     * Adds a {@link Circuit} to the machine's topology belonging to the specified subclass of {@code Circuit}.
     *
     * @param clazz
     *          the class of the {@code Circuit} to add; must be a subclass of {@code Circuit}
     * @param id
     *          the ID of the {@code Circuit}
     * @param c
     *          the {@code Circuit} to add
     * @param <T>
     *          the class of the {@code Circuit} to add
     */
    public <T extends Circuit> void addCircuit(Class<T> clazz, String id, T c);

    /**
     * Removes the {@link Circuit} with the specified ID from the machine's topology.
     *
     * @param id
     *          the ID of the {@code Circuit} to remove
     */
    public void removeCircuit(String id);

    /**
     * Gets the {@code Circuit} of the specified class with the specified ID.
     *
     * @param clazz
     *          the class of the {@code Circuit}
     * @param id
     *          the ID of the {@code Circuit}
     * @param <T>
     *          the class of the {@code Circuit}
     * @return
     *          the {@code Circuit}
     */
    public <T extends Circuit> T getCircuit(Class<T> clazz, String id);

    /**
     * Returns all {@code Circuit}s of the machine's topology.
     *
     * @return
     *          a set of all {@code Circuit}s
     */
    public Set<Circuit> getAllCircuits();
}