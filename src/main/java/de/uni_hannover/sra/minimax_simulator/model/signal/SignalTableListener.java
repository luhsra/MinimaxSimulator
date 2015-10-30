package de.uni_hannover.sra.minimax_simulator.model.signal;

/**
 * A {@code SignalTableListener} is a class that needs to react to changes of a {@link SignalTable}.
 *
 * @author Martin L&uuml;ck
 */
public interface SignalTableListener {

	/**
	 * Notifies the listener about a change of the structure.
	 */
	public void onStructureChanged();

	/**
	 * Notifies the listener about the addition of a {@link SignalRow}.
	 *
	 * @param index
	 *          the index of the new {@code SignalRow}
	 * @param row
	 *          the new {@code SignalRow}
	 */
	public void onRowAdded(int index, SignalRow row);

	/**
	 * Notifies the listener about the deletion of a {@link SignalRow}.
	 *
	 * @param index
	 *          the index of the removed {@code SignalRow}
	 */
	public void onRowRemoved(int index);

	/**
	 * Notifies the listener about the exchange of two {@link SignalRow}s.
	 *
	 * @param index1
	 *          the index of the first {@code SignalRow}
	 * @param index2
	 *          the index of the second {@code SignalRow}
	 */
	public void onRowsExchanged(int index1, int index2);

	/**
	 * Notifies the listener about the replacement of a {@link SignalRow}.
	 *
	 * @param index
	 *          the index of the replaced {@code SignalRow}
	 * @param row
	 *          the new {@code SignalRow}
	 */
	public void onRowReplaced(int index, SignalRow row);

	/**
	 * Notifies the listener about the update of several {@link SignalRow}s.
	 *
	 * @param fromIndex
	 *          the index of the first {@code SignalRow} to update
	 * @param toIndex
	 *          the index of the last {@code SignalRow} to update
	 */
	public void onRowsUpdated(int fromIndex, int toIndex);
}