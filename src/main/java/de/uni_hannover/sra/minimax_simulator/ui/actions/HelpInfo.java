package de.uni_hannover.sra.minimax_simulator.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.ui.common.dialogs.JInfoDialog;

@Deprecated
public class HelpInfo extends AbstractAction
{
	@Override
	public void actionPerformed(ActionEvent e)
	{
		JInfoDialog dialog = new JInfoDialog(Main.getTextResource("application"));
		dialog.setVisible(true);
	}
}