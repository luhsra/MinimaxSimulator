package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import de.uni_hannover.sra.minimax_simulator.ui.layout.*;
import de.uni_hannover.sra.minimax_simulator.util.toposort.SimpleTopologicalSorter;
import de.uni_hannover.sra.minimax_simulator.util.toposort.TopologicalSorter;

import java.util.*;
import java.util.Map.Entry;

// TODO: insets?
public class ConstraintContainer extends Container implements ConstraintsManager
{
	// Every Component is an AttributeOwner, but some (virtual) AttributeOwners
	// have no corresponding Component
	private final Map<String, AttributeOwner>	_attributeOwners;
	private final Map<String, Component>		_components;
	private final Map<Component, String>		_namesOfComponents;

	private final AttributeSource				_source;

	private List<Attribute>						_sortedAttributes;
	private Point								_offset;

	public ConstraintContainer()
	{
		_attributeOwners = new HashMap<String, AttributeOwner>();
		_components = new HashMap<String, Component>();
		_namesOfComponents = new HashMap<Component, String>();

		_sortedAttributes = Collections.emptyList();
		_offset = new Point(0, 0);

		_source = new AttributeSource()
		{
			@Override
			public int getValue(Attribute attribute)
			{
				AttributeOwner owner = _attributeOwners.get(attribute.getOwner());
				if (owner == null)
					throw new IllegalStateException("Attribute owner not existing: "
						+ attribute.getOwner());

				return owner.get(attribute.getType());
			}
		};
	}

	@Override
	public void updateSize()
	{
		for (Component component : _components.values())
			component.updateSize();

		if (_sortedAttributes == null)
		{
			// recollect attributes and dependencies
			_sortedAttributes = orderAttributes();
		}

		resolveAttributes();

		int minLeft = Integer.MAX_VALUE;
		int minTop = Integer.MAX_VALUE;
		int maxRight = Integer.MIN_VALUE;
		int maxBottom = Integer.MIN_VALUE;

		if (_attributeOwners.isEmpty())
		{
			minLeft = minTop = maxRight = maxBottom = 0;
		}
		else
		{
			for (AttributeOwner owner : _attributeOwners.values())
			{
				if (owner.get(AttributeType.LEFT) < minLeft)
					minLeft = owner.get(AttributeType.LEFT);
				if (owner.get(AttributeType.TOP) < minTop)
					minTop = owner.get(AttributeType.TOP);
				if (owner.get(AttributeType.RIGHT) > maxRight)
					maxRight = owner.get(AttributeType.RIGHT);
				if (owner.get(AttributeType.BOTTOM) > maxBottom)
					maxBottom = owner.get(AttributeType.BOTTOM);
			}
		}

		Insets insets = getInsets();

		_offset = new Point(minLeft - insets.l, minTop - insets.t);
		setDimension(new Dimension(maxRight - minLeft + insets.l + insets.r, maxBottom
			- minTop + insets.t + insets.b));
	}

	private List<Attribute> orderAttributes()
	{
		// Estimation for the list length
		Map<Attribute, Set<Attribute>> attributes = new HashMap<Attribute, Set<Attribute>>(
			_attributeOwners.size());

		for (AttributeOwner owner : _attributeOwners.values())
			for (Attribute attribute : owner.getAttributes())
				attributes.put(attribute, owner.getDependencies(attribute.getType()));

		TopologicalSorter sorter = new SimpleTopologicalSorter();
		return sorter.sort(attributes);
	}

	private Bounds toBounds(AttributeOwner attr)
	{
		return new Bounds(attr.get(AttributeType.LEFT) - _offset.x,
			attr.get(AttributeType.TOP) - _offset.y, attr.get(AttributeType.WIDTH),
			attr.get(AttributeType.HEIGHT));
	}

	@Override
	public void doLayout()
	{
		for (Entry<String, Component> entry : _components.entrySet())
		{
			AttributeOwner attr = _attributeOwners.get(entry.getKey());
			entry.getValue().setBounds(toBounds(attr));
			entry.getValue().doLayout();
		}
	}

