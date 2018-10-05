package de.uni_hannover.sra.minimax_simulator.config;

import de.uni_hannover.sra.minimax_simulator.io.IOUtils;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.uni_hannover.sra.minimax_simulator.config.Parser.toBoolean;

/**
 * This class holds the actual configuration information of the application using {@link Properties}.
 *
 * @author Philipp Rohde
 */
public class Config {

    /** application's properties */
    private static final Properties PROPERTIES = new Properties();
    /** properties file */
    private static final File PROPERTIES_FILE = new File("config.properties");

    /** string for getting the language property */
    private static final String LOCALE = "locale";

    /** string for getting the theme property */
    private static final String THEME = "theme";

    /** logger */
    private static final Logger LOG = Logger.getLogger("de.uni_hannover.sra.minimax_simulator");

    /** singleton instance */
    public static final Config INSTANCE = new Config();

    /**
     * Creates the singleton instance and reads the properties file.
     */
    private Config() {
        readPropertiesFile();
    }

    /**
     * Reads the properties file into {@link Properties} using {@link Properties#load(Reader)}.
     */
    private static void readPropertiesFile() {
        try {
            Reader r = IOUtils.toBufferedReader(new FileReader(PROPERTIES_FILE));

            PROPERTIES.load(r);

            r.close();
        } catch (FileNotFoundException e) {
            LOG.log(Level.FINE, "Configuration file does not exist.", e);
        } catch (IOException e) {
            LOG.log(Level.FINE, "Could not read from configuration file.", e);
        }
    }

    /**
     * Gets the language code of the language to use for localized texts.
     *
     * @return
     *         the language code of the language to use
     */
    public static String getLocale() {
        return PROPERTIES.getProperty(LOCALE, null);
    }

    /**
     * Changes the language code of the language to use for localized texts to the specified code.<br>
     * <b>Notice:</b> Changes take effect after restart.
     *
     * @param locale
     *         the new language code
     * @throws IOException
     *         thrown if the changes could not be saved to properties file
     */
    public static void changeLanguage(String locale) throws IOException {
        changeProperty(LOCALE, locale);
    }

    /**
     * Gets the name of the preferred theme.
     *
     * @return
     *         the theme's name
     */
    public static String getTheme() {
        return PROPERTIES.getProperty(THEME, "default");
    }

    /**
     * Changes the preferred theme.<br>
     * <b>Notice:</b> Changes take effect after restart.
     *
     * @param theme
     *         the name of the theme
     * @throws IOException
     *         thrown if the changes could not be saved to properties file
     */
    public static void changeTheme(String theme) throws IOException {
        changeProperty(THEME, theme);
    }

    /**
     * Gets the maximum length of a register name.<br>
     * <br>
     * Default value: 20
     *
     * @return
     *         the maximum length of a register name
     */
    public static int getEditorMaxRegisterLength() {
        return PROPERTIES.containsKey("editor.max-register-length") ? Integer.parseInt(PROPERTIES.getProperty("editor.max-register-length")) : 20;
    }

    /**
     * Gets the value of the {@code isDebugSchematics} property.<br>
     * <br>
     * Default value: false
     *
     * @return
     *         {@code true} if schematics should be debugged, {@code false} otherwise
     */
    public static boolean getIsDebugSchematics() {
        return PROPERTIES.containsKey("debug.schematics") && toBoolean(PROPERTIES.getProperty("debug.schematics"));
    }

    /**
     * Changes the value of specified property and saves the changes to the properties file.
     *
     * @param key
     *         the name of the property to update
     * @param value
     *         the new value of the property
     * @throws IOException
     *         thrown if the changes could not be saved to the properties file
     */
    private static void changeProperty(String key, String value) throws IOException {
        if (PROPERTIES.containsKey(key)) {
            PROPERTIES.replace(key, value);
        }
        else {
            PROPERTIES.put(key, value);
        }
        PROPERTIES.store(new FileWriter(PROPERTIES_FILE), null);
    }
}