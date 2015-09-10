package de.uni_hannover.sra.minimax_simulator;

import de.uni_hannover.sra.minimax_simulator.config.ConfigurationLoader;
import de.uni_hannover.sra.minimax_simulator.config.PropertiesFileConfigLoader;
import de.uni_hannover.sra.minimax_simulator.gui.FXMainController;
import de.uni_hannover.sra.minimax_simulator.model.user.Workspace;
import de.uni_hannover.sra.minimax_simulator.resources.DefaultResourceBundleLoader;
import de.uni_hannover.sra.minimax_simulator.resources.PropertyResourceControl;
import de.uni_hannover.sra.minimax_simulator.resources.ResourceBundleLoader;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.Locale;

/**
 * The main class of the simulator.
 *
 * @author Philipp Rohde
 */
// TODO: logging
public class Main extends javafx.application.Application {

    private static Stage _primaryStage;

    private static Workspace _workspace;
    private static ResourceBundleLoader _resourceLoader;

    private static Version _version;


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

        _primaryStage.setResizable(false);
        _primaryStage.show();
    }

    /**
     * Gets the primary {@link Stage} of the application.
     *
     * @return
     *          the applications primary {@link Stage}
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
     * Gets the {@link ResourceBundleLoader} of the application.
     * If it does not already exist it will be initialized.
     *
     * @return
     *          the {@link ResourceBundleLoader} used by the application
     */
    public static ResourceBundleLoader getResourceLoader()
    {
        if (_resourceLoader == null)
        {
            Locale locale = new Locale.Builder().setLanguage("en").setRegion("US").build();
/*            if (Config.LOCALE == null || Config.LOCALE.isEmpty())
            {
                locale = Locale.getDefault();
            }
            else
            {
                try
                {
                    locale = new Locale(Config.LOCALE);
                }
                catch (Exception e)
                {
                    // locale not supported
                    locale = Locale.getDefault();
                }
            }           */
            System.out.println("Locale:" + locale);
            _resourceLoader = new DefaultResourceBundleLoader(
                    new PropertyResourceControl("text/"), locale);
        }
        return _resourceLoader;
    }

    public static TextResource getTextResource(String bundleName)
    {
        return _resourceLoader.getTextResource(bundleName);
    }

    /**
     * The main method of the application. It initializes the configuration, {@link ResourceBundleLoader} and {@link Workspace}
     * before launching the JavaFX application.
     *
     * @param args
     *          the command line arguments
     */
    public static void main(String[] args) {
        // Initialize config, read from file if existing
        try
        {
            new PropertiesFileConfigLoader(PropertiesFileConfigLoader.MissingConfigStrategy.USE_DEFAULT).configure(Config.class);
        }
        catch (ConfigurationLoader.ConfigurationException e)
        {
            throw new Error("Cannot initialize configuration", e);
        }

        // Initialize resource loader for clients (text boxes etc...)
        getResourceLoader();
        _workspace = new Workspace();

        launch(args);
    }

    /**
     * Gets the {@link Workspace} used by the application.
     *
     * @return
     *      the application's {@link Workspace}
     */
    public static Workspace getWorkspace() {
        return _workspace;
    }

    /**
     * Gets the version number.
     *
     * @return
     *      the version number as {@link String}
     */
    public static String getVersionString() {
        return _version.getVersionNumber();
    }
}
