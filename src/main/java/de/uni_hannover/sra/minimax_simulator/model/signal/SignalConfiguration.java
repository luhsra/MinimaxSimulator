package de.uni_hannover.sra.minimax_simulator.model.signal;

import com.google.common.collect.ImmutableList;

/**
 * The configuration of a signal.
 *
 * @author Martin L&uuml;ck
 */
public interface SignalConfiguration {

	/**
	 * Gets the {@link SignalType}s related to the signal.
	 *
	 * @return
	 *          a list of the {@code SignalType}s
	 */
	// TODO: use immutable lists for other interfaces, too
	public ImmutableList<SignalType> getSignalTypes();

	/**
	 * Adds the specified {@code SignalType} at the specified index.
	 *
	 * @param index
	 *          the index of the new {@code SignalType}
	 * @param signal
	 *          the new {@code SignalType}
	 */
	public void addSignalType(int index, SignalType signal);

	/**
	 * Removes the {@code SignalType} at the specified index.
	 *
	 * @param index
	 *          the index of the {@code SignalType} to remove
	 */
	public void removeSignalType(int index);

	/**
	 * Exchanges two {@code SignalType}s.
	 *
	 * @param index1
	 *          the index of the first {@code SignalType}
	 * @param index2
	 *          the index of the second {@code SignalType}
	 */
	public void exchangeSignalsType(int index1, int index2);

	/**
	 * Replaces the {@code SignalType} at the specified index with the specified {@code SignalType}.
	 *
	 * @param index
	 *          the index of the {@code SignalType} to replace
	 * @param signal
	 *          the new {@code SignalType}
	 */
	public void replaceSignalType(int index, SignalType signal);

	/**
	 * Registers the specified {@link SignalConfigListener}.
	 *
	 * @param l
	 *          the {@code SignalConfigListener} to add
	 */
	public void addSignalConfigListener(SignalConfigListener l);

	/**
	 * Removes the specified {@link SignalConfigListener}.
	 *
	 * @param l
	 *          the {@code SignalConfigListener} to remove
	 */
	public void removeSignalConfigListener(SignalConfigListener l);
}