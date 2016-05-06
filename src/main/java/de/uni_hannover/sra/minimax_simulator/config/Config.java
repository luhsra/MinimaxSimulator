package de.uni_hannover.sra.minimax_simulator.config;

import de.uni_hannover.sra.minimax_simulator.io.IOUtils;

import java.io.*;
import java.util.Properties;

import static de.uni_hannover.sra.minimax_simulator.config.Parser.toBoolean;

/**
 * This class holds the actual configuration information of the application using {@link Properties}.
 *
 * @author Philipp Rohde
 */
public class Config {

    private static final Properties PROPERTIES = new Properties();
    private static final File PROPERTIES_FILE = new File("config.properties");

    /** The singleton instance. */
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

            //r.close();
        } catch (FileNotFoundException e) {
            // do nothing here because it is possible that no configuration file exists
        } catch (IOException e) {
            // do nothing and use default values if the properties file was not readable or malformed
        }
    }

    /**
     * Gets the language code of the language to use for localized texts.
     *
     * @return
     *         the language code of the language to use
     */
    public static String getLocale() {
        return PROPERTIES.getProperty("locale", null);
    }

    /**
     * Changes the language code of the language to use for localized texts to the specified code.<br>
     * <b>Notice:</b> Changes take effect after restart.
     *
     * @param locale
     *         the new language code
     * @throws IOException
     *         thrown if the changes could not be saved to PROPERTIES file
     */
    public static void changeLanguage(String locale) throws IOException {
        readPropertiesFile();
        if (PROPERTIES.containsKey("locale")) {
            PROPERTIES.replace("locale", locale);
        }
        else {
            PROPERTIES.put("locale", locale);
        }
        PROPERTIES.store(new FileWriter(PROPERTIES_FILE), null);
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
     *         {@code true} if schematics should be deugged, {@code false} otherwise
     */
    public static boolean getIsDebugSchematics() {
        return PROPERTIES.containsKey("debug.schematics") && toBoolean(PROPERTIES.getProperty("debug.schematics"));
    }
}