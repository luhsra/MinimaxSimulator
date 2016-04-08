package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands;

import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;

/**
 * This {@link Command} is used for deleting {@link MuxInput}s from the multiplexers.
 *
 * @author Philipp Rohde
 */
// TODO: very naive implementation; does not revert the changes made to the signal table after the deletion
public class MuxInputRemovedCommand implements Command {

    private final MuxType mux;
    private int index;
    private final MachineConfiguration config;
    private final MuxInput input;

    /**
     * Creates the {@code MuxInputRemovedCommand} instance for the change.
     *
     * @param mux
     *         the multiplexer where the input should be deleted
     * @param index
     *         the index of the input to delete
     * @param config
     *         the {@code MachineConfiguration} of the simulated machine
     */
    public MuxInputRemovedCommand(MuxType mux, int index, MachineConfiguration config) {
        this.mux = mux;
        this.index = index;
        this.config = config;
        this.input = config.getMuxSources(mux).get(index);
    }

    @Override
    public void execute() {
        config.removeMuxSource(mux, index);
    }

    @Override
    public void undo() {
        config.addMuxSource(mux, input);
        index = config.getMuxSources(mux).size() - 1;
    }

    @Override
    public void redo() {
        this.execute();
    }
}
