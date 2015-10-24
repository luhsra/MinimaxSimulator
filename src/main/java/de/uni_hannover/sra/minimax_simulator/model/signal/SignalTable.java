package de.uni_hannover.sra.minimax_simulator.model.signal;

import com.google.common.collect.ImmutableList;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;

/**
 * The control table of the machine.
 *
 * @author Martin L&uuml;ck
 */
public interface SignalTable {

	/**
	 * Gets the row count of the {@code SignalTable}.
	 *
	 * @return
	 *          the row count
	 */
	public int getRowCount();

	/**
	 * Gets the {@link SignalRow} at the specified index.
	 *
	 * @param index
	 *          the index
	 * @return
	 *          the {@code SignalRow}
	 */
	public SignalRow getRow(int index);

	/**
	 * Gets the {@link SignalRow}s of the {@code SignalTable}.
	 *
	 * @return
	 *          an immutable list of the {@code SignalRow}s
	 */
	public ImmutableList<SignalRow> getRows();

	/**
	 * Registers the specified {@link SignalTableListener}.
	 *
	 * @param l
	 *          the {@code SignalTableListener} to add
	 */
	public void addSignalTableListener(SignalTableListener l);

	/**
	 * Removes the specified {@link SignalTableListener}.
	 *
	 * @param l
	 *          the {@code SignalTableListener} to remove
	 */
	public void removeSignalTableListener(SignalTableListener l);

	/**
	 * Adds the specified {@link SignalRow}.
	 *
	 * @param row
	 *          the {@code SignalRow} to add
	 */
	public void addSignalRow(SignalRow row);

	/**
	 * Adds the specified {@link SignalRow} at the specified index.
	 *
	 * @param index
	 *          the index
	 * @param row
	 *          the {@code SignalRow} to add
	 */
	public void addSignalRow(int index, SignalRow row);

	/**
	 * Removes the {@link SignalRow} at the specified index.
	 *
	 * @param index
	 *          the index of the {@code SignalRow} to remove
	 */
	public void removeSignalRow(int index);

	/**
	 * Exchanges two {@link SignalRow}s.
	 *
	 * @param index1
	 *          the index of the first {@code SignalRow}
	 * @param index2
	 *          the index of the second {@code SignalRow}
	 */
	public void exchangeSignalRows(int index1, int index2);

	/**
	 * Moves the specified {@link SignalRow}s to the specified direction.
	 *
	 * @param firstIndex
	 *          the index of the first {@code SignalRow} to move
	 * @param lastIndex
	 *          the index of the last {@code SignalRow} to move
	 * @param direction
	 *          {@code -1} for up and {@code 1} for down
	 */
	public void moveSignalRows(int firstIndex, int lastIndex, int direction);

	/**
	 * Sets the specified {@link SignalRow} to the specified index.
	 *
	 * @param index
	 *          the index
	 * @param row
	 *          the new {@code SignalRow}
	 */
	public void setSignalRow(int index, SignalRow row);

	/**
	 * Gets the {@link DescriptionFactory} of the {@code SignalTable}.
	 *
	 * @return
	 *          the {@code DescriptionFactory}
	 */
	DescriptionFactory getDescriptionFactory();

	/**
	 * Sets the specified {@link SignalValue} to the specified signal of the {@link SignalRow} at the specified index.
	 * <br><br>
	 * <b>Caution:</b> this breaks a running simulation!
	 *
	 * @param index
	 *          the index of the {@code SignalRow}
	 * @param signal
	 *          the name of the signal
	 * @param value
	 *          the new {@code SignalValue}
	 */
	public void setRowSignal(int index, String signal, SignalValue value);

	/**
	 * Sets the {@link Jump} of the {@link SignalRow} at the specified index.
	 * <br><br>
	 * <b>Caution:</b> this breaks a running simulation!
	 *
	 * @param index
	 *          the index of the {@code SignalRow}
	 * @param jump
	 *          the new {@code Jump}
	 */
	public void setRowJump(int index, Jump jump);
}