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
    private final MachineConfiguration machineConfiguration;
    private final ProjectConfiguration projectConfiguration;
    private final SignalTable signalTable;

    // the simulated machine
    private final ConfigurableMachine machine;
    private final SignalConfiguration signalConfiguration;
    private final Simulation simulation;

    private boolean isUnsaved;

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
        isUnsaved = false;

        machineConfiguration = checkNotNull(machineConfig);
        projectConfiguration = checkNotNull(projectConfig);
        signalConfiguration = new MinimaxSignalConfiguration(machineConfiguration);
        this.signalTable = new MachineSignalTable(signalTable, machineConfiguration, new MinimaxSignalDescription(machineConfiguration), signalConfiguration);
        MinimaxMachine minimax = new MinimaxMachine();
        machine = minimax;
        machineConfiguration.addMachineConfigListener(new MachineConfigurator(machine, machineConfiguration));
        MinimaxSimulation simulation = new MinimaxSimulation(minimax, this.signalTable);
        this.simulation = simulation;
        machineConfiguration.addMachineConfigListener(simulation);
        signalConfiguration.addSignalConfigListener(simulation);
    }

    /**
     * Gets the {@link MachineConfiguration}.
     *
     * @return
     *          the machine's configuration
     */
    public MachineConfiguration getMachineConfiguration() {
        return machineConfiguration;
    }

    /**
     * Gets the {@link ProjectConfiguration}.
     *
     * @return
     *          the project configuration
     */
    public ProjectConfiguration getProjectConfiguration() {
        return projectConfiguration;
    }

    /**
     * Gets the {@link SignalTable}.
     *
     * @return
     *          the machine's control table
     */
    public SignalTable getSignalTable() {
        return signalTable;
    }

    /**
     * Gets the value of the {@code isUnsaved} property.
     *
     * @return
     *          {@code true} if the project has unsaved changes, {@code false} otherwise
     */
    public boolean isUnsaved() {
        return isUnsaved;
    }

    /**
     * Marks the {@code Project} as unsaved.
     */
    public void setIsUnsaved() {
        isUnsaved = true;
    }

    /**
     * Marks the {@code Project} as saved.
     */
    public void setIsSaved() {
        isUnsaved = false;
    }

    /**
     * Gets the {@link Machine}.
     *
     * @return
     *          the machine
     */
    public Machine getMachine() {
        return machine;
    }

    /**
     * Gets the {@link SignalConfiguration}.
     *
     * @return
     *          the signal configuration
     */
    public SignalConfiguration getSignalConfiguration() {
        return signalConfiguration;
    }

    /**
     * Gets the {@link Simulation} instance.
     *
     * @return
     *          the simulation
     */
    public Simulation getSimulation() {
        return simulation;
    }
}