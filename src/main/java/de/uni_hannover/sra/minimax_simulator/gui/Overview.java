package de.uni_hannover.sra.minimax_simulator.gui;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.MachineDisplayListener;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.MachineSchematics;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.*;
import javafx.scene.image.Image;

/**
 * <b>FXController of the overview tab</b><br>
 * <br>
 * This controller handles every GUI interaction with the overview {@link Tab}.
 *
 * @author Philipp Rohde
 */
public class Overview implements MachineDisplayListener {

    @FXML private ScrollPane paneSchematics;
    @FXML private ImageView imgSchematics;

    /**
     * This method is called from the main controller if a new project was created or opened.
     * It initializes the {@link ImageView} and binds the {@code translateXProperty} of it with the
     * {@code widthProperty} of the {@link ScrollPane} for being centered.
     */
    public void initOverview() {
        imgSchematics.translateXProperty().bind(paneSchematics.widthProperty().subtract(imgSchematics.fitWidthProperty()).divide(2));

        Main.getWorkspace().getProject().getMachine().getDisplay().addMachineDisplayListener(this);
        schematicsToImage();
    }

    /**
     * Creates an {@link Image} of the {@link MachineSchematics} using {@link javafx.scene.canvas.Canvas#snapshot(SnapshotParameters, WritableImage)}
     * and sets the {@code Image} to the {@link ImageView}.
     */
    private void schematicsToImage() {
        MachineSchematics mSchematics = new MachineSchematics(Main.getWorkspace().getProject().getMachine());
        Image image = mSchematics.snapshot(null, null);
        imgSchematics.setFitHeight(image.getHeight());
        imgSchematics.setFitWidth(image.getWidth());
        imgSchematics.setImage(image);
    }

    @Override
    public void machineSizeChanged() {
        schematicsToImage();
    }

    @Override
    public void machineDisplayChanged() {
        schematicsToImage();
    }

    @Override
    public void onSpriteOwnerAdded(SpriteOwner spriteOwner) {
        // not needed here
    }

    @Override
    public void onSpriteOwnerRemoved(SpriteOwner spriteOwner) {
        // not needed here
    }

    @Override
    public void onSpriteOwnerChanged(SpriteOwner spriteOwner) {
        // not needed here
    }
}
