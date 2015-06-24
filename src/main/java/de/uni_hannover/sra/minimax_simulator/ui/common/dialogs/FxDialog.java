package de.uni_hannover.sra.minimax_simulator.ui.common.dialogs;

import de.uni_hannover.sra.minimax_simulator.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Created by philipp on 24.06.15.
 */
public class FxDialog extends Alert {

    public FxDialog(AlertType alertType, String title, String message) {
        super(alertType);
        this.setTitle(title);
        this.setHeaderText(null);
        this.setContentText(message);

        // for setting the icon of the application to the dialog
        this.initOwner(Main.getPrimaryStage());
    }

    public ButtonType getChoice() {
        Optional<ButtonType> result = this.showAndWait();
        return result.get();
    }

}
