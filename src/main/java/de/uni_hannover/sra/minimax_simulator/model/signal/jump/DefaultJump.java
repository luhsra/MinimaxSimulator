package de.uni_hannover.sra.minimax_simulator.model.signal.jump;

/**
 * The {@code DefaultJump} is the implementation of the standard behaviour which is executing the next row afterwards.
 *
 * @author Martin L&uuml;ck
 */
public final class DefaultJump implements Jump {

	/** The {@code DefaultJump} instance. */
	public static final Jump INSTANCE = new DefaultJump();

	/**
	 * Constructs the singleton instance.
	 */
	private DefaultJump() {

	}

	@Override
	public int getTargetRow(int currentRow, int condition) {
		return currentRow + 1;
	}

	@Override
	public boolean equals(Object o) {
		return o == INSTANCE;
	}

	@Override
	public int hashCode() {
		return 31;
	}
}