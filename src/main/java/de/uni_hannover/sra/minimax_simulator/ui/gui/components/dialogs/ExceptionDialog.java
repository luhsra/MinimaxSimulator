package de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * The {@code ExceptionDialog} is a dialog for providing information about an {@code Exception} to the user.<br>
 * It shows the {@code Exception}'s class and localized message. The {@code Exception} stack trace is the
 * expandable content of the dialog.
 *
 * @author Philipp Rohde
 */
public class ExceptionDialog extends FXDialog {

    /**
     * Constructs a new {@code ExceptionDialog} for the specified {@link Exception}.
     *
     * @param e
     *          the {@code Exception} to display
     */
    public ExceptionDialog(Exception e) {
        super(AlertType.ERROR, "title", "message");

        TextResource res = Main.getTextResource("exception").using("exception-dialog");
        this.setTitle(res.get("title"));

        // create dialog content
        VBox vbox = new VBox();
        vbox.setSpacing(10.0);
        Label message = new Label(res.get("message"));
        Label cause = new Label(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
        cause.setStyle("-fx-font-weight: bold");

        vbox.getChildren().addAll(message, cause);
        this.getDialogPane().setContent(vbox);

        // create expandable Exception
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label stacktrace = new Label(res.get("stacktrace"));

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(stacktrace, 0, 0);
        expContent.add(textArea, 0, 1);

        // set expandable Exception into the dialog pane
        this.getDialogPane().setExpandableContent(expContent);
    }
}
