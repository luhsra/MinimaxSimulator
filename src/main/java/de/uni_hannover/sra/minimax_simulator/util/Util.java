package de.uni_hannover.sra.minimax_simulator.util;

import static com.google.common.base.Preconditions.*;

public class Util
{
	/**
	 * Returns <code>index</code> as a binary string. <br>
	 * The string is padded with zeros to the left such that its length would equal the minimal
	 * length of the binary string representing <code>maxIndex</code>. <br>
	 * Both integers are interpreted as unsigned values, therefore negative <code>int</code> values
	 * will always assume a width of 32 characters.
	 * 
	 * @param index
	 * @param maxIndex
	 * @return
	 */
	public static String toBinaryAddress(int index, int maxIndex)
	{
		int digits = Math.max(32 - Integer.numberOfLeadingZeros(maxIndex), 1);

		char[] buffer = new char[digits];
		int mask = 1;
		for (int i = digits - 1; i >= 0; i--)
		{
			buffer[i] = (index & mask) != 0 ? '1' : '0';
			mask <<= 1;
		}

		return new String(buffer);
	}

	/**
	 * Returns a format string for use with the {@link String#format(String, Object...)} method.
	 * <br>
	 * The created format will accept a single <code>int</code> parameter which will be
	 * formatted as an uppercase hexadecimal word.
	 * <br>
	 * The <code>wordWidth</code> parameter, which may be any non-negative number,
	 * determines the width of the represented word in bits.
	 * <br>
	 * The hexadecimal values produces by the formatter will always consist of
	 * <code>(wordWidth / 4)</code> hexadecimal letters, where <code>/</code> is a
	 * division operator that rounds up.
	 * 
	 * @param wordWidth the word width in bits
	 * @param useHexPrefix if "0x" shoud be appended before the hexadecimal value
	 * @return a format string
	 */
	public static String createHexFormatString(int wordWidth, boolean useHexPrefix)
	{
		checkArgument(wordWidth >= 0);

		StringBuilder sb = useHexPrefix ? new StringBuilder("0x") : new StringBuilder();

		// divide by 4 because of 4 bits per hex digit
		sb.append("%0").append((((wordWidth - 1) >> 2) + 1)).append('X');

		return sb.toString();
	}

	/**
	 * Formats a single integer to a hexidecimal representation of a word
	 * with fixed width.
	 * <br>
	 * The <code>wordWidth</code> parameter, which may be any non-negative number,
	 * determines the width of the represented word in bits.
	 * <br>
	 * The hexadecimal representation will consist of
	 * <code>(wordWidth / 4)</code> hexadecimal letters, where <code>/</code> is a
	 * division operator that rounds up.
	 * 
	 * @param value the integer word to represent in hexadecimal
	 * @param wordWidth the word width in bits
	 * @param useHexPrefix if "0x" shoud be appended before the hexadecimal value
	 * @return a format string
	 */
	public static String toHex(int value, int wordWidth, boolean useHexPrefix)
	{
		return String.format(createHexFormatString(wordWidth, useHexPrefix),
			Integer.valueOf(value));
	}
}