package de.uni_hannover.sra.minimax_simulator.model.machine.simulation;

/**
 * A {@code TraceableChangeListener} is a class that needs to react
 * to a change of the value of a traceable class.
 *
 * @param <T>
 *          the class of the traceable value
 *
 * @author Martin L&uuml;ck
 */
@FunctionalInterface
public interface TraceableChangeListener<T> {

    /**
     * Notifies the listener about a change of the value.
     *
     * @param value
     *          the new value
     */
    public void onValueChanged(T value);
}