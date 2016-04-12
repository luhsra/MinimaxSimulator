package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;

import java.util.ArrayList;
import java.util.List;

/**
 * This {@link Command} is used for deleting {@link SignalRow}s.
 *
 * @author Philipp Rohde
 */
public class SignalRowRemovedCommand extends Command {

    private final int index;
    private final SignalTable table;
    private final List<SignalRow> rows;

    /**
     * Creates the {@code SignalRowRemovedCommand} instance for the change.
     *
     * @param index
     *         the index of the {@code SignalRow} to delete
     * @param table
     *         the {@code SignalTable} of the simulated machine
     */
    public SignalRowRemovedCommand(int index, SignalTable table) {
        this.index = index;
        this.table = table;

        this.rows = new ArrayList<>();
        for (SignalRow row : table.getRows()) {
            this.rows.add(new SignalRow(row));
        }
    }

    @Override
    public void execute() {
        table.removeSignalRow(index);
    }

    @Override
    public void undo() {
        for (int i = table.getRowCount() - 1; i >= 0; i--) {
            table.removeSignalRow(i);
        }
        for (int i = 0; i < rows.size(); i++) {
            table.addSignalRow(rows.get(i));
        }
    }

    @Override
    public void redo() {
        this.execute();
    }
}
