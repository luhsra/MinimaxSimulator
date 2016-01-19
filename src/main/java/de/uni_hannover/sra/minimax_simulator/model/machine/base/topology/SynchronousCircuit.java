package de.uni_hannover.sra.minimax_simulator.model.machine.base.topology;

/**
 * A {@code Synchronous Circuit} is a {@link Circuit} with synchronous operation.
 *
 * @author Martin L&uuml;ck
 */
public interface SynchronousCircuit extends Circuit {

    /**
     * Switches the cycle in a {@code SynchronousCircuit}.
     */
    public void nextCycle();
}