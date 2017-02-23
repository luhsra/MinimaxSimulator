package de.uni_hannover.sra.minimax_simulator.util;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Provides miscellaneous utility methods.
 *
 * @author Martin L&uuml;ck
 */
public class Util {

    /**
     * Prevents instance creation of this utility class.
     */
    private Util() {

    }

    /**
     * Returns the specified {@code index} as a binary string.<br>
     * The string is padded with zeros to the left such that its length would equal the minimal
     * length of the binary string representing {@code maxIndex}.<br>
     * Both integers are interpreted as unsigned values, therefore negative {@code int} values
     * will always assume a width of 32 characters.
     *
     * @param index
     *          the integer to convert
     * @param maxIndex
     *          the maximum integer value
     * @return
     *          the binary representation of {@code index} with the width of {@code maxIndex}
     */
    public static String toBinaryAddress(int index, int maxIndex) {
        int digits = Math.max(32 - Integer.numberOfLeadingZeros(maxIndex), 1);

        char[] buffer = new char[digits];
        int mask = 1;
        for (int i = digits - 1; i >= 0; i--) {
            buffer[i] = (index & mask) != 0 ? '1' : '0';
            mask <<= 1;
        }

        return new String(buffer);
    }

    /**
     * Returns the specified {@code value} as a 32 bit binary string.
     *
     * @param value
     *          the integer to convert
     * @return
     *          the 32 bit binary representation of {@code value}
     */
    public static String to32BitBinary(int value) {
        String bin = toBinaryAddress(value, -1);       // returns binary string of 32 bit width
        return bin.substring(0, 8) + " " + bin.substring(8, 16) + " " + bin.substring(16, 24) + " " + bin.substring(24, 32);
    }

    /**
     * Returns a format string for use with the {@link String#format(String, Object...)} method.<br>
     * <br>
     * The created format will accept a single {@code int} parameter which will be
     * formatted as an uppercase hexadecimal word.<br>
     * <br>
     * The {@code wordWidth} parameter, which may be any non-negative number,
     * determines the width of the represented word in bits.<br>
     * <br>
     * The hexadecimal values produced by the formatter will always consist of
     * {@code (wordWidth / 4)} hexadecimal letters, where {@code /} is a
     * division operator that rounds up.
     *
     * @param wordWidth
     *          the word width in bits
     * @param useHexPrefix
     *          whether the hex prefix ("0x") should be appended before the hexadecimal value or not
     * @return
     *          a format string
     */
    public static String createHexFormatString(int wordWidth, boolean useHexPrefix) {
        checkArgument(wordWidth >= 0);

        StringBuilder sb = useHexPrefix ? new StringBuilder("0x") : new StringBuilder();

        // divide by 4 because of 4 bits per hex digit
        sb.append("%0").append(((wordWidth - 1) >> 2) + 1).append('X');

        return sb.toString();
    }

    /**
     * Formats a single integer to a hexadecimal representation of a word
     * with fixed width.<br>
     * <br>
     * The {@code wordWidth} parameter, which may be any non-negative number,
     * determines the width of the represented word in bits.<br>
     * <br>
     * The hexadecimal representation will consist of
     * {@code (wordWidth / 4)} hexadecimal letters, where {@code /} is a
     * division operator that rounds up.
     *
     * @param value
     *          the integer word to represent in hexadecimal
     * @param wordWidth
     *          the word width in bits
     * @param useHexPrefix
     *          whether the hex prefix ("0x") should be appended before the hexadecimal value or not
     * @return
     *          a format string
     */
    public static String toHex(int value, int wordWidth, boolean useHexPrefix) {
        return String.format(createHexFormatString(wordWidth, useHexPrefix), value);
    }
}