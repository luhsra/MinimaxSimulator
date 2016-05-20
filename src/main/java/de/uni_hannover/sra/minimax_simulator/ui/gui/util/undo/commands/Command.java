package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;

/**
 * Atomic user action which can be undone and redone.
 *
 * @author Philipp Rohde
 */
public abstract class Command {

    private boolean marked = false;
    private final String name;

    /**
     * Super constructor for all {@code Command} classes to set the command name.
     *
     * @param nameKey
     *         the resource key of the command name (without project.command)
     */
    protected Command(String nameKey) {
        TextResource res = Main.getTextResource("menu");
        this.name = res.using("project.command").get(nameKey);
    }

    /**
     * Executes the command.
     */
    public abstract void execute();

    /**
     * Undoes the changes made by the command.
     */
    public abstract void undo();

    /**
     * Redoes the changes made by the command.<br>
     * <br>
     * The default is a call to {@link Command#execute()}.
     */
    public void redo() {
        this.execute();
    }

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

    /**
     * Gets the name of the {@code Command}.
     *
     * @return
     *         the {@code Command}'s name
     */
    public String getCommandName() {
        return this.name;
    }
}
