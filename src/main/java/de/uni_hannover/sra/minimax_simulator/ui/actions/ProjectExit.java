package de.uni_hannover.sra.minimax_simulator.ui.actions;

import javafx.application.Platform;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

@Deprecated
public class ProjectExit extends AbstractAction
{
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Platform.exit();
	}
}