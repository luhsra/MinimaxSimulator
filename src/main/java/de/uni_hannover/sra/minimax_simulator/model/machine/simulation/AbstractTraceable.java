package de.uni_hannover.sra.minimax_simulator.model.machine.simulation;

import java.util.ArrayList;

/**
 * Basic implementation of a {@link Traceable}.
 *
 * @param <T>
 *          the traceable class
 *
 * @author Martin L&uuml;ck
 */
public abstract class AbstractTraceable<T> implements Traceable<T> {

    private final ArrayList<TraceableChangeListener<T>> listeners;

    /**
     * Constructs a new {@code AbstractTraceable}.
     */
    protected AbstractTraceable() {
        listeners = new ArrayList<TraceableChangeListener<T>>(2);
    }

    /**
     * Notifies the {@link TraceableChangeListener}s of a value change.
     */
    protected void fireValueChanged() {
        T value = get();
        for (TraceableChangeListener<T> listener : listeners) {
            listener.onValueChanged(value);
        }
    }

    @Override
    public void clearListeners() {
        listeners.clear();
    }

    @Override
    public void addChangeListener(TraceableChangeListener<T> listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeChangeListener(TraceableChangeListener<T> listener) {
        listeners.remove(listener);
    }
}