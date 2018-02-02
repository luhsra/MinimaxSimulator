package de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import javafx.scene.control.ButtonType;


/**
 * A {@code SimulationRunningDialog} is basically an {@link FXDialog} with customized content.<br>
 * It is used to ask the user whether a running simulation should be stop so changes can be made safely.
 *
 * @author Philipp Rohde
 */
public class SimulationRunningDialog extends FXDialog {

    /**
     * Constructs a new {@code SimulationRunningDialog}
     */
    public SimulationRunningDialog() {
        super(AlertType.WARNING, null, null);

        TextResource res = Main.getTextResource("debugger").using("simulation.running");
        this.setTitle(res.get("title"));
        this.setContentText(res.get("message"));

        this.getDialogPane().getButtonTypes().clear();
        this.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.CANCEL);
    }

}
