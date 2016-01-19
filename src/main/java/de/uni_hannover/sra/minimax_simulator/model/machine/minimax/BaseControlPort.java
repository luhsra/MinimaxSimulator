package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import de.uni_hannover.sra.minimax_simulator.model.machine.part.Port;

/**
 * This enumeration represents the base control ports of a Minimax machine.
 *
 * @see Port
 *
 * @author Martin L&uuml;ck
 */
public enum BaseControlPort {

    ALU_SELECT_A,
    ALU_SELECT_B,
    MDR_SEL,
    MEM_CS,
    MEM_RW,
    ALU_CTRL;

    private final Port port;

    /**
     * Sets the {@link Port} belonging to the {@code BaseControlPort}.
     */
    private BaseControlPort() {
        port = new Port(this.name());
    }

    /**
     * Gets the {@link Port} belonging to the {@code BaseControlPort}.
     *
     * @return
     *          the {@code Port}
     */
    public Port port() {
        return port;
    }
}