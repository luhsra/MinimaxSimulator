package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands;

import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;

/**
 * This {@link Command} is used for the change of the name or description of a {@link RegisterExtension}.
 *
 * @author Philipp Rohde
 */
public class RegisterModifiedCommand implements Command {

    private final int index;
    private final RegisterExtension oldValue;
    private final RegisterExtension newValue;
    private final MachineConfiguration config;

    /**
     * Creates the {@code RegisterModifiedCommand} instance for the change.
     *
     * @param index
     *         the index of the changed register
     * @param oldValue
     *         the old state of the register
     * @param newValue
     *         the new state of the register
     * @param config
     *         the {@code MachineConfiguration} of the simulated machine
     */
    public RegisterModifiedCommand(int index, RegisterExtension oldValue, RegisterExtension newValue, MachineConfiguration config) {
        this.index = index;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.config = config;
    }

    @Override
    public void execute() {
        config.setRegisterExtension(index, newValue);
    }

    @Override
    public void undo() {
        config.setRegisterExtension(index, oldValue);
    }

    @Override
    public void redo() {
        config.setRegisterExtension(index, newValue);
    }
}
