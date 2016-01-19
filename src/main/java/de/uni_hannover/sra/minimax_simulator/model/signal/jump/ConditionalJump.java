package de.uni_hannover.sra.minimax_simulator.model.signal.jump;

/**
 * A {@code ConditionalJump} has two target {@link de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow}s
 * depending on the ALU condition after the execution of the {@code SignalRow} the jump belongs to.
 *
 * @author Martin L&uuml;ck
 */
public final class ConditionalJump implements Jump {

    private final int condZeroTarget;
    private final int condOneTarget;

    /**
     * Constructs a new {@code ConditionalJump} with the specified target rows.
     *
     * @param condZeroTarget
     *          the index of the target row if ALU condition is {@code 0}
     * @param condOneTarget
     *          the index of the target row if ALU condition is {@code 1}
     */
    public ConditionalJump(int condZeroTarget, int condOneTarget) {
        this.condZeroTarget = condZeroTarget;
        this.condOneTarget = condOneTarget;
    }

    @Override
    public int getTargetRow(int currentRow, int condition) {
        return getTargetRow(condition);
    }

    /**
     * Gets the index of the {@code SignalRow} that will be executed next
     * if the ALU condition is the specified condition.
     *
     * @param condition
     *          the ALU condition
     * @return
     *          the index of the {@code SignalRow} that will be executed next if ALU condition is as specified
     */
    public int getTargetRow(int condition) {
        return condition == 0 ? condZeroTarget : condOneTarget;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + condOneTarget;
        result = prime * result + condZeroTarget;
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

        ConditionalJump other = (ConditionalJump) obj;
        if (condOneTarget != other.condOneTarget) {
            return false;
        }
        if (condZeroTarget != other.condZeroTarget) {
            return false;
        }
        return true;
    }
}