package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.Group;

interface GroupManager
{
	public void initializeGroup(String id, Group group);

	public void removeGroup(String id);
}