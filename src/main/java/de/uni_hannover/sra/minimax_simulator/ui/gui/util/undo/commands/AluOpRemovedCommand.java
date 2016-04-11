package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands;

import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;

/**
 * This {@link Command} is used for removing ALU operations.
 *
 * @author Philipp Rohde
 */
// TODO: very naive implementation; does not revert the changes made to the signal table after the deletion
public class AluOpRemovedCommand extends Command {

    private final AluOperation op;
    private final MachineConfiguration config;

    /**
     * Creates the {@code AluOpRemovedCommand} instance for the change.
     *
     * @param op
     *         the {@code AluOperation} to delete
     * @param config
     *         the {@code MachineConfiguration} of the simulated machine
     */
    public AluOpRemovedCommand(AluOperation op, MachineConfiguration config) {
        this.op = op;
        this.config = config;
    }

    @Override
    public void execute() {
        config.removeAluOperation(op);
    }

    @Override
    public void undo() {
        config.addAluOperation(op);
    }

    @Override
    public void redo() {
        this.execute();
    }
}
