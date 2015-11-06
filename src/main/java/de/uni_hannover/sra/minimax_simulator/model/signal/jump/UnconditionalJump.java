package de.uni_hannover.sra.minimax_simulator.model.signal.jump;

/**
 * An {@code UnconditionalJump} is a jump to any other {@link de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow}
 * excluding the next one. The {@code UnconditionalJump} is independent from the ALU condition.
 *
 * @author Martin L&uuml;ck
 */
public final class UnconditionalJump implements Jump {

	private final int targetRow;

	/**
	 * Constructs a new {@code UnconditionalJump} with the specified target row.
	 *
	 * @param targetRow
	 *          the index of the target row
	 */
	public UnconditionalJump(int targetRow) {
		this.targetRow = targetRow;
	}

	@Override
	public int getTargetRow(int currentRow, int condition) {
		return targetRow;
	}

	/**
	 * Gets the index of the {@code SignalRow} that will be executed next.
	 *
	 * @return
	 *          the index of the {@code SignalRow} that will be executed next
	 */
	public int getTargetRow() {
		return targetRow;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + targetRow;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		else if (obj == null) {
			return false;
		}
		else if (getClass() != obj.getClass()) {
			return false;
		}

		UnconditionalJump other = (UnconditionalJump) obj;
		if (targetRow != other.targetRow) {
			return false;
		}
		return true;
	}
}