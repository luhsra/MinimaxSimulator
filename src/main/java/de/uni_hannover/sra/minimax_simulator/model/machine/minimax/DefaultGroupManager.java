package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import java.util.HashMap;
import java.util.Map;

import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.Group;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;

class DefaultGroupManager implements GroupManager
{
	private final MinimaxLayout		_layout;
	private final MinimaxTopology	_topology;
	private final MinimaxDisplay	_display;

	private final Map<String, Group>		_groups		= new HashMap<String, Group>();
	private final Map<String, LayoutSet>	_layouts	= new HashMap<String, LayoutSet>();

	DefaultGroupManager(MinimaxLayout layout, MinimaxTopology topology,
			MinimaxDisplay display)
	{
		_layout = layout;
		_topology = topology;
		_display = display;
	}

	@Override
	public void removeGroup(String id)
	{
		Group group = _groups.remove(id);
		if (group == null)
			throw new IllegalArgumentException("Unknown group: " + id);

		LayoutSet set = _layouts.remove(id);
		if (set != null)
			_layout.removeLayouts(set);

		_display.removeGroup(group);
		_layout.removeGroup(group);
		_topology.removeGroup(group);
	}

	@Override
	public void initializeGroup(String id, Group group)
	{
		if (_groups.containsKey(id))
			throw new IllegalArgumentException("Group already registered: " + id);

		_groups.put(id, group);

		group.initialize(_topology, _display);
		_topology.addGroup(group);
		_layout.addGroup(group);
		_display.addGroup(group);

		if (group.hasLayouts())
		{
			LayoutSet set = group.createLayouts();
			_layouts.put(id, set);
			_layout.putLayouts(group.createLayouts());
		}
	}
}