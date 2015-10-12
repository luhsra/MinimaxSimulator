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

class NullInputGroupManager implements MuxInputGroupManager
{
	private final GroupManager						_groupManager;

	private final Map<MuxType, List<InputEntry>>	_inputEntries;

	public NullInputGroupManager(GroupManager groupManager)
	{
		_groupManager = groupManager;

		_inputEntries = new HashMap<MuxType, List<InputEntry>>();
		for (MuxType type : MuxType.values())
			_inputEntries.put(type, new ArrayList<InputEntry>());
	}

	@Override
	public void update(MuxInputManager muxInputs)
	{
		List<InputEntry> entries = _inputEntries.get(muxInputs.getMuxType());

		destroyGroups(entries);
		entries.clear();
		entries.addAll(muxInputs.getMuxInputs());
		createGroups(entries);
	}

	private void createGroups(List<InputEntry> entries)
	{
		for (InputEntry entry : entries)
		{
			if (entry.input instanceof NullMuxInput)
			{
				Group group = new MuxNullGroup(entry.pinId, entry.pin);
				_groupManager.initializeGroup(entry.pinId + Parts._CONSTANT, group);
			}
		}
	}

	private void destroyGroups(List<InputEntry> entries)
	{
		for (InputEntry entry : entries)
		{
			if (entry.input instanceof NullMuxInput)
			{
				_groupManager.removeGroup(entry.pinId + Parts._CONSTANT);
			}
		}
	}
}