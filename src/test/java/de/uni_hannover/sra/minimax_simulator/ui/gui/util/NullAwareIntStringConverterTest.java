package de.uni_hannover.sra.minimax_simulator.ui.gui.util;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests the implementation of {@link NullAwareIntStringConverter}.
 *
 * @author Philipp Rohde
 */
@RunWith(Enclosed.class)
public class NullAwareIntStringConverterTest {

    /**
     * Runs the conversion tests using parameterized tests.
     */
    @RunWith(Parameterized.class)
    public static class ParameterizedTest {

        private final Integer dec;
        private final String hex;
        private final String bin;

        /**
         * Initializes the test instance.
         *
         * @param dec
         *         the decimal value as Integer
         * @param hex
         *         the hexadecimal value as String
         * @param bin
         *         the binary value as String
         */
        public ParameterizedTest(Integer dec, String hex, String bin) {
            this.dec = dec;
            this.hex = hex;
            this.bin = bin;
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
                    {15, "F", "1111"},
                    {-3, "FFFFFFFD", "11111111111111111111111111111101"},
                    {0, "0", "0"},
                    {9, "9", "1001"},
                    {10, "A", "1010"},
                    {Integer.MAX_VALUE, "7FFFFFFF", "1111111111111111111111111111111"},
                    {Integer.MAX_VALUE-1, "7FFFFFFE", "1111111111111111111111111111110"},
                    {Integer.MIN_VALUE, "80000000", "10000000000000000000000000000000"},
                    {Integer.MIN_VALUE+1, "80000001", "10000000000000000000000000000001"}
            });
        }

        /**
         * Tests the converter using decimal numbers.
         */
        @Test
        public void testDecimal() {
            NullAwareIntStringConverter converter = new NullAwareIntStringConverter();
            assertEquals("decimal to string", String.valueOf(dec), converter.toString(dec));
            assertEquals("decimal from string", dec, converter.fromString(String.valueOf(dec)));
        }

        /**
         * Tests the converter using binary numbers.
         */
        @Test
        public void testBinary() {
            NullAwareIntStringConverter converter = new NullAwareIntStringConverter(2, false);
            assertEquals("binary to string", bin, converter.toString(dec));
            assertEquals("binary from string", dec, converter.fromString(String.valueOf(bin)));
        }

        /**
         * Tests the converter using hexadecimal numbers.
         */
        @Test
        public void testHexadecimal() {
            NullAwareIntStringConverter converter = new NullAwareIntStringConverter(16, false);
            assertEquals("hex to string", hex, converter.toString(dec));
            assertEquals("hex from string", dec, converter.fromString(String.valueOf(hex)));
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
            NullAwareIntStringConverter converter = new NullAwareIntStringConverter();
            assertEquals("null value", "", converter.toString(null));
            assertNull("null text", converter.fromString(null));
            assertNull("empty text", converter.fromString(""));
            assertNull("NumberFormatException", converter.fromString("MyNameIs..."));
        }
    }
}
