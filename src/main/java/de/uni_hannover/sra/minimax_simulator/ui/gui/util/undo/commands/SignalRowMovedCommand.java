package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands;

import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;

/**
 * This {@link Command} is used for moving {@link de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow}s.
 *
 * @author Philipp Rohde
 */
public class SignalRowMovedCommand implements Command {

    private final int index;
    private final int difference;
    private final SignalTable table;

    /**
     * Creates the {@code SignalRowMovedCommand} instance for the change.
     *
     * @param index
     *         the index of the {@code SignalRow} to move
     * @param difference
     *         the difference of the index to the index after moving
     * @param table
     *         the {@code SignalTable} of the simulated machine
     */
    public SignalRowMovedCommand(int index, int difference, SignalTable table) {
        this.index = index;
        this.difference = difference;
        this.table = table;
    }

    @Override
    public void execute() {
        table.moveSignalRows(index, index, difference);
    }

    @Override
    public void undo() {
        table.moveSignalRows(index + difference, index + difference, -difference);
    }

    @Override
    public void redo() {
        this.execute();
    }
}
