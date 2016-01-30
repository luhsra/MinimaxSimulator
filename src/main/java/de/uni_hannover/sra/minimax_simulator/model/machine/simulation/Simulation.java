package de.uni_hannover.sra.minimax_simulator.model.machine.simulation;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;

/**
 * Provides the methods each implementation of a machine simulation has to implement.
 *
 * @author Martin L&uuml;ck
 */
public interface Simulation {

    /**
     * Gets the ALU result.
     *
     * @return
     *          the ALU result
     */
    public Traceable<Integer> getAluResult();

    /**
     * Gets the value of the specified register.
     *
     * @param name
     *          the name of the register
     * @return
     *          the value of the register
     */
    public Traceable<Integer> getRegisterValue(String name);

    /**
     * Gets the current memory state.
     *
     * @return
     *          the {@link MachineMemory}
     */
    public MachineMemory getMemoryState();

    /**
     * Resets the simulation.
     */
    public void reset();

    /**
     * Initializes the simulation.
     */
    public void init();

    /**
     * Stops the simulation.
     */
    public void stop();

    /**
     * Simulates the next cycle.
     */
    public void step();

    /**
     * Runs the simulation.
     */
    public void run();

    /**
     * Pauses the simulation.
     */
    public void pause();

    /**
     * Registers the specified {@link SimulationListener}.
     *
     * @param listener
     *          the {@code SimulationListener} to register
     */
    public void addSimulationListener(SimulationListener listener);

    /**
     * Removes the specified {@link SimulationListener}.
     *
     * @param listener
     *          the {@code SimulationListener} to remove
     */
    public void removeSimulationListener(SimulationListener listener);

    /**
     * Gets the current state of the simulation.
     *
     * @return
     *          the {@link SimulationState}
     */
    public SimulationState getState();

    /**
     * Gets the count of cycles already simulated.
     *
     * @return
     *          the count of simulated cycles
     */
    public int getCyclesCount();

    /**
     * Gets the index of the current {@link de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow}.
     *
     * @return
     *          the index of the current signal row
     */
    public int getCurrentSignalRow();

    /**
     * Gets the value of the {@code resolved} property.
     *
     * @return
     *          {@code true} if the simulation is resolved, {@code false} otherwise
     */
    public boolean isResolved();

    /**
     * Gets the value of the {@code halted} property.
     *
     * @return
     *          {@code true} if the simulation is halted, {@code false} otherwise
     */
    public boolean isHalted();
}