package de.uni_hannover.sra.minimax_simulator.config;

import de.uni_hannover.sra.minimax_simulator.config.ConfigurationLoader.ConfigurationException;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the implementation of the {@link PropertiesFileConfigLoader}.
 *
 * @author Philipp Rohde
 */
public class PropertiesFileConfigLoaderTest {

    /** temporary folder */
    @ClassRule
    public static final TemporaryFolder TMP_DIR = new TemporaryFolder();

    private static File configFile;

    /**
     * Initializes the test class.
     *
     * @throws IOException
     *          thrown if the temporary file could not be created or written
     */
    @BeforeClass
    public static void initialize() throws IOException {
        configFile = TMP_DIR.newFile("config.properties");
        List<String> lines = Arrays.asList("locale=en", "timezone=GMT");
        Files.write(configFile.toPath(), lines, StandardCharsets.UTF_8);
    }

    /**
     * Tests {@link PropertiesFileConfigLoader#PropertiesFileConfigLoader()}.
     *
     * @throws ConfigurationException
     *          thrown if loading the configuration fails somehow
     */
    @Test
    public void testConstructorWithoutParameters() throws ConfigurationException {
        new PropertiesFileConfigLoader().configure(TestConfiguration.class);
        assertEquals("constructor without parameters: locale", "", TestConfiguration.LOCALE);
        assertTrue("constructor without parameters: unset", TestConfiguration.UNSET);
    }

    /**
     * Tests {@link PropertiesFileConfigLoader#PropertiesFileConfigLoader(PropertiesFileConfigLoader.MissingConfigStrategy)}
     * with {@link de.uni_hannover.sra.minimax_simulator.config.PropertiesFileConfigLoader.MissingConfigStrategy#USE_DEFAULT}.
     *
     * @throws ConfigurationException
     *          thrown if loading the configuration fails somehow
     */
    @Test
    public void testConstructorMissingConfigStrategy() throws ConfigurationException {
        new PropertiesFileConfigLoader(PropertiesFileConfigLoader.MissingConfigStrategy.USE_DEFAULT).configure(TestConfiguration.class);
        assertEquals("constructor missing config strategy: locale", "", TestConfiguration.LOCALE);
        assertTrue("constructor missing config strategy: unset", TestConfiguration.UNSET);
    }

    /**
     * Tests {@link PropertiesFileConfigLoader#PropertiesFileConfigLoader(PropertiesFileConfigLoader.MissingConfigStrategy)}
     * with {@link de.uni_hannover.sra.minimax_simulator.config.PropertiesFileConfigLoader.MissingConfigStrategy#THROW}.
     *
     * @throws ConfigurationException
     *          thrown because {@link TestConfiguration#UNSET} is not specified in configuration file
     */
    @Test(expected = ConfigurationException.class)
    public void testException() throws ConfigurationException {
        new PropertiesFileConfigLoader(PropertiesFileConfigLoader.MissingConfigStrategy.THROW).configure(TestConfiguration.class);
    }

    /**
     * Tests configuring a configuration class with the {@link ConfigurationFile} annotation.
     *
     * @throws ConfigurationException
     *          thrown if loading the configuration fails somehow
     */
    @Test
    public void testAnnotatedConfigFile() throws ConfigurationException {
        new PropertiesFileConfigLoader().configure(TestConfigurationAnnotatedFile.class);
        assertEquals("annotated config file: locale", "", TestConfiguration.LOCALE);
        assertTrue("annotated config file: unset", TestConfiguration.UNSET);
    }

    /**
     * Tests configuring with a not readable config file.
     *
     * @throws ConfigurationException
     *          thrown if loading the configuration fails somehow
     */
    @Test
    public void testNotReadableConfigFile() throws ConfigurationException {
        assertTrue(configFile.setReadable(false));
        try {
            new PropertiesFileConfigLoader(configFile, PropertiesFileConfigLoader.MissingConfigStrategy.THROW).configure(TestConfiguration.class);
            Assert.fail("expected to throw an exception");
        } catch (ConfigurationException e) {
            assertEquals("Configuration file not found: " + configFile.getPath(), e.getMessage());
        }

        new PropertiesFileConfigLoader(configFile).configure(TestConfiguration.class);
        assertEquals("not readable config file: locale", "", TestConfiguration.LOCALE);
        assertTrue("not readable config file: unset", TestConfiguration.UNSET);
    }

