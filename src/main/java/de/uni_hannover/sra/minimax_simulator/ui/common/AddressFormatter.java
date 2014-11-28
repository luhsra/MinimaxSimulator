package de.uni_hannover.sra.minimax_simulator.ui.common;

import java.text.ParseException;

import javax.swing.text.DefaultFormatter;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.AddressRange;
import de.uni_hannover.sra.minimax_simulator.util.Util;

public class AddressFormatter extends DefaultFormatter
{
	private final AddressRange _range;
	private final String _format;

	public AddressFormatter(AddressRange range)
	{
		_range = range;
		_format = Util.createHexFormatString(range.getAddressWidth(), false);

		setAllowsInvalid(true);
		setCommitsOnValidEdit(true);
		setOverwriteMode(true);
	}

	@Override
	public Object stringToValue(String text) throws ParseException
	{
		if (text == null || text.isEmpty())
			throw new ParseException(text, 0);

		try
		{
			Long l = Long.valueOf(text, 16);
			int value = l.intValue();
			if (value < _range.getMinAddress())
				value = _range.getMinAddress();
			else if (value > _range.getMaxAddress())
				value = _range.getMaxAddress();
			return value;
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

		//int nr = ((Integer) value).intValue();
		//String str = Long.toString(nr & 0xFFFFFFFFL, 16);
		//return str.toUpperCase();
		return String.format(_format, value);
	}
}