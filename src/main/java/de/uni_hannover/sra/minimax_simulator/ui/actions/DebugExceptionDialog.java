package de.uni_hannover.sra.minimax_simulator.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

@Deprecated
public class DebugExceptionDialog extends AbstractAction
{
	@Override
	public void actionPerformed(ActionEvent e)
	{
		throw new RuntimeException("Test exception");
	}
}