package de.uni_hannover.sra.minimax_simulator.model.signal;

/**
 * Factory for creating the description of a {@link SignalRow}.
 *
 * @author Martin L&uuml;ck
 */
public interface DescriptionFactory {

	/**
	 * Creates the description for the specified {@link SignalRow}.
	 *
	 * @param rowIndex
	 *          the index of the {@code SignalRow}
	 * @param row
	 *          the {@code SignalRow}
	 * @return
	 *          the description of the {@code SignalRow}
	 */
	public String createDescription(int rowIndex, SignalRow row);
}