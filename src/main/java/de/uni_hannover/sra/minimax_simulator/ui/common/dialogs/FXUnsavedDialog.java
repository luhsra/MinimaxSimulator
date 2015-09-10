package de.uni_hannover.sra.minimax_simulator.ui.common.dialogs;

import javafx.scene.control.ButtonType;

/**
 * An FXUnsavedDialog is basically an {@link javafx.scene.control.Alert}.<br>
 * It is used to ask the user for confirmation to dismiss unsaved changes.
 *
 * @author Philipp Rohde
 */
public class FXUnsavedDialog extends FXDialog {

    public FXUnsavedDialog(String title, String message) {
        super(AlertType.WARNING, title, message);
        this.getDialogPane().getButtonTypes().clear();
        this.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
    }
}
