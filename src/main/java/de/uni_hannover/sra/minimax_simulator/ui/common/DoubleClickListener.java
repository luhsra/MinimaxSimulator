package de.uni_hannover.sra.minimax_simulator.ui.common;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

@Deprecated
public abstract class DoubleClickListener implements MouseListener
{
	public abstract void doubleClicked(MouseEvent e);

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() % 2 == 0)
			doubleClicked(e);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}
}