package de.uni_hannover.sra.minimax_simulator.model.configuration.mux;

/**
 * A {@code ConstantMuxInput} is a {@link MuxInput} consisting of a constant.
 *
 * @author Martin L&uuml;ck
 */
public class ConstantMuxInput implements MuxInput {

    private final int CONSTANT;

    /**
     * Constructs a new {@code ConstantMuxInput} with the specified constant.
     *
     * @param constant
     *          the constant to set to the {@code ConstantMuxInput}
     */
    public ConstantMuxInput(int constant) {
        this.CONSTANT = constant;
    }

    /**
     * Gets the constant of the {@code ConstantMuxInput}.
     *
     * @return
     *          the constant of the {@code ConstantMuxInput}
     */
    public int getConstant() {
        return CONSTANT;
    }

    @Override
    public String toString() {
        return "ConstantMuxInput[" + CONSTANT + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }

        return ((ConstantMuxInput) o).CONSTANT == this.CONSTANT;
    }

    @Override
    public int hashCode() {
        return 31 * CONSTANT;
    }

    @Override
    public String getName() {
        return Integer.toString(CONSTANT);
    }
}