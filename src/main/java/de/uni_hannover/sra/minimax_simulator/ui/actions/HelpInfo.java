package de.uni_hannover.sra.minimax_simulator.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.ui.common.dialogs.JInfoDialog;

public class HelpInfo extends AbstractAction
{
	@Override
	public void actionPerformed(ActionEvent e)
	{
		JInfoDialog dialog = new JInfoDialog(Application.getTextResource("application"));
		dialog.setVisible(true);
	}
}