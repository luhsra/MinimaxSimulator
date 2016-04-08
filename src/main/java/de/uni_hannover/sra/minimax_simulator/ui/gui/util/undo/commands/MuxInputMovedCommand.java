package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands;

import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;

/**
 * This {@link Command} is used for moving {@link de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput}s
 * of the multiplexers.
 *
 * @author Philipp Rohde
 */
public class MuxInputMovedCommand implements Command {

    private final MuxType mux;
    private final int index1;
    private final int index2;
    private final MachineConfiguration config;

    /**
     * Creates the {@code MuxInputMovedCommand} instance for the change.
     *
     * @param mux
     *         the multiplexer where the input was moved
     * @param index1
     *         the index of the first input
     * @param index2
     *         the index of the second input
     * @param config
     *         the {@code MachineConfiguration} of the simulated machine
     */
    public MuxInputMovedCommand(MuxType mux, int index1, int index2, MachineConfiguration config) {
        this.mux = mux;
        this.index1 = index1;
        this.index2 = index2;
        this.config = config;
    }

    @Override
    public void execute() {
        config.exchangeMuxSources(mux, index1, index2);
    }

    @Override
    public void undo() {
        config.exchangeMuxSources(mux, index2, index1);
    }

    @Override
    public void redo() {
        this.execute();
    }
}
