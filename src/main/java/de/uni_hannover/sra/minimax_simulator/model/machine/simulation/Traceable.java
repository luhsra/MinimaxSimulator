package de.uni_hannover.sra.minimax_simulator.model.machine.simulation;

/**
 * A {@code Traceable} is a class that can be tracked.
 *
 * @param <T>
 *          the class of the {@code Traceable}
 *
 * @author Martin L&uuml;ck
 */
public interface Traceable<T> {

    /**
     * Gets the value of the {@code Traceable}.
     *
     * @return
     *          the value
     */
    public T get();

    /**
     * Sets the value of the {@code Traceable}.
     *
     * @param value
     *          the new value
     */
    public void set(T value);

    /**
     * Registers the specified {@link TraceableChangeListener}.
     *
     * @param listener
     *          the listener to register
     */
    public void addChangeListener(TraceableChangeListener<T> listener);

    /**
     * Removes the specified {@link TraceableChangeListener}.
     *
     * @param listener
     *          the listener to remove
     */
    public void removeChangeListener(TraceableChangeListener<T> listener);

    /**
     * Removes all {@link TraceableChangeListener}s.
     */
    public void clearListeners();
}