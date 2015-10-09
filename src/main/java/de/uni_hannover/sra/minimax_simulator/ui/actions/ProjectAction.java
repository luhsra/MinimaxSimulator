package de.uni_hannover.sra.minimax_simulator.ui.actions;

import javax.swing.AbstractAction;

import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import de.uni_hannover.sra.minimax_simulator.model.user.WorkspaceListener;
import de.uni_hannover.sra.minimax_simulator.ui.UI;

@Deprecated
public abstract class ProjectAction extends AbstractAction implements WorkspaceListener
{
	public ProjectAction()
	{
//		Main.getWorkspace().addListener(this);

		// initial state: no project open
		setEnabled(false);
	}

	private void setEnabledInEDT(final boolean enabled)
	{
/*		UI.invokeInEDT(new Runnable()
		{
			@Override
			public void run()
			{
				setEnabled(enabled);
			}
		});	*/
	}

	@Override
	public void onProjectOpened(Project project)
	{
		setEnabledInEDT(true);
	}

	@Override
	public void onProjectClosed(Project project)
	{
		setEnabledInEDT(false);
	}

	@Override
	public void onProjectDirty(Project project)
	{
	}

	@Override
	public void onProjectSaved(Project project)
	{
	}
}
