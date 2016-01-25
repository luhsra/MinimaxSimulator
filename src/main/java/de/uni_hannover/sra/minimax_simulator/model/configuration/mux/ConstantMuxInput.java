package de.uni_hannover.sra.minimax_simulator.model.configuration.mux;

/**
 * A {@code ConstantMuxInput} is a {@link MuxInput} consisting of a constant.
 *
 * @author Martin L&uuml;ck
 */
public class ConstantMuxInput implements MuxInput {

    private final int constant;

    /**
     * Constructs a new {@code ConstantMuxInput} with the specified constant.
     *
     * @param constant
     *          the constant to set to the {@code ConstantMuxInput}
     */
    public ConstantMuxInput(int constant) {
        this.constant = constant;
    }

    /**
     * Gets the constant of the {@code ConstantMuxInput}.
     *
     * @return
     *          the constant of the {@code ConstantMuxInput}
     */
    public int getConstant() {
        return constant;
    }

    @Override
    public String toString() {
        return "ConstantMuxInput[" + constant + "]";
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

        return ((ConstantMuxInput) o).constant == this.constant;
    }

    @Override
    public int hashCode() {
        return 31 * constant;
    }

    @Override
    public String getName() {
        return Integer.toString(constant);
    }
}