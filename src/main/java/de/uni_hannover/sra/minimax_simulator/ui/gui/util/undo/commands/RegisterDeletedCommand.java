package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands;

import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;

/**
 * This {@link Command} is used for deleting a {@link RegisterExtension}.
 *
 * @author Philipp Rohde
 */
// TODO: very naive implementation; does not revert the changes made to the MUX after the deletion
public class RegisterDeletedCommand extends Command {

    private final RegisterExtension register;
    private final MachineConfiguration config;

    /**
     * Creates the {@code RegisterDeletedCommand} instance for the change.
     *
     * @param register
     *         the register to delete
     * @param config
     *         the {@code MachineConfiguration} of the simulated machine
     */
    public RegisterDeletedCommand(RegisterExtension register, MachineConfiguration config) {
        this.register = register;
        this.config = config;
    }

    @Override
    public void execute() {
        config.removeRegisterExtension(register);
    }

    @Override
    public void undo() {
        config.addRegisterExtension(register);
    }

    @Override
    public void redo() {
        config.removeRegisterExtension(register);
    }
}