	private void resolveAttributes()
	{
		for (AttributeOwner owner : _attributeOwners.values())
		{
			owner.clearAttributes();
			owner.validateConstraints();
		}

		for (Attribute attr : _sortedAttributes)
		{
			AttributeOwner owner = _attributeOwners.get(attr.getOwner());
			if (owner == null)
				throw new IllegalStateException(
					"Missing AttributeOwner for attribute on " + attr.getOwner());

			owner.computeAttribute(attr.getType(), _source);
		}
	}

	public void addVirtualComponent(String id)
	{
		if (id == null)
			throw new IllegalArgumentException("Component id must not be null");

		addComponent(null, id);
	}

	@Override
	public void addComponent(Component component, Object constraint)
	{
		if (!(constraint instanceof String))
			throw new IllegalArgumentException("Component id must be a string");

		String id = (String) constraint;

		if (component != null)
		{
			if (_namesOfComponents.containsKey(component))
				throw new IllegalArgumentException("Component " + id
					+ " already existing in layout under name: "
					+ _namesOfComponents.get(component));

			_components.put(id, component);
			_namesOfComponents.put(component, id);
			_attributeOwners.put(id, new ConstrainedComponent(id, component));
		}
		else
		{
			_attributeOwners.put(id, new ConstrainedArea(id));
		}

		_sortedAttributes = null;
	}

	@Override
	public void removeComponent(Component component)
	{
		String name = _namesOfComponents.remove(component);
		if (name != null)
		{
			_components.remove(name);
			_attributeOwners.remove(name);

			_sortedAttributes = null;
		}
	}

	public void removeComponent(String name)
	{
		_components.remove(name);
		_attributeOwners.remove(name);

		_sortedAttributes = null;
	}

	@Override
	public void clearConstraints(String owner)
	{
		AttributeOwner attrOwner = _attributeOwners.get(owner);
		if (attrOwner == null)
			throw new IllegalStateException("Attribute owner not existing: " + owner);

		attrOwner.clearConstraints();

		_sortedAttributes = null;
	}

	@Override
	public void setConstraint(String owner, AttributeType attribute, Constraint con)
	{
		if (con == null)
			throw new IllegalArgumentException("Null constraint not allowed");
		if (attribute == null)
			throw new IllegalArgumentException("Null attribute not allowed");

		AttributeOwner attrOwner = _attributeOwners.get(owner);
		if (attrOwner == null)
			throw new IllegalArgumentException("Attribute owner not found: " + owner);

		attrOwner.setAttributeConstraint(attribute, con);

		_sortedAttributes = null;
	}

	@Override
	public void addConstraint(String owner, AttributeType attribute, Constraint con)
	{
		if (con == null)
			throw new IllegalArgumentException("Null constraint not allowed");
		if (attribute == null)
			throw new IllegalArgumentException("Null attribute not allowed");

		AttributeOwner attrOwner = _attributeOwners.get(owner);
		if (attrOwner == null)
			throw new IllegalArgumentException("Attribute owner not found: " + owner);

		if (attrOwner.getAttributeConstraint(attribute) != null)
			throw new IllegalArgumentException(owner + " is already constrained in the "
				+ attribute + " attribute");

		attrOwner.setAttributeConstraint(attribute, con);

		_sortedAttributes = null;
	}

	@Override
	public void removeConstraint(String owner, AttributeType attribute)
	{
		if (attribute == null)
			throw new IllegalArgumentException("Null attribute not allowed");

		AttributeOwner attrOwner = _attributeOwners.get(owner);
		if (attrOwner == null)
			throw new IllegalArgumentException("Attribute owner not found: " + owner);

		attrOwner.removeAttributeConstraint(attribute);

		_sortedAttributes = null;
	}

	@Override
	public ConstraintFactory createConstraintFactory()
	{
		return new ConstraintFactory()
		{
			@Override
			protected ConstraintsManager getManager()
			{
				return ConstraintContainer.this;
			}
		};
	}
}