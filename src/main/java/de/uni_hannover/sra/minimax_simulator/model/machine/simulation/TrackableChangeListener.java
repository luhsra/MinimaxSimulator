package de.uni_hannover.sra.minimax_simulator.model.machine.simulation;

/**
 * A {@code TrackableChangeListener} is a class that needs to react
 * to a change of the value of a trackable class.
 *
 * @param <T>
 *          the class of the trackable value
 *
 * @author Martin L&uuml;ck
 */
// TODO: rename to TraceableChangeListener
public interface TrackableChangeListener<T> {

	/**
	 * Notifies the listener about a change of the value.
	 *
	 * @param value
	 *          the new value
	 */
	public void onValueChanged(T value);
}