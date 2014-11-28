package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.DefaultCircuitTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.Group;

class MinimaxTopology extends DefaultCircuitTopology
{
	public void addGroup(Group group)
	{
		for (Circuit circuit : group.getGroupCircuits())
			addCircuit(group.getName(circuit), circuit);
	}

	public void removeGroup(Group group)
	{
		for (Circuit circuit : group.getGroupCircuits())
			removeCircuit(group.getName(circuit));
	}
}