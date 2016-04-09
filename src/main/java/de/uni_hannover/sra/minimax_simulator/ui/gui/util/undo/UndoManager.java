package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo;

import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands.Command;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * The {@code UndoManager} manages all {@link Command}s made by the user and calls their {@link Command#undo()} and
 * {@link Command#redo()} methods if the action should be undone or redone.<br>
 * <br>
 * The {@code UndoManager} is a singleton!
 *
 * @author Philipp Rohde
 */
public class UndoManager {

    private SimpleBooleanProperty undoAvailable;
    private SimpleBooleanProperty redoAvailable;

    private Deque<Command> undos;
    private Deque<Command> redos;

    /** The singleton instance. */
    public static final UndoManager INSTANCE = new UndoManager();

    /**
     * Initializes the instance.
     */
    private UndoManager() {
        undos = new ArrayDeque<>();
        redos = new ArrayDeque<>();

        undoAvailable = new SimpleBooleanProperty(false);
        redoAvailable = new SimpleBooleanProperty(false);
    }

    /**
     * Adds the specified {@code Command} to the list of performed actions and performs the action.
     *
     * @param command
     *         the {@code Command} to add
     */
    public void addCommand(Command command) {
        if (!redos.isEmpty()) {
            redos.clear();
            redoAvailable.set(false);
        }

        undos.push(command);
        command.execute();
        undoAvailable.set(true);
    }

    /**
     * Undoes the latest action if there is one.
     */
    public void undo() {
        if (!undoAvailable.get() || undos.isEmpty()) {
            return;
        }

        undos.peek().undo();
        redos.push(undos.pop());

        redoAvailable.set(true);
        if (undos.isEmpty()) {
            undoAvailable.set(false);
        }
    }

    /**
     * Redoes the latest action if there is one.
     */
    public void redo() {
        if (!redoAvailable.get() || redos.isEmpty()) {
            return;
        }

        redos.peek().redo();
        undos.push(redos.pop());

        undoAvailable.set(true);
        if (redos.isEmpty()) {
            redoAvailable.set(false);
        }
    }

    /**
     * Gets the {@code undoAvailableProperty}.
     *
     * @return
     *         the {@code undoAvailableProperty}
     */
    public SimpleBooleanProperty isUndoAvailableProperty() {
        return undoAvailable;
    }

    /**
     * Gets the {@code redoAvailableProperty}.
     *
     * @return
     *         the {@code redoAvailableProperty}
     */
    public SimpleBooleanProperty isRedoAvailableProperty() {
        return redoAvailable;
    }

}
