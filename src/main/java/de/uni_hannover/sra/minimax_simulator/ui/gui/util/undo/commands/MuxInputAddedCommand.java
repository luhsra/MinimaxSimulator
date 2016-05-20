package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands;

import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.ConstantMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;

/**
 * This {@link Command} is used for adding {@link MuxInput}s to the multiplexers.
 *
 * @author Philipp Rohde
 */
public class MuxInputAddedCommand extends Command {

    private final MuxType mux;
    private final MuxInput input;
    private final MachineConfiguration config;
    private int index;

    /**
     * Creates the {@code MuxInputAddedCommand} instance for the chance.
     *
     * @param mux
     *         the multiplexer where the new input should be added
     * @param config
     *         the {@code MachineConfiguration} of the simulated machine
     */
    public MuxInputAddedCommand(MuxType mux, MachineConfiguration config) {
        super("mux.added");
        this.mux = mux;
        this.input = createDefaultMuxSource();
        this.config = config;
    }

    @Override
    public void execute() {
        config.addMuxSource(mux, input);
        index = config.getMuxSources(mux).size()-1;
    }

    @Override
    public void undo() {
        config.removeMuxSource(mux, index);
    }

    @Override
    public void redo() {
        this.execute();
    }

    /**
     * Creates a default multiplexer source. The default is a {@link ConstantMuxInput} with value zero.
     *
     * @return
     *          the default {@link MuxInput}
     */
    private static MuxInput createDefaultMuxSource() {
        return new ConstantMuxInput(0);
    }
}
