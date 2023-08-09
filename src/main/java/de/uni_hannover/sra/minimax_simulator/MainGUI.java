package de.uni_hannover.sra.minimax_simulator;

import de.uni_hannover.sra.minimax_simulator.config.Config;
import de.uni_hannover.sra.minimax_simulator.config.ConfigurationLoader;
import de.uni_hannover.sra.minimax_simulator.config.PropertiesFileConfigLoader;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.gui.FXMainController;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;

/**
 * The main class of the simulator. It initializes and launches the JavaFX application
 * and provides some static methods needed by several classes.
 *
 * @author Philipp Rohde
 */
public class MainGUI extends javafx.application.Application {

    /** the primary {@link Stage} of the JavaFX application */
    private static Stage primaryStage;

    /** {@link HostServices} for opening files with system default application */
    private static HostServices hostServices;

    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage
     *          the primary {@link Stage} of the JavaFX application
     * @throws Exception
     *          thrown if an error occurs during start up
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        MainGUI.primaryStage = primaryStage;
        hostServices = getHostServices();

        // Initialize config, read from file if existing
        try {
            new PropertiesFileConfigLoader(PropertiesFileConfigLoader.MissingConfigStrategy.USE_DEFAULT).configure(Config.class);
        } catch (ConfigurationLoader.ConfigurationException e) {
            Main.LOG.severe("Can not initialize configuration!");
            throw e;
        }
        Main.LOG.info("Configuration loaded.");

        Main.LOG.info("Initializing UI...");
        URL location = getClass().getResource("/fxml/minimax-sim.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

        Parent root = fxmlLoader.load(location.openStream());
        Scene scene = new Scene(root, 1200, 705);
        //scene.getStylesheets().add("css/application.css");
        scene.getStylesheets().add("css/" + Config.getTheme() + ".css");
        MainGUI.primaryStage.setScene(scene);

        FXMainController mainController = fxmlLoader.getController();
        MainGUI.primaryStage.setOnCloseRequest((WindowEvent event) -> {
            if (!mainController.exitApplication()) {
                event.consume();            // do not close the application
            }
        });

        TextResource res = Main.getTextResource("application");
        MainGUI.primaryStage.setTitle(res.format("title", Main.version.getVersionNumber()));

        // set application icon
        MainGUI.primaryStage.getIcons().add(new Image("images/nuvola/cpu.png"));
        MainGUI.primaryStage.getIcons().add(new Image("images/nuvola/cpu-big.png"));

        MainGUI.primaryStage.setResizable(true);
        MainGUI.primaryStage.show();
    }

    /**
     * Gets the primary {@link Stage} of the application.
     *
     * @return
     *          the application's primary {@code Stage}
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Gets a list of all icons of the application.
     *
     * @return
     *          a list containing all icons of the application
     */
    public static ObservableList<Image> getAppIcon() {
        return primaryStage.getIcons();
    }

    /**
     * Calls {@link MainGUI#launch(String...)} for launching the JavaFX application.
     */
    public static void main() {
        launch();
    }

    /**
     * Gets the {@link HostServices} of the application.<br>
     * This method is meant to be a static version of {@link Application#getHostServices()}.
     *
     * @return
     *          the {@code HostServices} of the application
     */
    public static HostServices getHostServicesStatic() {
        return hostServices;
    }

}
