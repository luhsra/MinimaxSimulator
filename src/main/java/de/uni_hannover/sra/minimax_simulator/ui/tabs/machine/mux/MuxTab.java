package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.mux;

import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.MachineTab;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.mux.components.MuxEditorPanel;

public class MuxTab extends MachineTab<MuxEditorPanel>
{
	public MuxTab(Project project)
	{
		super("mux", new MuxEditorPanel(project.getMachineConfiguration()));
	}
}