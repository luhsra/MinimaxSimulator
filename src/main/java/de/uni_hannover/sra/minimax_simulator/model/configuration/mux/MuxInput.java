package de.uni_hannover.sra.minimax_simulator.model.configuration.mux;

/**
 * A {@code MuxInput} is the input of a multiplexer.
 *
 * @author Martin L&uuml;ck
 */
public interface MuxInput {

    /**
     * Gets the name of the {@code MuxInput}.
     *
     * @return
     *          the name of the {@code MuxInput}
     */
    public String getName();

    @Override
    public boolean equals(Object o);

    @Override
    public int hashCode();
}