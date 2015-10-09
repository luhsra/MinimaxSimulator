package de.uni_hannover.sra.minimax_simulator.ui.actions;

import java.awt.event.ActionEvent;

import de.uni_hannover.sra.minimax_simulator.Application;

@Deprecated
public class ViewTabAction extends ProjectAction
{
	private final String	_tab;

	public ViewTabAction(String tab)
	{
		_tab = tab;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Application.getMainWindow().getWorkspacePanel().getProjectPanel().selectTab(_tab,
			true);
	}
}