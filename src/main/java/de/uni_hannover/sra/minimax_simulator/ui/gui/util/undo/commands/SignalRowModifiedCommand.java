package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;

/**
 * This {@link Command} is used to modify {@link SignalRow}s.
 *
 * @author Philipp Rohde
 */
public class SignalRowModifiedCommand extends Command {

    private final int index;
    private final SignalRow oldValue;
    private final SignalRow newValue;
    private final SignalTable table;

    /**
     * Creates the {@code SignalRowModifiedCommand} instance for the change.
     *
     * @param index
     *         the index of the modified {@code SignalRow}
     * @param oldValue
     *         the old value of the {@code SignalRow}
     * @param newValue
     *         the new value of the {@code SignalRow}
     * @param table
     *         the {@code SignalTable} of the simulated machine
     */
    public SignalRowModifiedCommand(int index, SignalRow oldValue, SignalRow newValue, SignalTable table) {
        this.index = index;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.table = table;
    }

    @Override
    public void execute() {
        table.setSignalRow(index, newValue);
    }

    @Override
    public void undo() {
        table.setSignalRow(index, oldValue);
    }

    @Override
    public void redo() {
        this.execute();
    }
}
