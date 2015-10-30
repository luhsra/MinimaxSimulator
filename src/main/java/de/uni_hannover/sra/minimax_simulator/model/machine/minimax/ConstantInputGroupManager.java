package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;


import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.ConstantMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.MuxInputManager.InputEntry;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.Group;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.MuxConstantGroup;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.GroupLayout;

import java.util.*;

/**
 * Implementation of the {@link MuxInputGroupManager} for {@link ConstantMuxInput}s.
 *
 * @author Martin L&uuml;ck
 */
class ConstantInputGroupManager implements MuxInputGroupManager {

	private final Set<String> defaultGroupSet;

	private final String layoutGroupName;
	private final GroupManager groupManager;
	private final MinimaxLayout layout;

	private final Set<String> namesOfConstants;

	private final Map<MuxType, List<InputEntry>> inputEntries;

	/**
	 * Constructs a new {@code ConstantInputGroupManager} with the specified layout group name, default group set and {@link MinimaxMachine}.
	 *
	 * @param layoutGroupName
	 *          the name of the group layout
	 * @param defaultGroupSet
	 *          the default group set
	 * @param machine
	 *          the {@code MinimaxMachine}
	 */
	public ConstantInputGroupManager(String layoutGroupName, Collection<String> defaultGroupSet, MinimaxMachine machine) {
		this.defaultGroupSet = new HashSet<String>(defaultGroupSet);
		this.layoutGroupName = layoutGroupName;
		groupManager = machine.getGroupManager();
		layout = machine.getLayout();

		namesOfConstants = new HashSet<String>();

		inputEntries = new HashMap<MuxType, List<InputEntry>>();
		for (MuxType type : MuxType.values())
			inputEntries.put(type, new ArrayList<InputEntry>());

		updateLayout();
	}

	@Override
	public void update(MuxInputManager muxInputs) {
		List<InputEntry> entries = inputEntries.get(muxInputs.getMuxType());

		destroyGroups(entries);
		entries.clear();
		entries.addAll(muxInputs.getMuxInputs());
		createGroups(entries);

		updateLayout();
	}

	private void createGroups(List<InputEntry> entries) {
		for (InputEntry entry : entries) {
			if (entry.input instanceof ConstantMuxInput) {
				int value = ((ConstantMuxInput) entry.input).getConstant();
				Group group = new MuxConstantGroup(entry.pinId, entry.pin, value);

				groupManager.initializeGroup(entry.pinId + Parts._CONSTANT, group);
				namesOfConstants.add(entry.pinId + Parts._CONSTANT);
			}
		}
	}

	private void destroyGroups(List<InputEntry> entries) {
		for (InputEntry entry : entries) {
			if (entry.input instanceof ConstantMuxInput) {
				groupManager.removeGroup(entry.pinId + Parts._CONSTANT);
				namesOfConstants.remove(entry.pinId + Parts._CONSTANT);
			}
		}
	}

	private void updateLayout() {
		Set<String> constants;
		if (namesOfConstants.isEmpty()) {
			constants = defaultGroupSet;
		}
		else {
			constants = namesOfConstants;
		}

		layout.putLayout(layoutGroupName, new GroupLayout(constants));
	}
}