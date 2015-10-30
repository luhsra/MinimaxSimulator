package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group;

import de.uni_hannover.sra.minimax_simulator.ui.layout.Component;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Wire;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;

import java.util.*;

/**
 * Basic implementation of {@link Group}.
 *
 * @author Martin L&uuml;ck
 */
public abstract class AbstractGroup implements Group {

	private final Set<Circuit> circuitSet = new HashSet<Circuit>();
	private final Set<SpriteOwner> spriteSet = new HashSet<SpriteOwner>();
	private final Set<Component> componentSet = new HashSet<Component>();

	private final Set<String> virtualsSet = new HashSet<String>();

	private final Map<Object, String> names = new HashMap<Object, String>();

	/**
	 * Adds the specified {@code Object} links it with the specified name.
	 *
	 * @param object
	 *          the {@code Object} to add
	 * @param name
	 *          the name of the {@code Object}
	 */
	protected void add(Object object, String name) {
		if (object instanceof Circuit) {
			addCircuit((Circuit) object);
		}
		if (object instanceof SpriteOwner) {
			addSprite((SpriteOwner) object);
		}
		if (object instanceof Component) {
			addComponent((Component) object);
		}
		if (object instanceof Wire) {
			addWireComponents((Wire) object, name);
		}
		names.put(object, name);
	}

	/**
	 * Adds the {@link Component}s of the specified {@link Wire}.
	 * Their name will be computed using the specified name.
	 *
	 * @param wire
	 *          the {@code Wire} whose {@code Component}s will be added
	 * @param name
	 *          the name of the {@code Wire}
	 */
	private void addWireComponents(Wire wire, String name) {
		int index = 0;
		for (Component component : wire.createWireComponents()) {
			add(component, name + "." + index);
			index++;
		}
	}

	/**
	 * Adds the specified {@link Wire} and links it with the specified name.
	 *
	 * @param wire
	 *          the {@code Wire} to add
	 * @param name
	 *          the name of the {@code Wire}
	 */
	protected void addWire(Wire wire, String name) {
		// as Sprite and Circuit
		add(wire, name);

		int index = 0;
		for (Component component : wire.createWireComponents()) {
			add(component, name + "." + index);
			index++;
		}
	}

	/**
	 * Adds a {@link Circuit}.
	 *
	 * @param circuit
	 *          the {@code Circuit} to add
	 */
	protected void addCircuit(Circuit circuit) {
		circuitSet.add(circuit);
	}

	/**
	 * Adds a {@link Component}.
	 *
	 * @param component
	 *          the {@code Component} to add
	 */
	protected void addComponent(Component component) {
		componentSet.add(component);
	}

	/**
	 * Adds a sprite ({@link SpriteOwner}).
	 *
	 * @param sprite
	 *          the sprite to add
	 */
	protected void addSprite(SpriteOwner sprite) {
		spriteSet.add(sprite);
	}

	/**
	 * Adds a virtual component.
	 *
	 * @param name
	 *          the name of the virtual component
	 */
	protected void addVirtual(String name) {
		virtualsSet.add(name);
	}

	@Override
	public String getName(Object object) {
		return names.get(object);
	}

	@Override
	public Set<Circuit> getGroupCircuits() {
		return Collections.unmodifiableSet(circuitSet);
	}

	@Override
	public Set<SpriteOwner> getSpriteOwners() {
		return Collections.unmodifiableSet(spriteSet);
	}

	@Override
	public Set<Component> getComponents() {
		return Collections.unmodifiableSet(componentSet);
	}

	@Override
	public Set<String> getVirtualComponents() {
		return Collections.unmodifiableSet(virtualsSet);
	}

	@Override
	public boolean hasLayouts() {
		return false;
	}

	@Override
	public LayoutSet createLayouts() {
		throw new IllegalStateException();
	}
}