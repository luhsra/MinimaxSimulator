package de.uni_hannover.sra.minimax_simulator.config;

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

import static org.junit.Assert.*;

/**
 * Tests the implementation of the classes for loading and setting the configurations.
 *
 * @author Philipp Rohde
 */
public class ConfigurationTest {

    /** temporary folder */
    @ClassRule
    public static final TemporaryFolder TMP_DIR = new TemporaryFolder();

    private static final String EXPECTED_EXCEPTION = "expected to throw exception";

    /**
     * Initializes the test class.
     *
     * @throws IOException
     *          thrown if the temporary file could not be created or written
     * @throws ConfigurationLoader.ConfigurationException
     *          thrown if the configuration file could not be loaded
     */
    @BeforeClass
    public static void initialize() throws IOException, ConfigurationLoader.ConfigurationException {
        File configFile = TMP_DIR.newFile("config.properties");
        writeConfig(configFile);
        new PropertiesFileConfigLoader(configFile).configure(TestConfig.class);
    }

    /**
     * Writes the configuration file entries needed for the test cases.
     *
     * @param configFile
     *          the {@code File} for the configuration entries
     * @throws IOException
     *          thrown if the entries could not be written to the file
     */
    private static void writeConfig(File configFile) throws IOException {
        List<String> lines = Arrays.asList("string=de",
                "boolean=true",
                "integer=128",
                "long=248",
                "short=2048",
                "byteobj=110",
                "char=c",
                "float=0.0001",
                "double=1.01",
                "obj=Object@453535",
                "byte=512B",
                "kilo=2kB",
                "mega=3MB",
                "giga=4GB",
                "size=33");
        Files.write(configFile.toPath(), lines, StandardCharsets.UTF_8);
    }

    /**
     * Tests configuration entries parsed with {@link Parser#auto(String, Class)}.
     */
    @Test
    public void testAuto() {
        assertEquals("string property", "de", TestConfig.STRING);

        assertTrue("boolean property", TestConfig.BOOL_PRIM);
        assertEquals("Boolean property", Boolean.TRUE, TestConfig.BOOL);

        assertEquals("integer property", 128, TestConfig.INTEGER_PRIM);
        assertEquals("Integer property", Integer.valueOf(128), TestConfig.INTEGER);

        assertEquals("long property", 248, TestConfig.LONG_PRIM);
        assertEquals("Long property", Long.valueOf(248), TestConfig.LONG);

        assertEquals("short property", 2048, TestConfig.SHORT_PRIM);
        assertEquals("Short property", Short.valueOf("2048"), TestConfig.SHORT);

        assertEquals("byte property", Byte.parseByte("110"), TestConfig.BYTE_PRIM);
        assertEquals("Byte property", Byte.valueOf("110"), TestConfig.BYTE);

        assertEquals("char property", "c".charAt(0), TestConfig.CHAR_PRIM);
        assertEquals("Char property", (Character) "c".charAt(0), TestConfig.CHAR);

        assertEquals("float property", 0.0001f, TestConfig.FLOAT_PRIM, 0.00000000001);
        assertEquals("Float property", Float.parseFloat("0.0001"), TestConfig.FLOAT, 0.00000000001);

        assertEquals("double property", 1.01, TestConfig.DOUBLE_PRIM, 0.00000000001);
        assertEquals("Double property", Double.parseDouble("1.01"), TestConfig.DOUBLE, 0.00000000001);

        assertNull("object property", TestConfig.OBJ);
    }

    /**
     * Tests configuration entries parsed with {@link Parser#toFileSize(String)}.
     */
    @Test
    public void testToFileSize() {
        assertEquals("byte property", 512, TestConfig.BYTE_SIZE);
        assertEquals("kilo property", 2048, TestConfig.KILO);
        assertEquals("mega property", 3145728, TestConfig.MEGA);
        assertEquals("giga property", Long.parseLong("4294967296"), TestConfig.GIGA);

        assertEquals("size without unit property", 33, TestConfig.SIZE);

        // invalid input
        String value = "noNumbersHere";
        try {
            Parser.toFileSize(value);
        } catch (IllegalArgumentException e) {
            assertEquals("illegal size format", "Wrong format for file size for config: " + value, e.getMessage());
        }

        // invalid unit
        String invalidUnit = "234TB";
        try {
            Parser.toFileSize(invalidUnit);
            Assert.fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("Wrong format for file size for config: " + invalidUnit, e.getMessage());
        }
    }

