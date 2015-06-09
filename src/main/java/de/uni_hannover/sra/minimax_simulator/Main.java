package de.uni_hannover.sra.minimax_simulator;

import de.uni_hannover.sra.minimax_simulator.model.user.Workspace;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * The main class of the simulator.
 *
 * @author Philipp
 *
 */

public class Main extends javafx.application.Application {

    static final FXMLLoader loader = new FXMLLoader();
    static final private String version = "JavaFX Alpha 1";

    private static Workspace _workspace;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/minimax-sim.fxml"));
        primaryStage.setTitle("Minimax-Simulator ("+version+")");

        // set application icon
        primaryStage.getIcons().add(new Image("/images/nuvola/cpu.png"));
        primaryStage.getIcons().add(new Image("/images/nuvola/cpu-big.png"));

        Scene scene = new Scene(root, 1200, 675);
        scene.getStylesheets().add("css/application.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        _workspace = new Workspace();
        launch(args);
    }

    public static Workspace getWorkspace()
    {
        return _workspace;
    }
}
