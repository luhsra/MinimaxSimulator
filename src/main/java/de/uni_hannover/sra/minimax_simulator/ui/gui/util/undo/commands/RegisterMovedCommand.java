package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands;

import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;

/**
 * This {@link Command} is used for the change of position of a {@link de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension}.
 *
 * @author Philipp Rohde
 */
public class RegisterMovedCommand extends Command {

    private final int index1;
    private final int index2;
    private final MachineConfiguration config;

    /**
     * Creates the {@code RegisterMovedCommand} instance for the change.
     *
     * @param index1
     *         the index of the first register
     * @param index2
     *         the index of the second register
     * @param config
     *         the {@code MachineConfiguration} of the simulated machine
     */
    public RegisterMovedCommand(int index1, int index2, MachineConfiguration config) {
        this.index1 = index1;
        this.index2 = index2;
        this.config = config;
    }

    @Override
    public void execute() {
        config.exchangeRegisterExtensions(index1, index2);
    }

    @Override
    public void undo() {
        config.exchangeRegisterExtensions(index2, index1);
    }

    @Override
    public void redo() {
        config.exchangeRegisterExtensions(index1, index2);
    }
}
