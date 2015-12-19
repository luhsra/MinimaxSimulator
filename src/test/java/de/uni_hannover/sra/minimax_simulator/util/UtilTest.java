package de.uni_hannover.sra.minimax_simulator.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the functionality of the miscellaneous utility methods provided by {@link Util}.
 *
 * @author Philipp Rohde
 */
public class UtilTest {

    /**
     * Tests the implementation of {@link Util#toBinaryAddress(int, int)}.
     */
    @Test
    public void testToBinaryAddress() {
        Object[][] params = {{0, 1, "0"}, {0, 2, "00"}, {10, 32, "001010"}, {32, 32, "100000"}};

        for (Object[] param : params) {
            int index = (int) param[0];
            int maxAddress = (int) param[1];
            String expected = (String) param[2];

            assertEquals("binary address of " + index + " and max address " + maxAddress, expected, Util.toBinaryAddress(index, maxAddress));
        }
    }

    /**
     * Tests the implementation of {@link Util#createHexFormatString(int, boolean)}.
     */
    @Test
    public void testCreateHexFormatString() {
        Object[][] params = {{4, "%01X"}, {8, "%02X"}, {16, "%04X"}, {17, "%05X"}};

        for (Object[] param : params) {
            int width = (int) param[0];
            String expected = (String) param[1];

            assertEquals("hex format string for word width " + width, expected, Util.createHexFormatString(width, false));
            assertEquals("hex format string for word width " + width + " with prefix", "0x" + expected, Util.createHexFormatString(width, true));
        }
    }

    /**
     * Tests the implementation of {@link Util#toHex(int, int, boolean)}.
     */
    @Test
    public void toHex() {
        Object[][] params = {{3, 4, "3"}, {12, 4, "C"}, {12, 8, "0C"}, {32, 16, "0020"}, {4096, 16, "1000"}, {65535, 16, "FFFF"}};

        for (Object[] param : params) {
            int value = (int) param[0];
            int width = (int) param[1];
            String expected = (String) param[2];

            assertEquals("hex value of " + value, expected, Util.toHex(value, width, false));
            assertEquals("hex value of " + value + " with prefix", "0x" + expected, Util.toHex(value, width, true));
        }
    }
}
