package de.uni_hannover.sra.minimax_simulator.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class DebugGc extends AbstractAction
{
	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.gc();
	}
}