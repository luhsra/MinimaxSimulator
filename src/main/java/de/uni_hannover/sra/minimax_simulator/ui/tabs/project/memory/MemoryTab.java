package de.uni_hannover.sra.minimax_simulator.ui.tabs.project.memory;

import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.project.ProjectTab;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.project.memory.components.MemoryPanel;

public class MemoryTab extends ProjectTab<MemoryPanel>
{
	public MemoryTab(Project project)
	{
		super("memory", new MemoryPanel(project));
	}
}
