package de.uni_hannover.sra.minimax_simulator.ui.common;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@Deprecated
public abstract class AbstractDocumentListener implements DocumentListener
{
	public abstract void documentChanged(DocumentEvent e);

	@Override
	public void insertUpdate(DocumentEvent e)
	{
		documentChanged(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e)
	{
		documentChanged(e);
	}

	@Override
	public void changedUpdate(DocumentEvent e)
	{
		documentChanged(e);
	}
}