package de.uni_hannover.sra.minimax_simulator.model.user;

public interface WorkspaceListener
{
	public void onProjectOpened(Project project);

	public void onProjectSaved(Project project);

	public void onProjectClosed(Project project);

	public void onProjectDirty(Project project);
}