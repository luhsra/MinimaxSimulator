package de.uni_hannover.sra.minimax_simulator.util;

import java.util.ArrayList;
import java.util.List;

/**
 * This class bundles methods for listeners in a single place.<br>
 * <br>
 * The class is meant to be sub-classed.
 *
 * @param <L>
 *          the class of the listener instances
 *
 * @author Martin L&uuml;ck
 */
public class ListenerContainer<L> {

    private List<L> listeners = new ArrayList<>(2);

    /**
     * Adds the specified listener to the internal list if it is not already added
     *
     * @param listener
     *          the listener to register
     */
    public void addListener(L listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Removes the specified listener from the internal list if it was added.
     *
     * @param listener
     *          the listener to remove
     */
    public void removeListener(L listener) {
        listeners.remove(listener);
    }

    /**
     * Gets the registered listeners.
     *
     * @return
     *          a list of the registered listeners
     */
    protected List<L> getListeners() {
        return listeners;
    }
}