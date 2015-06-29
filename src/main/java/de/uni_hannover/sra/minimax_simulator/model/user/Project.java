package de.uni_hannover.sra.minimax_simulator.model.user;

import static com.google.common.base.Preconditions.*;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.machine.MachineSignalTable;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.ConfigurableMachine;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.Machine;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.MachineConfigurator;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.MinimaxMachine;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.MinimaxSignalConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.MinimaxSignalDescription;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.MinimaxSimulation;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.Simulation;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;

public class Project
{
	// The part that is actually export-friendly
	private final MachineConfiguration	_machineConfiguration;
	private final ProjectConfiguration	_projectConfiguration;
	private final SignalTable			_signalTable;

	// The simulated machine
	private final ConfigurableMachine	_machine;
	private final SignalConfiguration	_signalConfig;
	private final Simulation			_simulation;

	private boolean						_unsaved;

	public Project(MachineConfiguration machineConfig,
			ProjectConfiguration projectConfig, SignalTable signalTable)
	{
		_unsaved = false;

		_machineConfiguration = checkNotNull(machineConfig);
		_projectConfiguration = checkNotNull(projectConfig);
		System.out.println("DEBUG: call new MinimaxSignalConf");
		_signalConfig = new MinimaxSignalConfiguration(_machineConfiguration);
		System.out.println("DEBUG: call new MinimaxSignalConf succeeded");
		System.out.println("DEBUG: call new MST");
		_signalTable = new MachineSignalTable(signalTable, _machineConfiguration,
			new MinimaxSignalDescription(_machineConfiguration), _signalConfig);
		System.out.println("DEBUG: call new MST succeeded");
		System.out.println("DEBUG: call new MinimaxMachine");
		MinimaxMachine minimax = new MinimaxMachine();
		_machine = minimax;
		_machineConfiguration.addMachineConfigListener(new MachineConfigurator(_machine,
			_machineConfiguration));
		System.out.println("DEBUG: call new MinimaxMachine succeeded");
		System.out.println("DEBUG: call some adds");
		MinimaxSimulation simulation = new MinimaxSimulation(minimax, _signalTable);
		_simulation = simulation;
		_machineConfiguration.addMachineConfigListener(simulation);
		_signalConfig.addSignalConfigListener(simulation);
		System.out.println("DEBUG: call some adds succeeded");
	}

	public MachineConfiguration getMachineConfiguration()
	{
		return _machineConfiguration;
	}

	public ProjectConfiguration getProjectConfiguration()
	{
		return _projectConfiguration;
	}

	public SignalTable getSignalTable()
	{
		return _signalTable;
	}

	public boolean isUnsaved()
	{
		return _unsaved;
	}

	public void setIsUnsaved()
	{
		_unsaved = true;
	}

	public void setIsSaved()
	{
		_unsaved = false;
	}

	public Machine getMachine()
	{
		return _machine;
	}

	public SignalConfiguration getSignalConfiguration()
	{
		return _signalConfig;
	}

	public Simulation getSimulation()
	{
		return _simulation;
	}
}