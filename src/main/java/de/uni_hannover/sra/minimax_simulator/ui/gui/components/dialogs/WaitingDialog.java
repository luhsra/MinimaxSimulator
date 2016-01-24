package de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * A {@code WaitingDialog} is basically an {@link FXDialog}.<br>
 * It contains an indeterminate {@link ProgressBar} indicating that some {@link Runnable} is executed at the moment.
 *
 * @author Philipp Rohde
 */
public class WaitingDialog extends FXDialog {

    private final ButtonType btnTypeCancel;

    /**
     * Constructs a new {@code WaitingDialog} with the specified title and message.
     *
     * @param waitingTitle
     *          the title of the dialog
     * @param waitingMessage
     *          the message of the dialog
     */
    public WaitingDialog(String waitingTitle, String waitingMessage) {
        super(AlertType.NONE, waitingTitle, waitingMessage);

        TextResource res = Main.getTextResource("project").using("memory.update");

        btnTypeCancel = new ButtonType(res.get("cancel"), ButtonBar.ButtonData.OK_DONE);
        this.getButtonTypes().setAll(btnTypeCancel);

        ProgressBar pb = new ProgressBar(-1);
        pb.setPrefWidth(300.0);

        this.getDialogPane().setContent(pb);

        // set the result to btnTypeCancel if escape was pressed
        this.getDialogPane().getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                setResult(btnTypeCancel);
            }
        });
    }

    /**
     * Gets the user's choice.<br>
     * <br>
     * <b>Caution:</b><br>
     * This method is not supported for the {@code WaitingDialog}, use {@link #isCanceled()} instead.
     *
     * @return
     *          the chosen {@link ButtonType}
     */
    @Override
    public ButtonType getChoice() {
        throw new UnsupportedOperationException("Method getChoice() not supported for WaitingDialog, use isCanceled() instead.");
    }

    /**
     * Checks if the dialog was canceled or not.
     *
     * @return
     *          {@code true}, if the user closed the dialog, otherwise {@code false}
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
