package de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs;

import de.uni_hannover.sra.minimax_simulator.MainGUI;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * An {@code FXDialog} is basically an {@link Alert}.<br>
 * It makes it more comfortable to set up a new dialog and get the user's choice.
 *
 * @author Philipp Rohde
 */
public class FXDialog extends Alert {

    /**
     * Creates a dialog with the specified {@link javafx.scene.control.Alert.AlertType} and
     * sets the title and message of the dialog.
     *
     * @param alertType
     *          the {@code AlertType} of the dialog
     * @param title
     *          the title of the dialog
     * @param message
     *          the message of the dialog
     */
    public FXDialog(AlertType alertType, String title, String message) {
        super(alertType);
        this.setTitle(title);
        this.setHeaderText(null);
        this.setContentText(message);

        // for setting the icon of the application to the dialog
        this.initOwner(MainGUI.getPrimaryStage());
    }

    /**
     * Gets the user's choice.
     *
     * @return
     *          the chosen {@link ButtonType}
     */
    public ButtonType getChoice() {
        Optional<ButtonType> result = this.showAndWait();
        return result.get();
    }

}
