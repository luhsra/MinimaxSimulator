package de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs;

import javafx.scene.control.ButtonType;

/**
 * An {@code UnsavedDialog} is basically an {@link FXDialog}.<br>
 * It is used to ask the user for confirmation to dismiss unsaved changes.
 *
 * @author Philipp Rohde
 */
public class UnsavedDialog extends FXDialog {

    /**
     * Constructs a new {@code UnsavedDialog} with the specified title and message.
     *
     * @param title
     *          the title of the dialog
     * @param message
     *          the message of the dialog
     */
    public UnsavedDialog(String title, String message) {
        super(AlertType.WARNING, title, message);
        this.getDialogPane().getButtonTypes().clear();
        this.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
    }
}
