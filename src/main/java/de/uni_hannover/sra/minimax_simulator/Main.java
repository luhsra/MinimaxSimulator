package de.uni_hannover.sra.minimax_simulator;

import de.uni_hannover.sra.minimax_simulator.config.ConfigurationLoader;
import de.uni_hannover.sra.minimax_simulator.config.PropertiesFileConfigLoader;
import de.uni_hannover.sra.minimax_simulator.model.user.Workspace;
import de.uni_hannover.sra.minimax_simulator.resources.DefaultResourceBundleLoader;
import de.uni_hannover.sra.minimax_simulator.resources.PropertyResourceControl;
import de.uni_hannover.sra.minimax_simulator.resources.ResourceBundleLoader;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Locale;

/**
 * The main class of the simulator.
 *
 * @author Philipp
 *
 */
// TODO: logging
public class Main extends javafx.application.Application {

    static final FXMLLoader loader = new FXMLLoader();
    static final private String version = "JavaFX Alpha 1";

    private static Stage _primaryStage;

    private static Workspace _workspace;
    private static ResourceBundleLoader _resourceLoader;


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/minimax-sim.fxml"));
        _primaryStage = primaryStage;
        _primaryStage.setTitle("Minimax-Simulator ("+version+")");

        // set application icon
        _primaryStage.getIcons().add(new Image("/images/nuvola/cpu.png"));
        _primaryStage.getIcons().add(new Image("/images/nuvola/cpu-big.png"));

        Scene scene = new Scene(root, 1200, 675);
        scene.getStylesheets().add("css/application.css");
        _primaryStage.setScene(scene);
        _primaryStage.setResizable(false);
        _primaryStage.show();
    }

    public static Stage getPrimaryStage() {
        return _primaryStage;
    }

    public static ObservableList<Image> getAppIcon() {
        return _primaryStage.getIcons();
    }

    public static ResourceBundleLoader getResourceLoader()
    {
        if (_resourceLoader == null)
        {
            Locale locale = new Locale.Builder().setLanguage("en").setRegion("US").build();;
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
                    new PropertyResourceControl("./text/"), locale);
        }
        return _resourceLoader;
    }

    public static TextResource getTextResource(String bundleName)
    {
        return _resourceLoader.getTextResource(bundleName);
    }

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

    public static Workspace getWorkspace()
    {
        return _workspace;
    }
}
