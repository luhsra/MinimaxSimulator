package de.uni_hannover.sra.minimax_simulator;

import de.uni_hannover.sra.minimax_simulator.config.Configuration;
import de.uni_hannover.sra.minimax_simulator.config.ConfigurationFile;
import de.uni_hannover.sra.minimax_simulator.io.IOUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

/**
 * This class holds the actual configuration information of the application.
 *
 * @see Configuration
 * @see ConfigurationFile
 *
 * @author Martin L&uuml;ck
 */
@ConfigurationFile(file = "config.properties")
public class Config {

    /** The language key of the language to use for localized texts. */
    @Configuration(key = "locale", defaultValue = "")
    public static String LOCALE;

    /** The initial editor slider position. */
    @Configuration(key = "editor.slider-position", defaultValue = "200")
    public static int EDITOR_SLIDER_POSITION;

    /** The initial value of the {@code expanded} property of the ALU. */
    @Configuration(key = "editor.alu.expanded", defaultValue = "true")
    public static boolean EDITOR_ALU_EXPANDED;

    /** The initial value of the {@code expanded} property of the registers. */
    @Configuration(key = "editor.register.expanded", defaultValue = "true")
    public static boolean EDITOR_REGISTER_EXPANDED;

    /** The initial value of the {@code expanded} property of the memory. */
    @Configuration(key = "editor.memory.expanded", defaultValue = "false")
    public static boolean EDITOR_MEMORY_EXPANDED;

    /** The maximum length of a register name. */
    @Configuration(key = "editor.max-register-length", defaultValue = "20")
    public static int EDITOR_MAX_REGISTER_LENGTH;

    /** Enable/disable the schematics debugging. */
    @Configuration(key = "debug.schematics", defaultValue = "false")
    public static boolean DEBUG_SCHEMATICS;

    private static Properties properties = new Properties();
    private static final File PROPERTIES_FILE = new File(Config.class.getAnnotation(ConfigurationFile.class).file());

    /**
     * Prevents instance creation of this utility class.
     */
    private Config() {

    }

    private static void readPropertiesFile() {
        try {
            Reader r = IOUtils.toBufferedReader(new FileReader(PROPERTIES_FILE));

            properties.load(r);

            r.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changeLanguage(String locale) throws IOException {
        readPropertiesFile();
        if (properties.containsKey("locale")) {
            properties.replace("locale", locale);
        }
        else {
            properties.put("locale", locale);
        }
        properties.store(new FileWriter(PROPERTIES_FILE), null);
    }

/*
    public static void changeLanguage(String locale) {
        File file = new File("config.properties");
        System.out.println(file.exists());
        try {
            Scanner scanner = new Scanner(file);
            Map<String, String> configurations = new HashMap<>();

            //now read the file line by line...
            int lineNum = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("=", 2);
                configurations.put(parts[0], parts[1]);

                System.out.println("LIne: " + lineNum + "\t" + line);
                lineNum++;
                if(line.startsWith("locale=")) {
                    System.out.println("ho hum, i found it on line " +lineNum);
                }
            }
            scanner.close();

            if (configurations.containsKey("locale")) {
                configurations.replace("locale", locale);
            }
            else {
                configurations.put("locale", locale);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }       */
}