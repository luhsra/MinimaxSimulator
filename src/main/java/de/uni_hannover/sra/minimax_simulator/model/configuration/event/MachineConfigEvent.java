package de.uni_hannover.sra.minimax_simulator.model.configuration.event;

/**
 * A {@code MachineConfigEvent} is an event taking place when the
 * {@link de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration} changes.
 *
 * @author Martin L&uuml;ck
 */
public abstract class MachineConfigEvent {

    // protected because the events are not instantiable from outside
    protected MachineConfigEvent() {

    }
}