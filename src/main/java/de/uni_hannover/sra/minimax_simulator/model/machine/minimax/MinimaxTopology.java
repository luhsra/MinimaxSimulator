package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.DefaultCircuitTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.Group;

/**
 * The topology of a {@link MinimaxMachine}.
 *
 * @see DefaultCircuitTopology
 *
 * @author Martin L&uuml;ck
 */
class MinimaxTopology extends DefaultCircuitTopology {

	/**
	 * Adds all {@link Circuit}s of the specified {@link Group}.
	 *
	 * @param group
	 *          the {@code Group} whose {@code Circuit}s will be added
	 */
	public void addGroup(Group group) {
		for (Circuit circuit : group.getGroupCircuits()) {
			addCircuit(group.getName(circuit), circuit);
		}
	}

	/**
	 * Removes all {@link Circuit}s of the specified {@link Group}.
	 *
	 * @param group
	 *          the {@code Group} whose {@code Circuit}s will be removed
	 */
	public void removeGroup(Group group) {
		for (Circuit circuit : group.getGroupCircuits()) {
			removeCircuit(group.getName(circuit));
		}
	}
}