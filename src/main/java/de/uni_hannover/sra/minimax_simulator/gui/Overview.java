package de.uni_hannover.sra.minimax_simulator.gui;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.MachineSchematics;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane;

/**
 * <b>FXController of the overview tab</b><br>
 * <br>
 * This controller handles every GUI interaction with the overview {@link Tab}.
 *
 * @author Philipp Rohde
 */
// TODO: do this at FXMainController?
public class Overview {

    @FXML private ScrollPane paneSchematics;
    private MachineSchematics schematics;

    /**
     * This method is called from the main controller if a new project was created or opened.
     * It initializes the {@link MachineSchematics} and binds the {@code translateXProperty} of it with the
     * {@code widthProperty} of the {@link ScrollPane} for being centered.
     */
    public void initOverview() {
        schematics = new MachineSchematics(Main.getWorkspace().getProject().getMachine());
        schematics.translateXProperty().bind(paneSchematics.widthProperty().subtract(schematics.widthProperty()).divide(2));
        paneSchematics.setContent(schematics);
    }
}
