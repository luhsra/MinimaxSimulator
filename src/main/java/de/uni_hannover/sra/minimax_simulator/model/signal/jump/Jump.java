package de.uni_hannover.sra.minimax_simulator.model.signal.jump;

/**
 * A {@code Jump} defines which {@link de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow} will
 * be executed after the current one.
 *
 * @author Martin L&uuml;ck
 */
public interface Jump {

	/**
	 * Gets the index of the {@code SignalRow} that will be executed next.
	 *
	 * @param currentRow
	 *          the index of the current {@code SignalRow}
	 * @param condition
	 *          the ALU condition
	 * @return
	 *          the index of the {@code SignalRow} that will be executed next
	 */
	public int getTargetRow(int currentRow, int condition);
}