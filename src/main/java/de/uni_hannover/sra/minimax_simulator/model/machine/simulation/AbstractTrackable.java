package de.uni_hannover.sra.minimax_simulator.model.machine.simulation;

import java.util.ArrayList;

/**
 * Basic implementation of a {@link Trackable}.
 *
 * @param <T>
 *          the trackable class
 *
 * @author Martin L&uuml;ck
 */
public abstract class AbstractTrackable<T> implements Trackable<T> {

	private final ArrayList<TrackableChangeListener<T>> listeners;

	/**
	 * Constructs a new {@code AbstractTrackable}.
	 */
	public AbstractTrackable() {
		listeners = new ArrayList<TrackableChangeListener<T>>(2);
	}

	/**
	 * Notifies the {@link TrackableChangeListener}s of a value change.
	 */
	protected void fireValueChanged() {
		T value = get();
		for (TrackableChangeListener<T> listener : listeners) {
			listener.onValueChanged(value);
		}
	}

	@Override
	public void clearListeners() {
		listeners.clear();
	}

	@Override
	public void addChangeListener(TrackableChangeListener<T> listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeChangeListener(TrackableChangeListener<T> listener) {
		listeners.remove(listener);
	}
}