package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;

/**
 * This {@link Command} is used for deleting {@link SignalRow}s.
 *
 * @author Philipp Rohde
 */
// TODO: very naive implementation; does not revert the changes made to the signal table after the deletion
public class SignalRowRemovedCommand extends Command {

    private final int index;
    private final SignalTable table;
    private final SignalRow row;

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
        this.row = table.getRow(index);
    }

    @Override
    public void execute() {
        table.removeSignalRow(index);
    }

    @Override
    public void undo() {
        table.addSignalRow(index, row);
    }

    @Override
    public void redo() {
        this.execute();
    }
}
