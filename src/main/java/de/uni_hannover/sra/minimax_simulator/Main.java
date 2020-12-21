package de.uni_hannover.sra.minimax_simulator;

import de.uni_hannover.sra.minimax_simulator.config.Config;
import de.uni_hannover.sra.minimax_simulator.model.user.Workspace;
import de.uni_hannover.sra.minimax_simulator.resources.DefaultResourceBundleLoader;
import de.uni_hannover.sra.minimax_simulator.resources.PropertyResourceControl;
import de.uni_hannover.sra.minimax_simulator.resources.ResourceBundleLoader;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Wrapper to start the GUI or CLI.
 *
 * @author Philipp Rohde
 */
public class Main {

    /** object for loading resources */
    private static ResourceBundleLoader resourceLoader;

    /** logger */
    public static final Logger LOG = setupLogger();

    /** the {@link Workspace} used - initializes an empty workspace (no project loaded) */
    private static final Workspace workspace = new Workspace();

    /** version information */
    public static Version version;

    /**
     * Gets the {@link ResourceBundleLoader} of the application.<br>
     * If it does not already exist it will be initialized with the correct language.
     * The language setting of the JVM will be set to the same language.
     *
     * @return
     *          the {@code ResourceBundleLoader} used by the application
     */
    public static ResourceBundleLoader getResourceLoader() {
        if (resourceLoader == null) {
            Locale locale;
            if (Config.getLocale() == null) {
                locale = Locale.getDefault();
            }
            else {
                try {
                    locale = new Locale(Config.getLocale());
                    Locale.setDefault(locale);
                } catch (Exception e) {
                    // locale not supported
                    locale = Locale.getDefault();
                    LOG.log(Level.WARNING, "unsupported locale; fallback to default", e);
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

    /**
     * Initializes the application's logger.
     */
    private static Logger setupLogger() {
        final InputStream inputStream = MainGUI.class.getResourceAsStream("/logging.properties");

        try {
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (final IOException e) {
            throw new Error("Cannot initialize logging", e);
        }

        return Logger.getLogger(MainGUI.class.getName());
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
     * Gets the application up and running.
     *
     * @param args
     *          command line arguments
     */
    public static void main(String[] args) {
        version = new Version(Main.class);
        Main.LOG.info("Starting version " + version.getVersionNumber());

        // check if Java 11 or higher is available
        if (version.isJvmLower(11, 0, 0, 0)) {
            String jvmVersion = version.getJvmMajor() + "." + version.getJvmFeature() + "." + version.getJvmUpdate();
            Main.LOG.severe("Java 11.0.0 or higher needed but found " + jvmVersion);
            return;                 // shutting down application by returning from main method
        }

        // Initialize resource loader for clients (text boxes etc...)
        getResourceLoader();

        if (args.length > 0) {
            MainCLI.main(args);
        }
        else {
            MainGUI.main();
        }
    }
}
