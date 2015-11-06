package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import de.uni_hannover.sra.minimax_simulator.ui.layout.*;
import de.uni_hannover.sra.minimax_simulator.util.toposort.SimpleTopologicalSorter;
import de.uni_hannover.sra.minimax_simulator.util.toposort.TopologicalSorter;

import java.util.*;
import java.util.Map.Entry;

/**
 * A {@link Container} for {@link Constraint}s.
 *
 * @author Martin L&uuml;ck
 */
public class ConstraintContainer extends Container implements ConstraintsManager {

	// every Component is an AttributeOwner, but some (virtual) AttributeOwners have no corresponding Component
	private final Map<String, AttributeOwner> attributeOwners;
	private final Map<String, Component> components;
	private final Map<Component, String> namesOfComponents;

	private final AttributeSource source;

	private List<Attribute> sortedAttributes;
	private Point offset;

	/**
	 * Constructs a new {@code ConstraintContainer}.
	 */
	public ConstraintContainer() {
		attributeOwners = new HashMap<String, AttributeOwner>();
		components = new HashMap<String, Component>();
		namesOfComponents = new HashMap<Component, String>();

		sortedAttributes = Collections.emptyList();
		offset = new Point(0, 0);

		source = new AttributeSource() {
			@Override
			public int getValue(Attribute attribute) {
				AttributeOwner owner = attributeOwners.get(attribute.getOwner());
				if (owner == null) {
					throw new IllegalStateException("Attribute owner not existing: " + attribute.getOwner());
				}

				return owner.get(attribute.getType());
			}
		};
	}

	@Override
	public void updateSize() {
		components.values().forEach(Component::updateSize);

		if (sortedAttributes == null) {
			// recollect attributes and dependencies
			sortedAttributes = orderAttributes();
		}

		resolveAttributes();

		int minLeft = Integer.MAX_VALUE;
		int minTop = Integer.MAX_VALUE;
		int maxRight = Integer.MIN_VALUE;
		int maxBottom = Integer.MIN_VALUE;

		if (attributeOwners.isEmpty()) {
			minLeft = minTop = maxRight = maxBottom = 0;
		}
		else {
			for (AttributeOwner owner : attributeOwners.values()) {
				if (owner.get(AttributeType.LEFT) < minLeft) {
					minLeft = owner.get(AttributeType.LEFT);
				}
				if (owner.get(AttributeType.TOP) < minTop) {
					minTop = owner.get(AttributeType.TOP);
				}
				if (owner.get(AttributeType.RIGHT) > maxRight) {
					maxRight = owner.get(AttributeType.RIGHT);
				}
				if (owner.get(AttributeType.BOTTOM) > maxBottom) {
					maxBottom = owner.get(AttributeType.BOTTOM);
				}
			}
		}

		Insets insets = getInsets();

		offset = new Point(minLeft - insets.l, minTop - insets.t);
		setDimension(new Dimension(maxRight - minLeft + insets.l + insets.r, maxBottom - minTop + insets.t + insets.b));
	}

	/**
	 * Orders the {@link Attribute}s of the container using a {@link TopologicalSorter}.
	 *
	 * @return
	 *          a list of the ordered {@code Attribute}s
	 */
	private List<Attribute> orderAttributes() {
		// estimation for the list length
		Map<Attribute, Set<Attribute>> attributes = new HashMap<Attribute, Set<Attribute>>(attributeOwners.size());

		for (AttributeOwner owner : attributeOwners.values()) {
			for (Attribute attribute : owner.getAttributes()) {
				attributes.put(attribute, owner.getDependencies(attribute.getType()));
			}
		}

		TopologicalSorter sorter = new SimpleTopologicalSorter();
		return sorter.sort(attributes);
	}

	/**
	 * Gets the {@link Bounds} of the specified {@link AttributeOwner}.
	 *
	 * @param attr
	 *          the {@code AttributeOwner}
	 * @return
	 *          the {@code Bounds} of the {@code AttributeOwner}
	 */
	private Bounds toBounds(AttributeOwner attr) {
		return new Bounds(attr.get(AttributeType.LEFT) - offset.x,
			attr.get(AttributeType.TOP) - offset.y, attr.get(AttributeType.WIDTH),
			attr.get(AttributeType.HEIGHT));
	}

	@Override
	public void doLayout() {
		for (Entry<String, Component> entry : components.entrySet()) {
			AttributeOwner attr = attributeOwners.get(entry.getKey());
			entry.getValue().setBounds(toBounds(attr));
			entry.getValue().doLayout();
		}
	}

