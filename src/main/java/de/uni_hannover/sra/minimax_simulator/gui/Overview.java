package de.uni_hannover.sra.minimax_simulator.gui;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.MachineDisplayListener;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.MachineSchematics;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.*;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <b>FXController of the overview tab</b><br>
 * <br>
 * This controller handles every GUI interaction with the overview {@link Tab}.
 *
 * @author Philipp Rohde
 */
// TODO: find a better way
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
     * Creates a {@link BufferedImage} of the {@link MachineSchematics} and uses {@code Streams} to set it to the {@link ImageView}.
     */
    private void schematicsToImage() {
        // paint the machine schematics to a buffered image
        MachineSchematics mSchematics = new MachineSchematics(Main.getWorkspace().getProject().getMachine());
        Dimension dim = mSchematics.getPreferredSize();
        mSchematics.setSize(dim);
        BufferedImage image = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.createGraphics();
        mSchematics.paint(g);
        g.dispose();

        // set size of the image view to fit the image
        imgSchematics.setFitHeight(dim.getHeight());
        imgSchematics.setFitWidth(dim.getWidth());

        // set the buffered image to the image view
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "png", os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            imgSchematics.setImage(new Image(is));
        } catch (IOException e) {
            // catch only
        }
    }

    @Override
    public void machineSizeChanged() {
        schematicsToImage();
    }

    @Override
    public void machineDisplayChanged() {
        // not needed here
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
