package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

/**
 * Groups {@link MuxInputManager}s.
 *
 * @author Martin L&uuml;ck
 */
interface MuxInputGroupManager {

    /**
     * Updates the specified {@link MuxInputManager}.
     *
     * @param muxInputs
     *          the {@code MuxInputManager} to update
     */
    public void update(MuxInputManager muxInputs);
}