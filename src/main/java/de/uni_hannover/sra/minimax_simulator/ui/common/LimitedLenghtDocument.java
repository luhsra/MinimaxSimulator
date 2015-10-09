package de.uni_hannover.sra.minimax_simulator.ui.common;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

@Deprecated
public class LimitedLenghtDocument extends PlainDocument
{
	private final int	_limit;

	public LimitedLenghtDocument(int limit)
	{
		_limit = limit;
	}

	@Override
	public void insertString(int offset, String str, AttributeSet attr)
			throws BadLocationException
	{
		if (str == null)
			return;

		if (getLength() + str.length() <= _limit)
		{
			super.insertString(offset, str, attr);
		}
	}
}
