package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.Group;

/**
 * Manages {@link Group}s.
 *
 * @author Martin L&uuml;ck
 */
interface GroupManager {

	/**
	 * Initializes the specified {@link Group}.
	 *
	 * @param id
	 *          the ID of the {@code Group}
	 * @param group
	 *          the {@code Group}
	 */
	public void initializeGroup(String id, Group group);

	/**
	 * Removes the specified {@link Group} from the manager.
	 *
	 * @param id
	 *          the ID of the {@code Group}
	 */
	public void removeGroup(String id);
}