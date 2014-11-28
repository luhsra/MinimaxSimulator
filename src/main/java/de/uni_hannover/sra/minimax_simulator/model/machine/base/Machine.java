package de.uni_hannover.sra.minimax_simulator.model.machine.base;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.MachineDisplay;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;

public interface Machine
{
	public MachineDisplay getDisplay();

	public MachineMemory getMemory();

	public MachineTopology getTopology();
}