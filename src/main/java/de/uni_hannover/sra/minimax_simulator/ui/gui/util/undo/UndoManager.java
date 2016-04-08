package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo;

import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands.Command;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.List;

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

    private List<Command> commands;
    private int currentPosition;

    /** The singleton instance. */
    public static final UndoManager INSTANCE = new UndoManager();

    /**
     * Initializes the instance.
     */
    private UndoManager() {
        undoAvailable = new SimpleBooleanProperty(false);
        redoAvailable = new SimpleBooleanProperty(false);

        commands = new ArrayList<>();
        currentPosition = -1;
    }

    /**
     * Adds the specified {@code Command} to the list of performed actions and performs the action.
     *
     * @param command
     *         the {@code Command} to add
     */
    public void addCommand(Command command) {
        if (currentPosition < commands.size() - 1) {
            redoAvailable.set(false);
            for (int i = commands.size() - 1; i > currentPosition; i--) {
                commands.remove(i);
            }
        }

        commands.add(command);
        currentPosition++;
        undoAvailable.set(true);

        command.execute();
    }

    /**
     * Undoes the latest action if there is one.
     */
    public void undo() {
        if (!undoAvailable.get()) {
            return;
        }

        commands.get(currentPosition).undo();
        currentPosition--;
        redoAvailable.set(true);

        if (currentPosition < 0) {
            undoAvailable.set(false);
        }
    }

    /**
     * Redoes the latest action if there is one.
     */
    public void redo() {
        if (!redoAvailable.get()) {
            return;
        }

        commands.get(currentPosition+1).redo();
        currentPosition++;
        undoAvailable.set(true);

        if (currentPosition >= commands.size()-1) {
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
