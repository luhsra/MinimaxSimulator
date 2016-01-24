package de.uni_hannover.sra.minimax_simulator.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code Parser} is used to parse the configurable fields read from {@link ConfigurationFile}
 * to their according type.
 *
 * @author Martin L&uuml;ck
 */
class Parser {

    // used for file size parsers
    private static final Pattern NMB = Pattern.compile("[0-9]+");
    private static final Pattern S_B = Pattern.compile("[0-9]+(B|b)?");
    private static final Pattern S_KB = Pattern.compile("[0-9]+(K|k)(B|b)?");
    private static final Pattern S_MB = Pattern.compile("[0-9]+(M|m)(B|b)?");
    private static final Pattern S_GB = Pattern.compile("[0-9]+(G|g)(B|b)?");

    /**
     * Prevents instance creation of this utility class.
     */
    private Parser() {

    }

    /**
     * Parses the property value according to the given class.<br>
     * This method is used for {@link FieldType#AUTO} configuration fields.
     *
     * @param textValue
     *          the property value to parse
     * @param clazz
     *          the class of the property
     * @return
     *          the property converted to the according class
     */
    public static Object auto(String textValue, Class<?> clazz) {
        Object value = null;

        if (clazz == String.class) {
            value = textValue;
        }
        else if (clazz == Boolean.class || clazz == Boolean.TYPE) {
            value = toBoolean(textValue);
        }
        else if (clazz == Integer.class || clazz == Integer.TYPE) {
            value = toInteger(textValue);
        }
        else if (clazz == Long.class || clazz == Long.TYPE) {
            value = toLong(textValue);
        }
        else if (clazz == Short.class || clazz == Short.TYPE) {
            value = toShort(textValue);
        }
        else if (clazz == Byte.class || clazz == Byte.TYPE) {
            value = toByte(textValue);
        }
        else if (clazz == Character.class || clazz == Character.TYPE) {
            value = toChar(textValue);
        }
        else if (clazz == Float.class || clazz == Float.TYPE) {
            value = toFloat(textValue);
        }
        else if (clazz == Double.class || clazz == Double.TYPE) {
            value = toDouble(textValue);
        }

        return value;
    }

    /**
     * Converts the property value to {@link byte}.
     *
     * @param value
     *          the property value to parse
     * @return
     *          the property as {@code byte}
     */
    public static byte toByte(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Missing Byte attribute");
        }

        try {
            return Byte.decode(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Wrong Byte attribute: " + value);
        }
    }

    /**
     * Converts the property value to {@link char}.
     *
     * @param value
     *          the property value to parse
     * @return
     *          the property as {@code char}
     */
    public static char toChar(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Missing Char attribute");
        }

        if (value.length() != 1) {
            throw new IllegalArgumentException("Wrong Char attribute (size-mismatch): " + value);
        }

        return value.charAt(0);
    }

    /**
     * Converts the property value to {@link short}.
     *
     * @param value
     *          the property value to parse
     * @return
     *          the property as {@code short}
     */
    public static short toShort(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Missing Short attribute");
        }

        try {
            return Short.decode(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Wrong Short attribute: " + value);
        }
    }

    /**
     * Converts the property value to {@link int}.
     *
     * @param value
     *          the property value to parse
     * @return
     *          the property as {@code int}
     */
    public static int toInteger(String value)
    {
        if (value == null) {
            throw new IllegalArgumentException("Missing Integer attribute");
        }

        try {
            return Integer.decode(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Wrong Integer attribute: " + value);
        }
    }

    /**
     * Converts the property value to {@link long}.
     *
     * @param value
     *          the property value to parse
     * @return
     *          the property as {@code long}
     */
    public static long toLong(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Missing Long attribute");
        }

        try {
            return Long.decode(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Wrong Long attribute: " + value);
        }
    }

    /**
     * Converts the property value to {@link float}.
     *
     * @param value
     *          the property value to parse
     * @return
     *          the property as {@code float}
     */
    public static float toFloat(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Missing Float attribute");
        }

        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Wrong Float attribute: " + value);
        }
    }

    /**
     * Converts the property value to {@link double}.
     *
     * @param value
     *          the property value to parse
     * @return
     *          the property as {@code double}
     */
    public static double toDouble(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Missing Double attribute");
        }

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Wrong Double attribute: " + value);
        }
    }

    /**
     * Converts the property value to {@link boolean}.
     *
     * @param value
     *          the property value to parse
     * @return
     *          the property as {@code boolean}
     */
    public static boolean toBoolean(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Missing Boolean attribute");
        }

        value = value.toLowerCase();
        if ("true".equals(value) || "1".equals(value) || "yes".equals(value)) {
            return true;
        }
        else if ("false".equals(value) || "0".equals(value) || "no".equals(value)) {
            return false;
        }
        else {
            throw new IllegalArgumentException("Wrong Boolean attribute: " + value);
        }
    }

    /**
     * Converts the property value to {@link String[]}.
     *
     * @param value
     *          the property value to parse
     * @param delim
     *          the string separator (delimiter)
     * @return
     *          the property as {@code String[]}
     */
    public static String[] toStrings(String value, String delim) {
        if (value == null) {
            throw new IllegalArgumentException("Missing String[] attribute");
        }
        return value.split(Pattern.quote(delim));
    }

    /**
     * Converts the property value to a file size in {@code long}.<br><br>
     * This method is used for {@link FieldType#BYTESIZE} configuration fields.
     *
     * @param value
     *          the property value to parse
     * @return
     *          the file size
     * @throws IllegalArgumentException
     *          thrown if the parameter {@code value} had the wrong format
     */
    public static long toFileSize(String value) throws IllegalArgumentException {
        Matcher m = NMB.matcher(value);

        long size;
        // extract number
        if (m.find()) {
            size = Long.parseLong(m.group(0));
        }
        else {
            throw new IllegalArgumentException("Wrong format for file size for config: " + value);
        }

        // byte format
        if (S_B.matcher(value).matches()) {
            return size;
        }
        else if (S_KB.matcher(value).matches()) {
            return size * 1024;
        }
        else if (S_MB.matcher(value).matches()) {
            return size * 1048576;      // 1024^2
        }
        else if (S_GB.matcher(value).matches()) {
            return size * 1073741824;   // 1024^3
        }
        else {
            throw new IllegalArgumentException("Wrong format for file size for config: " + value);
        }
    }

    /**
     * Converts the property value to an {@code enum} of the given class.
     *
     * @param val
     *          the property value to parse
     * @param clazz
     *          the class of the {@code enum} to create
     * @param <E>
     *          the {@code enum} class
     * @return
     *          the property value as {@code enum} of the given class
     */
    public static <E extends Enum<E>> E toEnum(String val, Class<E> clazz) {
        try {
            return Enum.valueOf(clazz, val.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Enum value \"" + val + "\" not found in class: " + clazz.getSimpleName());
        }
    }
}