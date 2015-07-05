package de.uni_hannover.sra.minimax_simulator.ui.common.dialogs;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.Version;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * The FXAboutDialog is basically an {@link javafx.scene.control.Alert}.
 * It provides all information about the application such as version and authors.
 * The information is read from the MANIFEST.MF.
 *
 * @see Version
 *
 * @author Philipp Rohde
 */
public class FXAboutDialog extends FXDialog {

    private final ButtonType btnTypeOK;
    private final TextResource _res;
    private final Version ver = new Version(Main.class);        // works only with JAR files

    public FXAboutDialog() {
        super(AlertType.NONE, "Info", null);
        _res = Main.getTextResource("application").using("info");

        btnTypeOK = new ButtonType(_res.get("ok"), ButtonBar.ButtonData.OK_DONE);
        this.getButtonTypes().setAll(btnTypeOK);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(20);

        // set up the logos
        Image luh = new Image("images/luh/luh.png");
        Image sra = new Image("images/luh/sra.png");

        ImageView luhIV = new ImageView();
        luhIV.setImage(luh);
        luhIV.setPreserveRatio(true);
        luhIV.setFitHeight(70);

        ImageView sraIV = new ImageView();
        sraIV.setImage(sra);
        sraIV.setPreserveRatio(true);
        sraIV.setFitHeight(70);

        // create the description
        Label description = new Label("GUI-based Minimax Simulator");
        description.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // create application information labels
        VBox vb = new VBox();
        vb.setPadding(new Insets(15, 0, 15, 0));
        vb.setSpacing(5);
        Label version = new Label("Version: " + ver.getVersionNumber());
        Label build = new Label(_res.format("build", ver.getBuildTime() , ver.getBuildJdk()));
        Label authors = new Label(_res.format("author", ver.getAuthorName()));
        vb.getChildren().addAll(version, build, authors);

        // create university label
        Label university = new Label("Leibniz Universität Hannover, Institut für Systems Engineering, FG System- und Rechnerarchitektur");
        university.setPadding(new Insets(0, 15, 0, 0));

        // putting all together
        grid.add(sraIV, 0, 0);
        grid.setHalignment(luhIV, HPos.RIGHT);
        grid.add(luhIV, 1, 0);
        grid.add(description, 0, 1, 2, 1);
        grid.setHalignment(description, HPos.CENTER);
        grid.add(vb, 0, 2, 2, 1);
        grid.add(university, 0, 3, 2, 1);
        this.getDialogPane().setContent(grid);
    }

}
