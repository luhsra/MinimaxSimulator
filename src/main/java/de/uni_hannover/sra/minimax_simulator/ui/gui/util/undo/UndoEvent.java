package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo;

/**
 * Event used for notifying the {@link UndoListener}s.
 *
 * @author Philipp Rohde
 */
public class UndoEvent {

    private final boolean isSavedState;
    private final boolean undoAvailable;
    private final boolean redoAvailable;

    private final String undoCommandName;
    private final String redoCommandName;

    /**
     * Creates a new {@code UndoEvent} object with the specified parameters.
     *
     * @param isSaved
     *         the {@code isSaved} property
     * @param undoAvailable
     *         the {@code undoAvailable} property
     * @param redoAvailable
     *         the {@code redoAvailable} property
     * @param undo
     *         the command name of the next {@code Command} in the undo list
     * @param redo
     *         the command name of the nex {@code Command} in the redo list
     */
    UndoEvent(boolean isSaved, boolean undoAvailable, boolean redoAvailable, String undo, String redo) {
        this.isSavedState = isSaved;
        this.undoAvailable = undoAvailable;
        this.redoAvailable = redoAvailable;

        this.undoCommandName = undo;
        this.redoCommandName = redo;
    }

    /**
     * Whether the project is in a saved state or not.
     *
     * @return
     *         {@code true} if the project is in a saved state, {@code false} otherwise
     */
    public boolean isSaved() {
        return this.isSavedState;
    }

    /**
     * Whether there is a {@code Command} to undo or not.
     *
     * @return
     *         {@code true} if a {@code Command} can be undone, {@code false} otherwise
     */
    public boolean undoAvailable() {
        return this.undoAvailable;
    }

    /**
     * Whether there is a {@code Command} to redo or not.
     *
     * @return
     *         {@code true} if a {@code Command} can be redone, {@code false} otherwise
     */
    public boolean redoAvailable() {
        return this.redoAvailable;
    }

    /**
     * Gets the name of the next {@code Command} in the undo list.
     *
     * @return
     *         the name of the next {@code Command} in the undo list, empty string if there is none
     */
    public String undoCommandName() {
        return this.undoCommandName;
    }

    /**
     * Gets the name of the next {@code Command} in the redo list.
     *
     * @return
     *         the name of the next {@code Command} in the redo list, empty string if there is none
     */
    public String redoCommandName() {
        return this.redoCommandName;
    }

}
