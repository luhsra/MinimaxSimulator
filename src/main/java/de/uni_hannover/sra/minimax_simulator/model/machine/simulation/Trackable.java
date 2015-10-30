package de.uni_hannover.sra.minimax_simulator.model.machine.simulation;

/**
 * A {@code Trackable} is a class that can be tracked.
 *
 * @param <T>
 *          the class of the {@code Trackable}
 *
 * @author Martin L&uuml;ck
 */
// TODO: rename to Traceable
public interface Trackable<T> {

	/**
	 * Gets the value of the {@code Trackable}.
	 *
	 * @return
	 *          the value
	 */
	public T get();

	/**
	 * Sets the value of the {@code Trackable}.
	 *
	 * @param value
	 *          the new value
	 */
	public void set(T value);

	/**
	 * Registers the specified {@link TrackableChangeListener}.
	 *
	 * @param listener
	 *          the listener to register
	 */
	public void addChangeListener(TrackableChangeListener<T> listener);

	/**
	 * Removes the specified {@link TrackableChangeListener}.
	 *
	 * @param listener
	 *          the listener to remove
	 */
	public void removeChangeListener(TrackableChangeListener<T> listener);

	/**
	 * Removes all {@link TrackableChangeListener}s.
	 */
	public void clearListeners();
}