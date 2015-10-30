package de.uni_hannover.sra.minimax_simulator;

import de.uni_hannover.sra.minimax_simulator.config.ConfigurationLoader;
import de.uni_hannover.sra.minimax_simulator.config.PropertiesFileConfigLoader;
import de.uni_hannover.sra.minimax_simulator.ui.gui.FXMainController;
import de.uni_hannover.sra.minimax_simulator.model.user.Workspace;
import de.uni_hannover.sra.minimax_simulator.resources.DefaultResourceBundleLoader;
import de.uni_hannover.sra.minimax_simulator.resources.PropertyResourceControl;
import de.uni_hannover.sra.minimax_simulator.resources.ResourceBundleLoader;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
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

    private static Stage _primaryStage;

    private static Workspace _workspace;
    private static ResourceBundleLoader _resourceLoader;

    private static Version _version;

    private static Logger _log;

    private static HostServices hostServices;

    private static boolean _isDebugging;

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

        _primaryStage = primaryStage;
        _version = new Version(this.getClass());

        _isDebugging = System.getProperty("application.debug") != null;

        hostServices = getHostServices();

        // Read logger settings from .properties inside jar
        setupLogger();

        _log.info("Starting version " + _version.getVersionNumber());

        // check if Java 8u40 or higher is available
        if (_version.isJvmLower(1, 8, 0, 40)) {
            String jvmVersion = _version.getJvmMajor() + "." + _version.getJvmFeature() + "." + _version.getJvmUpdate() + "_" + _version.getJvmBuild();
            _log.severe("Java 1.8.0_40 or higher needed but found " + jvmVersion);
            System.exit(-1);
        }

        // Initialize config, read from file if existing
        try {
            new PropertiesFileConfigLoader(PropertiesFileConfigLoader.MissingConfigStrategy.USE_DEFAULT).configure(Config.class);
        } catch (ConfigurationLoader.ConfigurationException e) {
            throw new Error("Cannot initialize configuration", e);
        }
        _log.info("Configuration loaded.");

        // Initialize resource loader for clients (text boxes etc...)
        getResourceLoader();

        // Initialize empty workspace (no project loaded)
        _workspace = new Workspace();

        _log.info("Initializing UI...");

        URL location = getClass().getResource("/fxml/minimax-sim.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

        Parent root = fxmlLoader.load(location.openStream());
        Scene scene = new Scene(root, 1200, 705);
        scene.getStylesheets().add("css/application.css");
        _primaryStage.setScene(scene);

        FXMainController mainController = fxmlLoader.getController();
        _primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (!mainController.exitApplication()) {
                    event.consume();        // do not close the application
                }
            }
        });

        TextResource res = _resourceLoader.getTextResource("application");
        _primaryStage.setTitle(res.format("title", _version.getVersionNumber()));

        // set application icon
        _primaryStage.getIcons().add(new Image("images/nuvola/cpu.png"));
        _primaryStage.getIcons().add(new Image("images/nuvola/cpu-big.png"));

        _primaryStage.setResizable(true);
        _primaryStage.show();
    }

    /**
     * Gets the primary {@link Stage} of the application.
     *
     * @return
     *          the application's primary {@code Stage}
     */
    public static Stage getPrimaryStage() {
        return _primaryStage;
    }

    /**
     * Gets a list of all icons of the application.
     *
     * @return
     *          a list containing all icons of the application
     */
    public static ObservableList<Image> getAppIcon() {
        return _primaryStage.getIcons();
    }

    /**
     * Gets the {@link ResourceBundleLoader} of the application.<br>
     * If it does not already exist it will be initialized.
     *
     * @return
     *          the {@code ResourceBundleLoader} used by the application
     */
    public static ResourceBundleLoader getResourceLoader() {
        if (_resourceLoader == null) {
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
            _resourceLoader = new DefaultResourceBundleLoader(new PropertyResourceControl("text/"), locale);
        }
        return _resourceLoader;
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
        return _resourceLoader.getTextResource(bundleName);
    }

    public static boolean isDebugging() {
        return _isDebugging;
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
        return _workspace;
    }

    /**
     * Gets the version number.
     *
     * @return
     *      the version number as {@code String}
     */
    public static String getVersionString() {
        return _version.getVersionNumber();
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

        _log = Logger.getLogger(Main.class.getName());
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