    /**
     * Tests parts of {@link Parser#toByte(String)} not covered by other tests.
     */
    @Test
    public void testToByte() {
        // null input
        try {
            Parser.toByte(null);
            Assert.fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("Missing Byte attribute", e.getMessage());
        }

        // invalid input
        String input = "123ffs2";
        try {
            Parser.toByte(input);
            Assert.fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("Wrong Byte attribute: " + input, e.getMessage());
        }
    }

    /**
     * Tests parts of {@link Parser#toChar(String)} not covered by other tests.
     */
    @Test
    public void testToChar() {
        // null input
        try {
            Parser.toChar(null);
            Assert.fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("Missing Char attribute", e.getMessage());
        }

        // invalid input
        String input = "aadf";
        try {
            Parser.toChar(input);
            Assert.fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("Wrong Char attribute (size-mismatch): " + input, e.getMessage());
        }
    }

    /**
     * Tests parts of {@link Parser#toShort(String)} not covered by other tests.
     */
    @Test
    public void testToShort() {
        // null input
        try {
            Parser.toShort(null);
            Assert.fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("Missing Short attribute", e.getMessage());
        }

        // invalid input
        String input = "3fg";
        try {
            Parser.toShort(input);
            Assert.fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("Wrong Short attribute: " + input, e.getMessage());
        }
    }

    /**
     * Tests parts of {@link Parser#toInteger(String)} not covered by other tests.
     */
    @Test
    public void testToInteger() {
        // null input
        try {
            Parser.toInteger(null);
            Assert.fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("Missing Integer attribute", e.getMessage());
        }

        // invalid input
        String input = "7h6fd33";
        try {
            Parser.toInteger(input);
            Assert.fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("Wrong Integer attribute: " + input, e.getMessage());
        }
    }

    /**
     * Tests parts of {@link Parser#toLong(String)} not covered by other tests.
     */
    @Test
    public void testToLong() {
        // null input
        try {
            Parser.toLong(null);
            Assert.fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("Missing Long attribute", e.getMessage());
        }

        // invalid input
        String input = "ju4";
        try {
            Parser.toLong(input);
            Assert.fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("Wrong Long attribute: " + input, e.getMessage());
        }
    }

    /**
     * Tests parts of {@link Parser#toFloat(String)} not covered by other tests.
     */
    @Test
    public void testToFloat() {
        // null input
        try {
            Parser.toFloat(null);
            Assert.fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("Missing Float attribute", e.getMessage());
        }

        // invalid input
        String input = "1.78m5";
        try {
            Parser.toFloat(input);
            Assert.fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("Wrong Float attribute: " + input, e.getMessage());
        }
    }

    /**
     * Tests parts of {@link Parser#toDouble(String)} not covered by other tests.
     */
    @Test
    public void testToDouble() {
        // null input
        try {
            Parser.toDouble(null);
            Assert.fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("Missing Double attribute", e.getMessage());
        }

        // invalid input
        String input = "99.5647,3";
        try {
            Parser.toDouble(input);
            Assert.fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("Wrong Double attribute: " + input, e.getMessage());
        }
    }

    /**
     * Tests parts of {@link Parser#toBoolean(String)} not covered by other tests.
     */
    @Test
    public void testToBoolean() {
        // null input
        try {
            Parser.toBoolean(null);
            Assert.fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("Missing Boolean attribute", e.getMessage());
        }

        // invalid inputs
        String invalid = "I'm no boolean!";
        try {
            Parser.toBoolean(invalid);
            Assert.fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("Wrong Boolean attribute: " + invalid.toLowerCase(), e.getMessage());
        }

        // 1 || yes
        assertTrue("toBoolean(1)", Parser.toBoolean("1"));
        assertTrue("toBoolean(yes)", Parser.toBoolean("yes"));

        // false || 0 || no
        assertFalse("toBoolean(false)", Parser.toBoolean("false"));
        assertFalse("toBoolean(0)", Parser.toBoolean("0"));
        assertFalse("toBoolean(no)", Parser.toBoolean("no"));
    }

