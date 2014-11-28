package de.uni_hannover.sra.minimax_simulator.ui.actions;

import java.awt.event.ActionEvent;

import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;

public class ProjectSave extends ProjectSaveTo
{
	public ProjectSave()
	{
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		save(Application.getWorkspace().getCurrentProjectFile());
	}

	@Override
	public void onProjectOpened(Project project)
	{
		// do not enable
	}

	@Override
	public void onProjectSaved(Project project)
	{
		setEnabled(false);
	}

	@Override
	public void onProjectDirty(Project project)
	{
		setEnabled(true);
	}
}