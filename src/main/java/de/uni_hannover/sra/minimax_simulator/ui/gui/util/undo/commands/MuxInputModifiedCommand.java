package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands;

import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;

/**
 * This {@link Command} is used for modifying {@link MuxInput}s of the multiplexers.
 *
 * @author Philipp Rohde
 */
public class MuxInputModifiedCommand implements Command {

    private final MuxType mux;
    private final int index;
    private final MuxInput oldValue;
    private final MuxInput newValue;
    private final MachineConfiguration config;

    /**
     * Creates the {@code MuxInputModifiedCommand} instance for the change.
     *
     * @param mux
     *         the multiplexer where the input was modified
     * @param index
     *         the index of the modified input
     * @param newValue
     *         the new value of the input
     * @param config
     *         the {@code MachineConfiguration} of the simulated machine
     */
    public MuxInputModifiedCommand(MuxType mux, int index, MuxInput newValue, MachineConfiguration config) {
        this.mux = mux;
        this.index = index;
        this.oldValue = config.getMuxSources(mux).get(index);
        this.newValue = newValue;
        this.config = config;
    }

    @Override
    public void execute() {
        config.setMuxSource(mux, index, newValue);
    }

    @Override
    public void undo() {
        config.setMuxSource(mux, index, oldValue);
    }

    @Override
    public void redo() {
        this.execute();
    }
}
