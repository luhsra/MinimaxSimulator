package de.uni_hannover.sra.minimax_simulator.ui.tabs.project.debugger;

import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.project.ProjectTab;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.project.debugger.components.DebuggerPanel;

public class DebuggerTab extends ProjectTab<DebuggerPanel>
{
	public DebuggerTab(Project project)
	{
		super("debugger", new DebuggerPanel(project, project.getSimulation()));
	}
}
