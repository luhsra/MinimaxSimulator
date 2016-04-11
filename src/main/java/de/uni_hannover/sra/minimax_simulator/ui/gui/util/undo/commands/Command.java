package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands;

/**
 * Atomic user action which can be undone and redone.
 *
 * @author Philipp Rohde
 */
public abstract class Command {

    private boolean marked = false;

    /**
     * Executes the command.
     */
    public abstract void execute();

    /**
     * Undoes the changes made by the command.
     */
    public abstract void undo();

    /**
     * Redoes the changes made by the command.
     */
    public abstract void redo();

    /**
     * Marks the command.<br>
     * A command will be marked if it represents the latest saved state.
     */
    public void mark() {
        marked = true;
    }

    /**
     * Unmarks the command.<br>
     * A command will be unmarked if if no longer represents the latest saved state.
     */
    public void unmark() {
        marked = false;
    }

    /**
     * Gets the {@code isMarked} property of the command.
     *
     * @return
     *         {@code true} if the {@code Command} represents the latest saved state, {@code false} otherwise
     */
    public boolean isMarked() {
        return marked;
    }
}
