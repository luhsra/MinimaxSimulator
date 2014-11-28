package de.uni_hannover.sra.minimax_simulator.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Parser
{
	// Used for file size parsers
	private final static Pattern NMB = Pattern.compile("[0-9]+");
	private final static Pattern S_B = Pattern.compile("[0-9]+(B|b)?");
	private final static Pattern S_KB = Pattern.compile("[0-9]+(K|k)(B|b)?");
	private final static Pattern S_MB = Pattern.compile("[0-9]+(M|m)(B|b)?");
	private final static Pattern S_GB = Pattern.compile("[0-9]+(G|g)(B|b)?");

	public static Object auto(String textValue, Class<?> clazz)
	{
		Object value = null;

		if (clazz == String.class)
		{
			value = textValue;
		}
		else if (clazz == Boolean.class || clazz == Boolean.TYPE)
		{
			value = toBoolean(textValue);
		}
		else if (clazz == Integer.class || clazz == Integer.TYPE)
		{
			value = toInteger(textValue);
		}
		else if (clazz == Long.class || clazz == Long.TYPE)
		{
			value = toLong(textValue);
		}
		else if (clazz == Short.class || clazz == Short.TYPE)
		{
			value = toShort(textValue);
		}
		else if (clazz == Byte.class || clazz == Byte.TYPE)
		{
			value = toByte(textValue);
		}
		else if (clazz == Character.class || clazz == Character.TYPE)
		{
			value = toChar(textValue);
		}
		else if (clazz == Float.class || clazz == Float.TYPE)
		{
			value = toFloat(textValue);
		}
		else if (clazz == Double.class || clazz == Double.TYPE)
		{
			value = toDouble(textValue);
		}

		return value;
	}

	public static byte toByte(String value)
	{
		if (value == null)
			throw new IllegalArgumentException("Missing Byte attribute");
		try
		{
			return Byte.decode(value);
		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException("Wrong Byte attribute: " + value);
		}
	}

	public static char toChar(String value)
	{
		if (value == null)
			throw new IllegalArgumentException("Missing Char attribute");

		if (value.length() != 1)
			throw new IllegalArgumentException("Wrong Char attribute (size-mismatch): " + value);

		return value.charAt(0);
	}

	public static short toShort(String value)
	{
		if (value == null)
			throw new IllegalArgumentException("Missing Short attribute");
		try
		{
			return Short.decode(value);
		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException("Wrong Short attribute: " + value);
		}
	}

	public static int toInteger(String value)
	{
		if (value == null)
			throw new IllegalArgumentException("Missing Integer attribute");
		try
		{
			return Integer.decode(value);
		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException("Wrong Integer attribute: " + value);
		}
	}

	public static long toLong(String value)
	{
		if (value == null)
			throw new IllegalArgumentException("Missing Long attribute");
		try
		{
			return Long.decode(value);
		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException("Wrong Long attribute: " + value);
		}
	}

	public static float toFloat(String value)
	{
		if (value == null)
			throw new IllegalArgumentException("Missing Float attribute");
		try
		{
			return Float.parseFloat(value);
		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException("Wrong Float attribute: " + value);
		}
	}

	public static double toDouble(String value)
	{
		if (value == null)
			throw new IllegalArgumentException("Missing Double attribute");
		try
		{
			return Double.parseDouble(value);
		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException("Wrong Double attribute: " + value);
		}
	}

	public static boolean toBoolean(String value)
	{
		if (value == null)
			throw new IllegalArgumentException("Missing Boolean attribute");
		value = value.toLowerCase();
		if ("true".equals(value) || "1".equals(value) || "yes".equals(value))
			return true;
		if ("false".equals(value) || "0".equals(value) || "no".equals(value))
			return false;
		throw new IllegalArgumentException("Wrong Boolean attribute: " + value);
	}

	public static String[] toStrings(String value, String delim)
	{
		if (value == null)
			throw new IllegalArgumentException("Missing String[] attribute");
		return value.split(Pattern.quote(delim));
	}

	public static long toFileSize(String value) throws IllegalArgumentException
	{
		Matcher m = NMB.matcher(value);

		long size;
		// Extract number
		if (m.find())
			size = Long.parseLong(m.group(0));
		else
			throw new IllegalArgumentException("Wrong format for file size for config: " + value);

		// Byte format
		if (S_B.matcher(value).matches())
		{
			return size;
		}
		if (S_KB.matcher(value).matches())
		{
			return size * 1024;
		}
		if (S_MB.matcher(value).matches())
		{
			return size * 1048576; // 1024^2
		}
		if (S_GB.matcher(value).matches())
		{
			return size * 1073741824; // 1024^3
		}
		throw new IllegalArgumentException("Wrong format for file size for config: " + value);
	}

	public static <E extends Enum<E>> E toEnum(String val, Class<E> clazz)
	{
		try
		{
			return Enum.valueOf(clazz, val.toUpperCase());
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Enum value \"" + val + "\" not found in class: "
					+ clazz.getSimpleName());
		}
	}
}