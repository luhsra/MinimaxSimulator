package de.uni_hannover.sra.minimax_simulator.ui.common.components;

import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.JMenuItem;

public class JCustomMenuItem extends JMenuItem
{
	public JCustomMenuItem(String text)
	{
		super(text);
	}

	public JCustomMenuItem()
	{
		super();
	}

	public JCustomMenuItem(Action action)
	{
		super(action);
	}

	@Override
	protected void processMouseEvent(MouseEvent e)
	{
		// pass up event only if enabled -- prevents hiding of menu
		// when disabled item is clicked
		if (isEnabled())
			super.processMouseEvent(e);
	}
}
