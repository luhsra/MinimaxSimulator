package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo;

/**
 * A class that needs to react to actions of the {@link UndoManager}.
 *
 * @author Philipp Rohde
 */
public interface UndoListener {

    /**
     * Handles an action performed by the {@code UndoManager}.
     *
     * @param e
     *         the {@link UndoEvent} that happend
     */
    public void onUndoAction(UndoEvent e);

}
