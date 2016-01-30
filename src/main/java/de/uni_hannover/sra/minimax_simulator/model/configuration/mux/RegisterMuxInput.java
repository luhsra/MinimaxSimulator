package de.uni_hannover.sra.minimax_simulator.model.configuration.mux;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A {@code RegisterMuxInput} is a {@link MuxInput} consisting of a register ({@link de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension}).
 *
 * @author Martin L&uuml;ck
 */
public class RegisterMuxInput implements MuxInput {

    private final String registerName;
    private final String name;
    private final int hashCache;

    /**
     * Constructs a new {@code RegisterMuxInput} using the specified register.
     *
     * @param registerName
     *          the name of the {@link de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension} belonging to the {@code RegisterMuxInput}
     */
    public RegisterMuxInput(String registerName) {
        this(registerName, registerName);
    }

    /**
     * Constructs a new {@code RegisterMuxInput} using the specified register and display name.
     *
     * @param registerName
     *          the name of the {@link de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension} belonging to the {@code RegisterMuxInput}
     * @param name
     *          the name to display name of the {@code RegisterMuxInput}
     */
    public RegisterMuxInput(String registerName, String name) {
        this.registerName = checkNotNull(registerName);
        this.name = checkNotNull(name);

        checkArgument(!registerName.isEmpty());
        checkArgument(!name.isEmpty());

        hashCache = computeHashCode();
    }

    /**
     * Gets the name of the register belonging to the {@code RegisterMuxInput}.
     *
     * @return
     *          the name of the register belonging to the {@code RegisterMuxInput}
     */
    public String getRegisterName() {
        return registerName;
    }

    @Override
    public String toString() {
        return "RegisterMuxInput[" + registerName + "]";
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

        return ((RegisterMuxInput) o).registerName.equals(registerName) && ((RegisterMuxInput) o).name.equals(name);
    }

    /**
     * Computes the hash code of the {@code RegisterMuxInput}.
     *
     * @return
     *          the computed hash code
     */
    private int computeHashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + registerName.hashCode();
        result = prime * result + name.hashCode();
        return result;
    }

    @Override
    public int hashCode() {
        return hashCache;
    }

    @Override
    public String getName() {
        return name;
    }
}