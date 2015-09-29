package de.uni_hannover.sra.minimax_simulator.ui.tabs.project.debugger.components;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.Simulation;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import de.uni_hannover.sra.minimax_simulator.ui.common.Disposable;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.project.memory.components.MemoryView;

@Deprecated
public class DebuggerPanel extends JPanel implements Disposable
{
	private final RegisterView		_register;
	private final MemoryView		_memory;
	private final AluResultView		_alu;
	private final SimulationPanel	_simulationPanel;

	private MigLayout createLayout()
	{
		String cols = "[]unrel[]";
		String rows = "[min:p:p,top]unrel[min!]unrel[min:p:p]";

		return new MigLayout("left, top", cols, rows);
	}

	public DebuggerPanel(Project project, Simulation simulation)
	{
		_register = new RegisterView(project.getMachine(),
			project.getMachineConfiguration(), simulation);
		_memory = new MemoryView(simulation.getMemoryState());
		_alu = new AluResultView(simulation);

		_simulationPanel = new SimulationPanel(simulation, project.getSignalTable());

		setLayout(createLayout());

		add(_memory, "cell 0 0");
		add(_alu, "cell 0 1");
		add(_register, "cell 0 2");
		add(_simulationPanel, "cell 1 0 1 3");
	}

	@Override
	public void dispose()
	{
		_alu.dispose();
		_register.dispose();
		_memory.dispose();
		_simulationPanel.dispose();
	}
}