	/**
	 * Resolves the {@link Attribute}s of the container.
	 */
	private void resolveAttributes() {
		for (AttributeOwner owner : attributeOwners.values()) {
			owner.clearAttributes();
			owner.validateConstraints();
		}

		for (Attribute attr : sortedAttributes) {
			AttributeOwner owner = attributeOwners.get(attr.getOwner());
			if (owner == null) {
				throw new IllegalStateException("Missing AttributeOwner for attribute on " + attr.getOwner());
			}

			owner.computeAttribute(attr.getType(), source);
		}
	}

	/**
	 * Adds a virtual {@link Component} to the container.
	 *
	 * @param id
	 *          the ID of the virtual {@code Component}
	 */
	public void addVirtualComponent(String id) {
		if (id == null) {
			throw new IllegalArgumentException("Component id must not be null");
		}

		addComponent(null, id);
	}

	@Override
	public void addComponent(Component component, Object constraint) {
		if (!(constraint instanceof String)) {
			throw new IllegalArgumentException("Component id must be a string");
		}

		String id = (String) constraint;

		if (component != null) {
			if (namesOfComponents.containsKey(component)) {
				throw new IllegalArgumentException("Component " + id + " already existing in layout under name: " + namesOfComponents.get(component));
			}

			components.put(id, component);
			namesOfComponents.put(component, id);
			attributeOwners.put(id, new ConstrainedComponent(id, component));
		}
		else {
			attributeOwners.put(id, new ConstrainedArea(id));
		}

		sortedAttributes = null;
	}

	@Override
	public void removeComponent(Component component) {
		String name = namesOfComponents.remove(component);
		if (name != null) {
			components.remove(name);
			attributeOwners.remove(name);

			sortedAttributes = null;
		}
	}

	/**
	 * Removes the {@link Component} with the specified name.
	 *
	 * @param name
	 *          the name of the {@code Component} to remove
	 */
	public void removeComponent(String name) {
		components.remove(name);
		attributeOwners.remove(name);

		sortedAttributes = null;
	}

	@Override
	public void clearConstraints(String owner) {
		AttributeOwner attrOwner = attributeOwners.get(owner);
		if (attrOwner == null) {
			throw new IllegalStateException("Attribute owner not existing: " + owner);
		}

		attrOwner.clearConstraints();

		sortedAttributes = null;
	}

	@Override
	public void setConstraint(String owner, AttributeType attribute, Constraint con) {
		if (con == null) {
			throw new IllegalArgumentException("Null constraint not allowed");
		}
		if (attribute == null) {
			throw new IllegalArgumentException("Null attribute not allowed");
		}

		AttributeOwner attrOwner = attributeOwners.get(owner);
		if (attrOwner == null) {
			throw new IllegalArgumentException("Attribute owner not found: " + owner);
		}

		attrOwner.setAttributeConstraint(attribute, con);

		sortedAttributes = null;
	}

	@Override
	public void addConstraint(String owner, AttributeType attribute, Constraint con) {
		if (con == null) {
			throw new IllegalArgumentException("Null constraint not allowed");
		}
		if (attribute == null) {
			throw new IllegalArgumentException("Null attribute not allowed");
		}

		AttributeOwner attrOwner = attributeOwners.get(owner);
		if (attrOwner == null) {
			throw new IllegalArgumentException("Attribute owner not found: " + owner);
		}

		if (attrOwner.getAttributeConstraint(attribute) != null) {
			throw new IllegalArgumentException(owner + " is already constrained in the " + attribute + " attribute");
		}

		attrOwner.setAttributeConstraint(attribute, con);

		sortedAttributes = null;
	}

	@Override
	public void removeConstraint(String owner, AttributeType attribute) {
		if (attribute == null) {
			throw new IllegalArgumentException("Null attribute not allowed");
		}

		AttributeOwner attrOwner = attributeOwners.get(owner);
		if (attrOwner == null) {
			throw new IllegalArgumentException("Attribute owner not found: " + owner);
		}

		attrOwner.removeAttributeConstraint(attribute);

		sortedAttributes = null;
	}

	@Override
	public ConstraintFactory createConstraintFactory() {
		return new ConstraintFactory() {
			@Override
			protected ConstraintsManager getManager() {
				return ConstraintContainer.this;
			}
		};
	}
}