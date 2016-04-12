package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;

import java.util.ArrayList;
import java.util.List;

/**
 * This {@link Command} is used for removing ALU operations.
 *
 * @author Philipp Rohde
 */
public class AluOpRemovedCommand extends Command {

    private final AluOperation op;
    private final MachineConfiguration config;
    private final List<AluOperation> aluOps;
    private final List<SignalRow> signalRows;
    private final SignalTable signalTable;

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
        this.aluOps = new ArrayList<>();
        this.signalRows = new ArrayList<>();

        for (AluOperation aluOp : config.getAluOperations()) {
            aluOps.add(aluOp);
        }

        this.signalTable = Main.getWorkspace().getProject().getSignalTable();
        for (SignalRow row : signalTable.getRows()) {
            signalRows.add(new SignalRow(row));
        }
    }

    @Override
    public void execute() {
        config.removeAluOperation(op);
    }

    @Override
    public void undo() {
        for (int i = config.getAluOperations().size() - 1; i >= 0; i--) {
            config.removeAluOperation(config.getAluOperation(i));
        }
        for (AluOperation aluOp : aluOps) {
            config.addAluOperation(aluOp);
        }

        for (int j = signalTable.getRowCount() - 1; j >= 0; j--) {
            signalTable.removeSignalRow(j);
        }
        for (SignalRow row : signalRows) {
            signalTable.addSignalRow(row);
        }
    }

    @Override
    public void redo() {
        this.execute();
    }
}
