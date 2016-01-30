package de.uni_hannover.sra.minimax_simulator.config;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
        Files.write(configFile.toPath(), lines, Charset.forName("UTF-8"));
    }

    /**
     * Tests {@link PropertiesFileConfigLoader#PropertiesFileConfigLoader()}.
     *
     * @throws ConfigurationLoader.ConfigurationException
     *          thrown if loading the configuration fails somehow
     */
    @Test
    public void testConstructorWithoutParameters() throws ConfigurationLoader.ConfigurationException {
        new PropertiesFileConfigLoader().configure(TestConfiguration.class);
        assertEquals("constructor without parameters: locale", "", TestConfiguration.LOCALE);
        assertEquals("constructor without parameters: unset", true, TestConfiguration.UNSET);
    }

    /**
     * Tests {@link PropertiesFileConfigLoader#PropertiesFileConfigLoader(PropertiesFileConfigLoader.MissingConfigStrategy)}
     * with {@link de.uni_hannover.sra.minimax_simulator.config.PropertiesFileConfigLoader.MissingConfigStrategy#USE_DEFAULT}.
     *
     * @throws ConfigurationLoader.ConfigurationException
     *          thrown if loading the configuration fails somehow
     */
    @Test
    public void testConstructorMissingConfigStrategy() throws ConfigurationLoader.ConfigurationException {
        new PropertiesFileConfigLoader(PropertiesFileConfigLoader.MissingConfigStrategy.USE_DEFAULT).configure(TestConfiguration.class);
        assertEquals("constructor without parameters: locale", "", TestConfiguration.LOCALE);
        assertEquals("constructor without parameters: unset", true, TestConfiguration.UNSET);
    }

    /**
     * Tests {@link PropertiesFileConfigLoader#PropertiesFileConfigLoader(PropertiesFileConfigLoader.MissingConfigStrategy)}
     * with {@link de.uni_hannover.sra.minimax_simulator.config.PropertiesFileConfigLoader.MissingConfigStrategy#THROW}.
     *
     * @throws ConfigurationLoader.ConfigurationException
     *          thrown because {@link TestConfiguration#UNSET} is not specified in configuration file
     */
    @Test(expected = ConfigurationLoader.ConfigurationException.class)
    public void testException() throws ConfigurationLoader.ConfigurationException {
        new PropertiesFileConfigLoader(PropertiesFileConfigLoader.MissingConfigStrategy.THROW).configure(TestConfiguration.class);
    }

    /**
     * Tests configuring a configuration class with the {@link ConfigurationFile} annotation.
     *
     * @throws ConfigurationLoader.ConfigurationException
     *          thrown if loading the configuration fails somehow
     */
    @Test
    public void testAnnotatedConfigFile() throws ConfigurationLoader.ConfigurationException {
        new PropertiesFileConfigLoader().configure(TestConfigurationAnnotatedFile.class);
        assertEquals("constructor without parameters: locale", "", TestConfiguration.LOCALE);
        assertEquals("constructor without parameters: unset", true, TestConfiguration.UNSET);
    }

    /**
     * Tests configuring with a not readable config file.
     *
     * @throws ConfigurationLoader.ConfigurationException
     *          thrown if loading the configuration fails somehow
     */
    @Test
    public void testNotReadableConfigFile() throws ConfigurationLoader.ConfigurationException {
        assertEquals(true, configFile.setReadable(false));
        try {
            new PropertiesFileConfigLoader(configFile, PropertiesFileConfigLoader.MissingConfigStrategy.THROW).configure(TestConfiguration.class);
            Assert.fail("expected to throw an exception");
        } catch (ConfigurationLoader.ConfigurationException e) {
            assertEquals("Configuration file not found: " + configFile.getPath(), e.getMessage());
        }

        new PropertiesFileConfigLoader(configFile).configure(TestConfiguration.class);
        assertEquals("constructor without parameters: locale", "", TestConfiguration.LOCALE);
        assertEquals("constructor without parameters: unset", true, TestConfiguration.UNSET);
    }

    /**
     * This class is a modified version of {@link de.uni_hannover.sra.minimax_simulator.Config} for testing purposes.
     */
    private static class TestConfiguration {

        /** a string config entry for testing */
        @Configuration(key = "locale", defaultValue = "")
        public static String LOCALE;

        @Configuration(key = "unset", defaultValue = "true")
        public static boolean UNSET;
    }

    /**
     * This class is a modified version of {@link de.uni_hannover.sra.minimax_simulator.Config} for testing purposes
     * with annotated configuration file.
     */
    @ConfigurationFile(file = "config.properties")
    private static class TestConfigurationAnnotatedFile {
        /** a string config entry for testing */
        @Configuration(key = "locale", defaultValue = "")
        public static String LOCALE;

        @Configuration(key = "unset", defaultValue = "true")
        public static boolean UNSET;
    }

}
