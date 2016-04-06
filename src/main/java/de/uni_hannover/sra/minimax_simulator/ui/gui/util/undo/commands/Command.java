package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands;

/**
 * Atomic user action which can be undone and redone.
 *
 * @author Philipp Rohde
 */
public interface Command {

    /**
     * Executes the command.
     */
    public void execute();

    /**
     * Undoes the changes made by the command.
     */
    public void undo();

    /**
     * Redoes the changes made by the command.
     */
    public void redo();
}
