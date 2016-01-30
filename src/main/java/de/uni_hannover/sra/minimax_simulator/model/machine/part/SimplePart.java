package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;

import java.util.Set;

/**
 * Basic implementation of a simple component part.<br>
 * A {@code SimplePart} differs from a basic {@link Part} in having an {@link OutgoingPin} attached.
 *
 * @author Martin L&uuml;ck
 */
public abstract class SimplePart extends Part {

    private final OutgoingPin dataOut;

    /**
     * Constructs a new {@code SimplePart}.
     */
    protected SimplePart() {
        dataOut = new OutgoingPin(this);
    }

    /**
     * Gets the {@link OutgoingPin}.
     *
     * @return
     *          the output pin
     */
    public OutgoingPin getDataOut() {
        return dataOut;
    }

    @Override
    public Set<? extends Circuit> getSuccessors() {
        return dataOut.getSuccessors();
    }

    @Override
    public void reset() {

    }
}