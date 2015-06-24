package de.uni_hannover.sra.minimax_simulator.ui.common.dialogs;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Created by philipp on 24.06.15.
 */
public class FxWaitingDialog extends FxDialog {

    private final ButtonType btnTypeCancel;

    public FxWaitingDialog(String waitingTitle, String waitingMessage) {
        super(AlertType.NONE, waitingTitle, waitingMessage);

        btnTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.OK_DONE);
        this.getButtonTypes().setAll(btnTypeCancel);

        ProgressBar pb = new ProgressBar(-1);
        pb.setPrefWidth(300.0);

        this.getDialogPane().setContent(pb);
        this.setTitle(waitingTitle);
        this.setContentText(waitingMessage);
    }

    @Override
    public ButtonType getChoice() {
        throw new UnsupportedOperationException("Method getChoice() not supported for FxWaitingDialog, use isCanceled() instead.");
    }

    public boolean isCanceled() {
        Optional<ButtonType> result = this.showAndWait();
        try {
            ButtonType bt = result.get();
            if (bt == btnTypeCancel) {
                return true;
            }
        } catch (NoSuchElementException e) {
            // result.get() throws this exception if the dialog is closed via the close method
        }
        return false;
    }
}