    /**
     * Tests the implementation of {@link Parser#toStrings(String, String)}.
     */
    @Test
    public void testToStrings() {
        // null input
        try {
            Parser.toStrings(null, "");
            Assert.fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("Missing String[] attribute", e.getMessage());
        }

        String input = "Line1,Line2,Line3";
        String[] expected = {"Line1", "Line2", "Line3"};
        String[] actual = Parser.toStrings(input, ",");
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }

        assertEquals(input, Parser.toStrings(input, ";")[0]);
    }

    /**
     * Tests the implementation of {@link Parser#toEnum(String, Class)}.
     */
    @Test
    public void testToEnum() {
        assertEquals("muxType A", MuxType.A, Parser.toEnum("A", MuxType.class));
        assertEquals("muxType B", MuxType.B, Parser.toEnum("B", MuxType.class));

        try {
            Parser.toEnum("C", MuxType.class);
            Assert.fail(EXPECTED_EXCEPTION);
        } catch (IllegalArgumentException e) {
            assertEquals("Enum value \"C\" not found in class: " + MuxType.class.getSimpleName(), e.getMessage());
        }
    }

    /**
     * This class is a modified version of {@link Config} for testing purposes.
     */
    private static class TestConfig {
        /** a string config entry */
        @Configuration(key = "string", defaultValue = "")
        public static String STRING;

        /** a boolean config entry */
        @Configuration(key = "boolean", defaultValue = "false")
        public static boolean BOOL_PRIM;

        /** a Boolean config entry */
        @Configuration(key = "boolean", defaultValue = "false")
        public static Boolean BOOL;

        /** an integer config entry */
        @Configuration(key = "integer", defaultValue = "2")
        public static int INTEGER_PRIM;

        /** an Integer config entry */
        @Configuration(key = "integer", defaultValue = "2")
        public static Integer INTEGER;

        /** a long config entry */
        @Configuration(key = "long", defaultValue = "3")
        public static long LONG_PRIM;

        /** a Long config entry */
        @Configuration(key = "long", defaultValue = "3")
        public static Long LONG;

        /** a short config entry */
        @Configuration(key = "short", defaultValue = "4")
        public static short SHORT_PRIM;

        /** a Short config entry */
        @Configuration(key = "short", defaultValue = "4")
        public static Short SHORT;

        /** a byte config entry */
        @Configuration(key = "byteobj", defaultValue = "110")
        public static byte BYTE_PRIM;

        /** a Byte config entry */
        @Configuration(key = "byteobj", defaultValue = "110")
        public static Byte BYTE;

        /** a char config entry */
        @Configuration(key = "char", defaultValue = "a")
        public static char CHAR_PRIM;

        /** a Char config entry */
        @Configuration(key = "char", defaultValue = "a")
        public static Character CHAR;

        /** a float config entry */
        @Configuration(key = "float", defaultValue = "1.0")
        public static float FLOAT_PRIM;

        /** a Float config entry */
        @Configuration(key = "float", defaultValue = "1.0")
        public static Float FLOAT;

        /** a double config entry */
        @Configuration(key = "double", defaultValue = "0.0")
        public static double DOUBLE_PRIM;

        /** a Double config entry */
        @Configuration(key = "double", defaultValue = "0.0")
        public static Double DOUBLE;

        /** an Object config entry */
        @Configuration(key = "obj", defaultValue = "")
        public static Object OBJ;

        /** a file size config entry in bytes */
        @Configuration(key = "byte", defaultValue = "512B", type = FieldType.BYTESIZE)
        public static long BYTE_SIZE;

        /** a file size config entry in kilo bytes */
        @Configuration(key = "kilo", defaultValue = "123kB", type = FieldType.BYTESIZE)
        public static long KILO;

        /** a file size config entry in mega bytes */
        @Configuration(key = "mega", defaultValue = "5MB", type = FieldType.BYTESIZE)
        public static long MEGA;

        /** a file size config entry in giga bytes */
        @Configuration(key = "giga", defaultValue = "1GB", type = FieldType.BYTESIZE)
        public static long GIGA;

        /** a file size config entry without unit */
        @Configuration(key = "size", defaultValue = "4", type = FieldType.BYTESIZE)
        public static long SIZE;
    }
}
