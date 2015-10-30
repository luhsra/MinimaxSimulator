package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link LayoutManager}.
 *
 * @author Martin L&uuml;ck
 */
public class DefaultLayoutManager implements LayoutManager {

	private final ConstraintContainer container;
	private final Map<String, Layout> layouts;

	/**
	 * Constructs a new {@code DefaultLayoutManager} for the specified {@link ConstraintContainer}.
	 *
	 * @param container
	 *          the {@code ConstraintContainer} to use
	 */
	public DefaultLayoutManager(ConstraintContainer container) {
		this.container = container;
		layouts = new HashMap<String, Layout>();
	}

	@Override
	public Layout putLayout(String name, Layout layout) {
		if (layout == null) {
			throw new NullPointerException("layout must not be null");
		}

		Layout l = layouts.put(name, layout);
		refreshConstraints(name, layout);
		return l;
	}

	@Override
	public Layout removeLayout(String name) {
		Layout layout = layouts.remove(name);
		if (layout != null) {
			container.clearConstraints(name);
		}
		return layout;
	}

	@Override
	public void updateLayout(String name) {
		Layout layout = layouts.get(name);
		if (layout == null) {
			throw new IllegalArgumentException("Component has no layout: " + name);
		}

		refreshConstraints(name, layout);
	}

	/**
	 * Refreshes the {@link Constraint}s with the specified name using the {@link Layout}.
	 *
	 * @param name
	 *          the name of the {@code Constraint}s to refresh
	 * @param layout
	 *          the {@code Layout} to use for getting the new {@code Constraint}s
	 */
	private void refreshConstraints(String name, Layout layout) {
		container.clearConstraints(name);
		for (AttributeType attribute : layout.getConstrainedAttributes()) {
			Constraint constraint = layout.getConstraint(attribute);
			container.setConstraint(name, attribute, constraint);
		}
	}

	@Override
	public void addConstraint(String owner, AttributeType attribute, Constraint con) {
		container.addConstraint(owner, attribute, con);
	}

	@Override
	public void setConstraint(String owner, AttributeType attribute, Constraint con) {
		container.setConstraint(owner, attribute, con);
	}

	@Override
	public void clearConstraints(String owner) {
		container.clearConstraints(owner);
	}

	@Override
	public void removeConstraint(String owner, AttributeType attribute) {
		container.removeConstraint(owner, attribute);
	}

	@Override
	public ConstraintFactory createConstraintFactory() {
		return container.createConstraintFactory();
	}
}