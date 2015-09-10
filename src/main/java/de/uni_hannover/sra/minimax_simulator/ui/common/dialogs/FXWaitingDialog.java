package de.uni_hannover.sra.minimax_simulator.ui.common.dialogs;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * An FXWaitingDialog is basically an {@link javafx.scene.control.Alert}.<br>
 * It contains an indeterminate {@link ProgressBar} indicating that some {@link Runnable} is executed at the moment.
 *
 * @author Philipp Rohde
 */
public class FXWaitingDialog extends FXDialog {

    private final ButtonType btnTypeCancel;

    public FXWaitingDialog(String waitingTitle, String waitingMessage) {
        super(AlertType.NONE, waitingTitle, waitingMessage);

        TextResource res = Main.getTextResource("project").using("memory.update");

        btnTypeCancel = new ButtonType(res.get("cancel"), ButtonBar.ButtonData.OK_DONE);
        this.getButtonTypes().setAll(btnTypeCancel);

        ProgressBar pb = new ProgressBar(-1);
        pb.setPrefWidth(300.0);

        this.getDialogPane().setContent(pb);
    }

    /**
     * Gets the user's choice.<br>
     * <br>
     * <b>Caution:</b><br>
     * This method is not supported for the FXWaitingDialog, use {@link #isCanceled()} instead.
     *
     * @return
     *          the chosen {@link ButtonType}
     */
    @Override
    public ButtonType getChoice() {
        throw new UnsupportedOperationException("Method getChoice() not supported for FXWaitingDialog, use isCanceled() instead.");
    }

    /**
     * Checks if the dialog was canceled or not.
     *
     * @return
     *          <code>true</code>, if the user closed the dialog, otherwise <code>false</code>
     */
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
