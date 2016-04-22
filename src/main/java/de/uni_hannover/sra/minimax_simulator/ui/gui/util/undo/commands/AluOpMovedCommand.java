package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands;

import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;

/**
 * This {@link Command} is used for moving ALU operations.
 *
 * @author Philipp Rohde
 */
public class AluOpMovedCommand extends Command {

    private final int index1;
    private final int index2;
    private final MachineConfiguration config;

    /**
     * Creates the {@code AluOpMovedCommand} instance for the change.
     *
     * @param index1
     *         the index of the first {@code AluOperation}
     * @param index2
     *         the index of the second {@code AluOperation}
     * @param config
     *         the {@code MachineConfiguration} of the simulated machine
     */
    public AluOpMovedCommand(int index1, int index2, MachineConfiguration config) {
        this.index1 = index1;
        this.index2 = index2;
        this.config = config;
    }

    @Override
    public void execute() {
        config.exchangeAluOperations(index1, index2);
    }

    @Override
    public void undo() {
        config.exchangeAluOperations(index2, index1);
    }

    @Override
    public void redo() {
        this.execute();
    }
}
