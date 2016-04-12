package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands;

import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;

/**
 * This {@link Command} is used for adding a {@link RegisterExtension}.
 *
 * @author Philipp Rohde
 */
public class RegisterAddedCommand extends Command {

    private final RegisterExtension register;
    private final MachineConfiguration config;

    /**
     * Creates the {@code RegisterAddedCommand} instance for the change.
     *
     * @param register
     *         the register to add
     * @param config
     *         the {@code MachineConfiguration} of the simulated machine
     */
    public RegisterAddedCommand(RegisterExtension register, MachineConfiguration config) {
        this.register = register;
        this.config = config;
    }

    @Override
    public void execute() {
        config.addRegisterExtension(register);
    }

    @Override
    public void undo() {
        config.removeRegisterExtension(register);
    }
}
