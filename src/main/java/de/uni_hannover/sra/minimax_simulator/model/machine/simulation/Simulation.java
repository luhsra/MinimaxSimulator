package de.uni_hannover.sra.minimax_simulator.model.machine.simulation;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;

public interface Simulation
{
	public Trackable<Integer> getAluResult();

	public Trackable<Integer> getRegisterValue(String name);

	public MachineMemory getMemoryState();

	public void reset();

	public void init();

	public void stop();

	public void step();

	public void run();

	public void pause();

	public void addSimulationListener(SimulationListener listener);

	public void removeSimulationListener(SimulationListener listener);

	public SimulationState getState();

	public int getCyclesCount();

	public int getCurrentSignalRow();

	public boolean isResolved();

	public boolean isHalted();
}