package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.uni_hannover.sra.minimax_simulator.layout.Component;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Wire;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;

public abstract class AbstractGroup implements Group
{
	private final Set<Circuit>			_circuitSet		= new HashSet<Circuit>();
	private final Set<SpriteOwner>		_spriteSet		= new HashSet<SpriteOwner>();
	private final Set<Component>		_componentSet	= new HashSet<Component>();

	private final Set<String>			_virtualsSet	= new HashSet<String>();

	private final Map<Object, String>	_names			= new HashMap<Object, String>();

	protected void add(Object object, String name)
	{
		if (object instanceof Circuit)
		{
			addCircuit((Circuit) object);
		}
		if (object instanceof SpriteOwner)
		{
			addSprite((SpriteOwner) object);
		}
		if (object instanceof Component)
		{
			addComponent((Component) object);
		}
		if (object instanceof Wire)
		{
			addWireComponents((Wire) object, name);
		}
		_names.put(object, name);
	}

	private void addWireComponents(Wire wire, String name)
	{
		int index = 0;
		for (Component component : wire.createWireComponents())
		{
			add(component, name + "." + index);
			index++;
		}
	}

	protected void addWire(Wire wire, String name)
	{
		// As Sprite and Circuit
		add(wire, name);

		int index = 0;
		for (Component component : wire.createWireComponents())
		{
			add(component, name + "." + index);
			index++;
		}
	}

	protected void addCircuit(Circuit circuit)
	{
		_circuitSet.add(circuit);
	}

	protected void addComponent(Component component)
	{
		_componentSet.add(component);
	}

	protected void addSprite(SpriteOwner sprite)
	{
		_spriteSet.add(sprite);
	}

	protected void addVirtual(String name)
	{
		_virtualsSet.add(name);
	}

	@Override
	public String getName(Object object)
	{
		return _names.get(object);
	}

	@Override
	public Set<Circuit> getGroupCircuits()
	{
		return Collections.unmodifiableSet(_circuitSet);
	}

	@Override
	public Set<SpriteOwner> getSpriteOwners()
	{
		return Collections.unmodifiableSet(_spriteSet);
	}

	@Override
	public Set<Component> getComponents()
	{
		return Collections.unmodifiableSet(_componentSet);
	}

	@Override
	public Set<String> getVirtualComponents()
	{
		return Collections.unmodifiableSet(_virtualsSet);
	}

	@Override
	public boolean hasLayouts()
	{
		return false;
	}

	@Override
	public LayoutSet createLayouts()
	{
		throw new IllegalStateException();
	}
}