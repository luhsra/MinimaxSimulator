package de.uni_hannover.sra.minimax_simulator.model.configuration.event;

/**
 * A {@code MachineConfigListener} is a class that needs to react to changes
 * of the {@link de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration}.
 *
 * @author Martin L&uuml;ck
 */
public interface MachineConfigListener {

    /**
     * Handles the specified {@link MachineConfigEvent}.
     *
     * @param event
     *          the {@code MachineConfigEvent} to handle
     */
    public void processEvent(MachineConfigEvent event);
}