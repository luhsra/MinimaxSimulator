package de.uni_hannover.sra.minimax_simulator.model.machine.base.topology;

import java.util.Set;

public interface MachineTopology
{
	public void addCircuit(String id, Circuit c);

	public <T extends Circuit> void addCircuit(Class<T> clazz, String id, T c);

	public void removeCircuit(String id);

	public <T extends Circuit> T getCircuit(Class<T> clazz, String id);

	public Set<Circuit> getAllCircuits();
}