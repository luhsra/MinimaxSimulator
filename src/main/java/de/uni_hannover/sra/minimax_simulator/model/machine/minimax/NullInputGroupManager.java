package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;


import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.NullMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.MuxInputManager.InputEntry;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.Group;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.MuxNullGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link MuxInputGroupManager} for {@link NullMuxInput}s.
 *
 * @author Martin L&uuml;ck
 */
class NullInputGroupManager implements MuxInputGroupManager {

	private final GroupManager groupManager;

	private final Map<MuxType, List<InputEntry>> inputEntries;

	/**
	 * Constructs a new {@code NullInputGroupManager} with the specified {@link GroupManager}.
	 *
	 * @param groupManager
	 *          the {@code GroupManager}
	 */
	public NullInputGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;

		inputEntries = new HashMap<MuxType, List<InputEntry>>();
		for (MuxType type : MuxType.values()) {
			inputEntries.put(type, new ArrayList<InputEntry>());
		}
	}

	@Override
	public void update(MuxInputManager muxInputs) {
		List<InputEntry> entries = inputEntries.get(muxInputs.getMuxType());

		destroyGroups(entries);
		entries.clear();
		entries.addAll(muxInputs.getMuxInputs());
		createGroups(entries);
	}

	/**
	 * Creates {@link MuxNullGroup}s for the {@link NullMuxInput}s of the specified entries.
	 *
	 * @param entries
	 *          a list of {@link InputEntry} to create {@code Group}s for
	 */
	private void createGroups(List<InputEntry> entries) {
		for (InputEntry entry : entries) {
			if (entry.input instanceof NullMuxInput) {
				Group group = new MuxNullGroup(entry.pinId, entry.pin);
				groupManager.initializeGroup(entry.pinId + Parts._CONSTANT, group);
			}
		}
	}

	/**
	 * Removes the {@link MuxNullGroup}s of the {@link NullMuxInput}s of the specified entries.
	 *
	 * @param entries
	 *          a list of {@link InputEntry} to destroy {@code Group}s for
	 */
	private void destroyGroups(List<InputEntry> entries) {
		for (InputEntry entry : entries) {
			if (entry.input instanceof NullMuxInput) {
				groupManager.removeGroup(entry.pinId + Parts._CONSTANT);
			}
		}
	}
}