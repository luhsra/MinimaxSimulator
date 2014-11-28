package de.uni_hannover.sra.minimax_simulator.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.Config;

public class DebugSprites extends AbstractAction
{
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Config.DEBUG_SCHEMATICS = !Config.DEBUG_SCHEMATICS;
		Application.getMainWindow().getWorkspacePanel().repaint();
	}
}