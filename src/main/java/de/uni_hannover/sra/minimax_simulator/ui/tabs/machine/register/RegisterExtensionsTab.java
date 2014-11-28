package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.register;

import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.MachineTab;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.register.components.RegisterEditorPanel;

public class RegisterExtensionsTab extends MachineTab<RegisterEditorPanel>
{
	public RegisterExtensionsTab(Project project)
	{
		super("register", new RegisterEditorPanel(project));
	}
}