package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.alu;

import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.MachineTab;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.alu.components.AluEditorPanel;

public class AluTab extends MachineTab<AluEditorPanel>
{
	public AluTab(Project project)
	{
		super("alu", new AluEditorPanel(project));
	}
}