    /**
     * Tests configuring fields with wrong modifiers.<br>
     * Only <i>public, static and non-final</i> fields should work.
     */
    @Test
    public void testWrongModifiers() {
        PropertiesFileConfigLoader loader = new PropertiesFileConfigLoader();

        String confClass = "Configurable field of class ";
        String modifiers = " must be public, static and non-final: ";

        // private field
        try {
            loader.configure(TestConfigurationPrivateField.class);
        } catch (ConfigurationException e) {
            assertEquals("private field exception", confClass + TestConfigurationPrivateField.class.getName() + modifiers + "UNSET", e.getMessage());
        }

        // non-static field
        try {
            loader.configure(TestConfigurationNonStaticField.class);
        } catch (ConfigurationException e) {
            assertEquals("non-static field exception", confClass + TestConfigurationNonStaticField.class.getName() + modifiers + "UNSET", e.getMessage());
        }

        // final field
        try {
            loader.configure(TestConfigurationFinalField.class);
        } catch (ConfigurationException e) {
            assertEquals("final field exception", confClass + TestConfigurationFinalField.class.getName() + modifiers + "UNSET", e.getMessage());
        }
    }

    /**
     * Tests configuring enums and String arrays.
     *
     * @throws ConfigurationException
     *          thrown if loading the configuration fails somehow
     */
    @Test
    public void testDifferentFieldTypes() throws ConfigurationException {
        new PropertiesFileConfigLoader().configure(TestConfigurationDifferentFieldTypes.class);

        // array
        assertEquals("array size", 3, TestConfigurationDifferentFieldTypes.ARRAY.length);
        assertEquals("array[0]", "one", TestConfigurationDifferentFieldTypes.ARRAY[0]);
        assertEquals("array[1]", "two", TestConfigurationDifferentFieldTypes.ARRAY[1]);
        assertEquals("array[2]", "three", TestConfigurationDifferentFieldTypes.ARRAY[2]);

        // enum
        assertEquals("parsed enum", MuxType.A, TestConfigurationDifferentFieldTypes.MUX);
    }

    /**
     * This class is a modified version of {@link Config} for testing purposes.
     */
    private static class TestConfiguration {

        /** a string config entry for testing */
        @Configuration(key = "locale", defaultValue = "")
        public static String LOCALE;

        /** a boolean config entry for testing */
        @Configuration(key = "unset", defaultValue = "true")
        public static boolean UNSET;
    }

    /**
     * This class is a modified version of {@link Config} for testing purposes
     * with annotated configuration file.
     */
    @ConfigurationFile(file = "config.properties")
    private static class TestConfigurationAnnotatedFile {

        /** a string config entry for testing */
        @Configuration(key = "locale", defaultValue = "")
        public static String LOCALE;

        /** a boolean config entry for testing */
        @Configuration(key = "unset", defaultValue = "true")
        public static boolean UNSET;
    }

    /**
     * This class is a modified version of {@link Config} for testing purposes
     * with private fields.
     */
    private static class TestConfigurationPrivateField {

        /** a private boolean config entry for testing */
        @Configuration(key = "unset", defaultValue = "true")
        private static boolean UNSET;
    }

    /**
     * This class is a modified version of {@link Config} for testing purposes
     * with non-static fields.
     */
    private static class TestConfigurationNonStaticField {

        /** a non-static boolean config entry for testing */
        @Configuration(key = "unset", defaultValue = "true")
        public boolean UNSET;
    }

    /**
     * This class is a modified version of {@link Config} for testing purposes
     * with final fields.
     */
    private static class TestConfigurationFinalField {

        /** a final boolean config entry for testing */
        @Configuration(key = "unset", defaultValue = "true")
        public static final boolean UNSET = true;
    }

    /**
     * This class is a modified version of {@link Config} for testing purposes
     * of different field types.
     */
    private static class TestConfigurationDifferentFieldTypes {

        /** a string array config entry for testing */
        @Configuration(key = "array", defaultValue = "one,two,three")
        public static String[] ARRAY;

        /** an enum config entry for testing */
        @Configuration(key = "mux", defaultValue = "A")
        public static MuxType MUX;
    }
}
