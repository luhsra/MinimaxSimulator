package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.ResultPort;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;

import java.util.Collections;
import java.util.Set;

/**
 * Implementation of a {@link ResultPort} as component part of a machine.
 *
 * @author Martin L&uuml;ck
 */
public class ReadablePort extends Part implements ResultPort {

    private final IngoingPin in;

    private int value;

    /**
     * Constructs a new {@code ReadablePort}.
     */
    public ReadablePort() {
        in = new IngoingPin(this);
    }

    @Override
    public void update() {
        value = in.read();
    }

    /**
     * Gets the {@link IngoingPin} of the {@code ReadablePort}.
     *
     * @return
     *          the input pin
     */
    public IngoingPin getIn() {
        return in;
    }

    @Override
    public Set<Circuit> getSuccessors() {
        return Collections.emptySet();
    }

    @Override
    public int read() {
        return value;
    }

    @Override
    public void reset() {
        value = 0;
    }

    @Override
    public String toString() {
        return "ReadablePort[name=" + getName() + ", value=" + value + "]";
    }
}