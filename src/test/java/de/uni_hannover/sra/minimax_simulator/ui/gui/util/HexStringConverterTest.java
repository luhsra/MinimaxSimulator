package de.uni_hannover.sra.minimax_simulator.ui.gui.util;

import de.uni_hannover.sra.minimax_simulator.util.Util;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests the implementation of {@link HexStringConverter}.
 *
 * @author Philipp Rohde
 */
@RunWith(Enclosed.class)
public class HexStringConverterTest {

    /**
     * Runs the conversion tests using parameterized tests.
     */
    @RunWith(Parameterized.class)
    public static class ParameterizedTest {

        private final Integer dec;
        private final String hex;
        private final String format;
        private final String formattedHex;

        /**
         * Initializes the test instance
         *
         * @param dec
         *         the decimal value as Integer
         * @param hex
         *         the hexadecimal value as String
         * @param format
         *         the format string
         */
        public ParameterizedTest(Integer dec, String hex, String format, String formattedHex) {
            this.dec = dec;
            this.hex = hex;
            this.format = format;
            this.formattedHex = formattedHex;
        }

        /**
         * Creates the parameters for the test.
         *
         * @return
         *          the parameters for the test
         */
        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    {15, "F", Util.createHexFormatString(8, true), "0x0F"},
                    {0, "0", Util.createHexFormatString(4, false), "0"},
                    {-3, "FFFFFFFD", Util.createHexFormatString(32, true), "0xFFFFFFFD"},
                    {9, "9", "", "9"},
                    {10, "A", Util.createHexFormatString(12, false), "00A"},
                    {Integer.MAX_VALUE, "7FFFFFFF", Util.createHexFormatString(32, true), "0x7FFFFFFF"},
                    {Integer.MAX_VALUE-1, "7FFFFFFE", Util.createHexFormatString(32, true), "0x7FFFFFFE"},
                    {Integer.MIN_VALUE, "80000000", Util.createHexFormatString(32, true), "0x80000000"},
                    {Integer.MIN_VALUE+1, "80000001", Util.createHexFormatString(32, true), "0x80000001"}
            });
        }

        /**
         * Tests the converter using decimal numbers.
         */
        @Test
        public void testWithFormatString() {
            HexStringConverter converter = new HexStringConverter(Integer.MIN_VALUE, Integer.MAX_VALUE, format);
            assertEquals("toString with format string", formattedHex, converter.toString(dec));
            assertEquals("fromString with format string", dec, converter.fromString(formattedHex));
        }

        /**
         * Tests the converter using binary numbers.
         */
        @Test
        public void testWithoutFormatString() {
            HexStringConverter converter = new HexStringConverter(Integer.MIN_VALUE, Integer.MAX_VALUE);
            assertEquals("toString", hex, converter.toString(dec));
            assertEquals("fromString", dec, converter.fromString(hex));
        }
    }

    /**
     * Runs tests of the error handling.
     */
    public static class ErrorHandling {

        /**
         * Actually runs the test.
         */
        @Test
        public void testErrorHandling() {
            HexStringConverter converter = new HexStringConverter(0, 100);
            assertEquals("null value", "", converter.toString(null));
            assertNull("null text", converter.fromString(null));
            assertNull("empty text", converter.fromString(""));
            assertNull("NumberFormatException", converter.fromString("MyNameIs..."));

            // input out of bounds
            assertEquals("value lower than min", Integer.valueOf(0), converter.fromString("-3"));
            assertEquals("value higher than max", Integer.valueOf(100), converter.fromString("235"));
        }
    }
}
