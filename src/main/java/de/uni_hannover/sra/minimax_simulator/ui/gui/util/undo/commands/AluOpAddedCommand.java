package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands;

import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;

/**
 * This {@link Command} is used for adding ALU operations.
 *
 * @author Philipp Rohde
 */
public class AluOpAddedCommand extends Command {

    private final AluOperation op;
    private final MachineConfiguration config;

    /**
     * Creates the {@code AluOpAddedCommand} instance for the change.
     *
     * @param op
     *         the {@code AluOperation} to add
     * @param config
     *         the {@code MachineConfiguration} of the simulated machine
     */
    public AluOpAddedCommand(AluOperation op, MachineConfiguration config) {
        super("alu.added");
        this.op = op;
        this.config = config;
    }

    @Override
    public void execute() {
        config.addAluOperation(op);
    }

    @Override
    public void undo() {
        config.removeAluOperation(op);
    }

    @Override
    public void redo() {
        this.execute();
    }
}
