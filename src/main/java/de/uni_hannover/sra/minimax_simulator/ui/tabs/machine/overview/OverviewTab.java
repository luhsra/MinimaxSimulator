package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.overview;

import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.overview.components.MachineOverviewPanel;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.project.ProjectTab;

public class OverviewTab extends ProjectTab<MachineOverviewPanel>
{
	public OverviewTab(Project project)
	{
		super("overview", new MachineOverviewPanel(project));
	}
}