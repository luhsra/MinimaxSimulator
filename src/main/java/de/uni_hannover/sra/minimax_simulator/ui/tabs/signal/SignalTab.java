package de.uni_hannover.sra.minimax_simulator.ui.tabs.signal;

import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.MachineTab;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.components.SignalTablePanel;

public class SignalTab extends MachineTab<SignalTablePanel>
{
	public SignalTab(Project project)
	{
		super("signal", new SignalTablePanel(project.getSignalTable(),
			project.getSignalConfiguration()));
	}
}