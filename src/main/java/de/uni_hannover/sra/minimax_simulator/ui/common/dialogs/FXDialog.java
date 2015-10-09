package de.uni_hannover.sra.minimax_simulator.ui.common.dialogs;

import de.uni_hannover.sra.minimax_simulator.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * An FXDialog is basically an {@link Alert}.<br>
 * It makes it more comfortable to set up a new dialog and get the user's choice.
 *
 * @author Philipp Rohde
 */
public class FXDialog extends Alert {

    public FXDialog(AlertType alertType, String title, String message) {
        super(alertType);
        this.setTitle(title);
        this.setHeaderText(null);
        this.setContentText(message);

        // for setting the icon of the application to the dialog
        this.initOwner(Main.getPrimaryStage());
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
