package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.layout.constraint.ConstraintBuilder;
import de.uni_hannover.sra.minimax_simulator.layout.constraint.Layout;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultLayoutSet implements LayoutSet
{
	private final Map<String, Layout> _layouts = new HashMap<String, Layout>();

	public void addLayout(String name, Layout layout)
	{
		if (_layouts.containsKey(name))
			throw new IllegalStateException();

		_layouts.put(name, layout);
	}

	public void addLayout(String name, ConstraintBuilder cb)
	{
		if (_layouts.containsKey(name))
			throw new IllegalStateException("Duplicate layout: " + name);

		_layouts.put(name, new DefaultLayout(cb.constraints()));
		cb.clear();
	}

	@Override
	public Set<String> getComponents()
	{
		return _layouts.keySet();
	}

	@Override
	public Layout getLayout(String component)
	{
		return _layouts.get(component);
	}
}