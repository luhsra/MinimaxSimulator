package de.uni_hannover.sra.minimax_simulator.model.user;

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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The {@code Project} holds every information of the current machine, i.e. the {@link MachineConfiguration},
 * {@link ProjectConfiguration}, {@link SignalTable}, {@link ConfigurableMachine}, {@link SignalConfiguration} and
 * {@link Simulation}.
 *
 * @author Martin L&uuml;ck
 */
public class Project {

	// the part that is actually export-friendly
	private final MachineConfiguration	_machineConfiguration;
	private final ProjectConfiguration	_projectConfiguration;
	private final SignalTable			_signalTable;

	// the simulated machine
	private final ConfigurableMachine	_machine;
	private final SignalConfiguration	_signalConfig;
	private final Simulation			_simulation;

	private boolean						_unsaved;

	/**
	 * Constructs a new {@code Project} with the specified {@link MachineConfiguration}, {@link ProjectConfiguration} and control table.
	 *
	 * @param machineConfig
	 *          the machine's configuration
	 * @param projectConfig
	 *          the project configuration
	 * @param signalTable
	 *          the machine's control table
	 */
	public Project(MachineConfiguration machineConfig, ProjectConfiguration projectConfig, SignalTable signalTable) {
		_unsaved = false;

		_machineConfiguration = checkNotNull(machineConfig);
		_projectConfiguration = checkNotNull(projectConfig);
		_signalConfig = new MinimaxSignalConfiguration(_machineConfiguration);
		_signalTable = new MachineSignalTable(signalTable, _machineConfiguration, new MinimaxSignalDescription(_machineConfiguration), _signalConfig);
		MinimaxMachine minimax = new MinimaxMachine();
		_machine = minimax;
		_machineConfiguration.addMachineConfigListener(new MachineConfigurator(_machine, _machineConfiguration));
		MinimaxSimulation simulation = new MinimaxSimulation(minimax, _signalTable);
		_simulation = simulation;
		_machineConfiguration.addMachineConfigListener(simulation);
		_signalConfig.addSignalConfigListener(simulation);
	}

	/**
	 * Gets the {@link MachineConfiguration}.
	 *
	 * @return
	 *          the machine's configuration
	 */
	public MachineConfiguration getMachineConfiguration() {
		return _machineConfiguration;
	}

	/**
	 * Gets the {@link ProjectConfiguration}.
	 *
	 * @return
	 *          the project configuration
	 */
	public ProjectConfiguration getProjectConfiguration() {
		return _projectConfiguration;
	}

	/**
	 * Gets the {@link SignalTable}.
	 *
	 * @return
	 *          the machine's control table
	 */
	public SignalTable getSignalTable() {
		return _signalTable;
	}

	/**
	 * Gets the value of the {@code unsaved} property.
	 *
	 * @return
	 *          {@code true} if the project has unsaved changes, {@code false} otherwise
	 */
	public boolean isUnsaved() {
		return _unsaved;
	}

	/**
	 * Marks the {@code Project} as unsaved.
	 */
	public void setIsUnsaved() {
		_unsaved = true;
	}

	/**
	 * Marks the {@code Project} as saved.
	 */
	public void setIsSaved() {
		_unsaved = false;
	}

	/**
	 * Gets the {@link Machine}.
	 *
	 * @return
	 *          the machine
	 */
	public Machine getMachine() {
		return _machine;
	}

	/**
	 * Gets the {@link SignalConfiguration}.
	 *
	 * @return
	 *          the signal configuration
	 */
	public SignalConfiguration getSignalConfiguration() {
		return _signalConfig;
	}

	/**
	 * Gets the {@link Simulation} instance.
	 *
	 * @return
	 *          the simulation
	 */
	public Simulation getSimulation() {
		return _simulation;
	}
}