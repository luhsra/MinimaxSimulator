package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link LayoutManager}.
 *
 * @author Martin L&uuml;ck
 */
public class DefaultLayoutManager implements LayoutManager {

	private final ConstraintContainer	_container;
	private final Map<String, Layout>	_layouts;

	/**
	 * Constructs a new {@code DefaultLayoutManager} for the specified {@link ConstraintContainer}.
	 *
	 * @param container
	 *          the {@code ConstraintContainer} to use
	 */
	public DefaultLayoutManager(ConstraintContainer container) {
		_container = container;
		_layouts = new HashMap<String, Layout>();
	}

	@Override
	public Layout putLayout(String name, Layout layout) {
		if (layout == null) {
			throw new NullPointerException("layout must not be null");
		}

		Layout l = _layouts.put(name, layout);
		refreshConstraints(name, layout);
		return l;
	}

	@Override
	public Layout removeLayout(String name) {
		Layout layout = _layouts.remove(name);
		if (layout != null) {
			_container.clearConstraints(name);
		}
		return layout;
	}

	@Override
	public void updateLayout(String name) {
		Layout layout = _layouts.get(name);
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
		_container.clearConstraints(name);
		for (AttributeType attribute : layout.getConstrainedAttributes()) {
			Constraint constraint = layout.getConstraint(attribute);
			_container.setConstraint(name, attribute, constraint);
		}
	}

	@Override
	public void addConstraint(String owner, AttributeType attribute, Constraint con) {
		_container.addConstraint(owner, attribute, con);
	}

	@Override
	public void setConstraint(String owner, AttributeType attribute, Constraint con) {
		_container.setConstraint(owner, attribute, con);
	}

	@Override
	public void clearConstraints(String owner) {
		_container.clearConstraints(owner);
	}

	@Override
	public void removeConstraint(String owner, AttributeType attribute) {
		_container.removeConstraint(owner, attribute);
	}

	@Override
	public ConstraintFactory createConstraintFactory() {
		return _container.createConstraintFactory();
	}
}