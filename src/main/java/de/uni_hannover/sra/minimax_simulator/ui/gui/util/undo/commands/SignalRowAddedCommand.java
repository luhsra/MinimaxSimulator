package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;

/**
 * This {@link Command} is used for adding {@link SignalRow}s.
 *
 * @author Philipp Rohde
 */
public class SignalRowAddedCommand extends Command {

    private int index;
    private final SignalTable table;

    /**
     * Creates the {@code SignalRowAddedCommand} instance for the change.
     *
     * @param index
     *         the index of the {@code SignalRow} to add
     * @param table
     *         the {@code SignalTable} of the simulated machine
     */
    public SignalRowAddedCommand(int index, SignalTable table) {
        this.index = index;
        this.table = table;
    }

    @Override
    public void execute() {
        if (index == -1) {
            table.addSignalRow(new SignalRow());
            index = table.getRowCount() - 1;
        }
        else {
            table.addSignalRow(index, new SignalRow());
        }
    }

    @Override
    public void undo() {
        table.removeSignalRow(index);
    }
}
