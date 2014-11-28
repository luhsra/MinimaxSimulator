package de.uni_hannover.sra.minimax_simulator.ui.common;

import java.text.ParseException;

import javax.swing.text.DefaultFormatter;

public class NullAwareIntFormatter extends DefaultFormatter
{
	private final int _radix;
	private final boolean _signed;

	public NullAwareIntFormatter(int radix, boolean signed)
	{
		_radix = radix;
		_signed = signed;
		setAllowsInvalid(true);
		setCommitsOnValidEdit(true);
		setOverwriteMode(false);
	}

	public NullAwareIntFormatter()
	{
		this(10, true);
	}

	@Override
	public Object stringToValue(String text) throws ParseException
	{
		if (text == null || text.isEmpty())
			return null;

		try
		{
			Long l = Long.valueOf(text, _radix);
			return l.intValue();
		}
		catch (NumberFormatException nfe)
		{
			throw new ParseException(text, 0);
		}
	}

	@Override
	public String valueToString(Object value) throws ParseException
	{
		if (value == null)
			return "";

		int nr = (Integer) value;

		String str;
		if (!_signed)
		{
			// For unsigned values
			str = Long.toString(nr & 0xFFFFFFFFL, _radix);
		}
		else
		{
			// For signed values
			str = Integer.toString(nr, _radix);
		}

		return _radix <= 10 ? str : str.toUpperCase();
	}
}