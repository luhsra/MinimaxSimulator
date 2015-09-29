package de.uni_hannover.sra.minimax_simulator.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.uni_hannover.sra.minimax_simulator.Application;

@Deprecated
public class ProjectExit extends AbstractAction
{
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Application.shutdown();
	}
}