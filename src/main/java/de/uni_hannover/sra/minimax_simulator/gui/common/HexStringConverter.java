package de.uni_hannover.sra.minimax_simulator.gui.common;

import javafx.util.StringConverter;

/**
 * The HexStringConverter is a {@link StringConverter} that converts decimal values to their hexadecimal representation.
 * It may use a format string for the conversion.
 *
 * @author Philipp Rohde
 */
public class HexStringConverter extends StringConverter<Integer> {

    private final String formatString;
    private final int maxValue;
    private final int minValue;

    /**
     * The default constructor takes the minimal and maximal value. No format string is set.
     *
     * @param minValue
     * 			the minimal value that is allowed
     * @param maxValue
     * 			the maximal value that is allowed
     */
    public HexStringConverter(int minValue, int maxValue) {
        super();
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.formatString = "";
    }

    /**
     * This constructor also takes a format string. The minimal and maximal value will be set as well as
     * the format string.
     *
     * @param minValue
     * 			the minimal value that is allowed
     * @param maxValue
     * 			the maximal value that is allowed
     * @param formatString
     * 			the format string to use for conversion
     */
    public HexStringConverter(int minValue, int maxValue, String formatString) {
        super();
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.formatString = formatString;
    }

    @Override
    public String toString(Integer value) {
        if (value == null) {
            return "";
        }
        else if (formatString.equals("")) {
            return Long.toString(value & 0xFFFFFFFFL, 16).toUpperCase();
        }
        else {
            return String.format(formatString, value);
        }
    }

    @Override
    public Integer fromString(String text) {
        if (text == null || text.isEmpty())
            return null;

        try {
            Long l = Long.valueOf(text, 16);
            int value = l.intValue();
            if (value < minValue)
                value = minValue;
            else if (value > maxValue)
                value = maxValue;
            return value;
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        return null;
    }

}
