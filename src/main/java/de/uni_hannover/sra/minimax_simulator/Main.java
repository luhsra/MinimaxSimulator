package de.uni_hannover.sra.minimax_simulator;

import de.uni_hannover.sra.minimax_simulator.config.ConfigurationLoader;
import de.uni_hannover.sra.minimax_simulator.config.PropertiesFileConfigLoader;
import de.uni_hannover.sra.minimax_simulator.model.user.Workspace;
import de.uni_hannover.sra.minimax_simulator.resources.DefaultResourceBundleLoader;
import de.uni_hannover.sra.minimax_simulator.resources.PropertyResourceControl;
import de.uni_hannover.sra.minimax_simulator.resources.ResourceBundleLoader;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.gui.FXMainController;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * The main class of the simulator. It initializes and launches the JavaFX application
 * and provides some static methods needed by several classes.
 *
 * @author Philipp Rohde
 */
/* TODO: DebugMenu is isDebugging with
         DebugException: throw new RuntimeException("Test exception")
         DebugSprites: toggle Config.DEBUG_SCHEMATICS
*/
public class Main extends javafx.application.Application {

    private static Stage primaryStage;

    private static Workspace workspace;
    private static ResourceBundleLoader resourceLoader;

    private static Version version;

    private static Logger log;

    private static HostServices hostServices;

    private static boolean isDebugging;

    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage
     *          the primary {@link Stage} of the JavaFX application
     * @throws Exception
     *          thrown if an error occurs during start up
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        Main.primaryStage = primaryStage;
        version = new Version(this.getClass());

        isDebugging = System.getProperty("application.debug") != null;

        hostServices = getHostServices();

        // Read logger settings from .properties inside jar
        setupLogger();

        log.info("Starting version " + version.getVersionNumber());

        // check if Java 8u40 or higher is available
        if (version.isJvmLower(1, 8, 0, 40)) {
            String jvmVersion = version.getJvmMajor() + "." + version.getJvmFeature() + "." + version.getJvmUpdate() + "_" + version.getJvmBuild();
            log.severe("Java 1.8.0_40 or higher needed but found " + jvmVersion);
            System.exit(-1);
        }

        // Initialize config, read from file if existing
        try {
            new PropertiesFileConfigLoader(PropertiesFileConfigLoader.MissingConfigStrategy.USE_DEFAULT).configure(Config.class);
        } catch (ConfigurationLoader.ConfigurationException e) {
            throw new Error("Cannot initialize configuration", e);
        }
        log.info("Configuration loaded.");

        // Initialize resource loader for clients (text boxes etc...)
        getResourceLoader();

        // Initialize empty workspace (no project loaded)
        workspace = new Workspace();

        log.info("Initializing UI...");

        URL location = getClass().getResource("/fxml/minimax-sim.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

        Parent root = fxmlLoader.load(location.openStream());
        Scene scene = new Scene(root, 1200, 705);
        scene.getStylesheets().add("css/application.css");
        Main.primaryStage.setScene(scene);

        FXMainController mainController = fxmlLoader.getController();
        Main.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (!mainController.exitApplication()) {
                    event.consume();        // do not close the application
                }
            }
        });

        TextResource res = resourceLoader.getTextResource("application");
        Main.primaryStage.setTitle(res.format("title", version.getVersionNumber()));

        // set application icon
        Main.primaryStage.getIcons().add(new Image("images/nuvola/cpu.png"));
        Main.primaryStage.getIcons().add(new Image("images/nuvola/cpu-big.png"));

        Main.primaryStage.setResizable(true);
        Main.primaryStage.show();
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
     * Gets the {@link ResourceBundleLoader} of the application.<br>
     * If it does not already exist it will be initialized.
     *
     * @return
     *          the {@code ResourceBundleLoader} used by the application
     */
    public static ResourceBundleLoader getResourceLoader() {
        if (resourceLoader == null) {
            Locale locale;
            if (Config.LOCALE == null || Config.LOCALE.isEmpty()) {
                locale = Locale.getDefault();
            }
            else {
                try {
                    locale = new Locale(Config.LOCALE);
                } catch (Exception e) {
                    // locale not supported
                    locale = Locale.getDefault();
                }
            }
            resourceLoader = new DefaultResourceBundleLoader(new PropertyResourceControl("text/"), locale);
        }
        return resourceLoader;
    }

    /**
     * Gets the {@link TextResource} with the specified bundle name.
     *
     * @param bundleName
     *          the name of the resource bundle
     * @return
     *          the {@code TextResource} with the specified bundle name
     */
    public static TextResource getTextResource(String bundleName) {
        return resourceLoader.getTextResource(bundleName);
    }

    public static boolean isDebugging() {
        return isDebugging;
    }

    /**
     * Calls {@link Main#launch(String...)} for launching the JavaFX application.
     *
     * @param args
     *          the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Gets the {@link Workspace} used by the application.
     *
     * @return
     *      the application's {@code Workspace}
     */
    public static Workspace getWorkspace() {
        return workspace;
    }

    /**
     * Gets the version number.
     *
     * @return
     *      the version number as {@code String}
     */
    public static String getVersionString() {
        return version.getVersionNumber();
    }

    /**
     * Initializes the application's logger.
     */
    private static void setupLogger() {
        final InputStream inputStream = Main.class.getResourceAsStream("/logging.properties");

        try {
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (final IOException e) {
            throw new Error("Cannot initialize logging", e);
        }

        log = Logger.getLogger(Main.class.getName());
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
