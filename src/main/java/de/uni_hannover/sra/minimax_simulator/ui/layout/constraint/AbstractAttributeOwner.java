package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

abstract class AbstractAttributeOwner implements AttributeOwner
{
	private final String								_name;

	private final EnumMap<AttributeType, Integer>		_values;
	private final EnumMap<AttributeType, Constraint>	_constraints;

	public AbstractAttributeOwner(String name)
	{
		if (name == null)
			throw new IllegalArgumentException("Name must not be null");

		_name = name;
		_values = new EnumMap<AttributeType, Integer>(AttributeType.class);
		_constraints = new EnumMap<AttributeType, Constraint>(AttributeType.class);
	}

	@Override
	public boolean hasSet(AttributeType attribute)
	{
		if (attribute == null)
			throw new NullPointerException(_name + ": attribute is null");

		return _values.containsKey(attribute);
	}

	@Override
	public void set(AttributeType attribute, int value)
	{
		if (attribute == null)
			throw new NullPointerException(_name + ": attribute is null");

		if (_values.containsKey(attribute))
			throw new IllegalStateException(_name + ": Value for " + attribute + " already set");
		_values.put(attribute, value);
	}

	@Override
	public int get(AttributeType attribute)
	{
		if (attribute == null)
			throw new NullPointerException(_name + ": attribute is null");

		if (!_values.containsKey(attribute))
			throw new IllegalStateException(_name + ": Unresolved attribute " + attribute);
		return _values.get(attribute);
	}

	@Override
	public void clearAttributes()
	{
		_values.clear();
	}

	@Override
	public void clearConstraints()
	{
		_constraints.clear();
	}

	@Override
	public Constraint getAttributeConstraint(AttributeType attribute)
	{
		if (attribute == null)
			throw new IllegalArgumentException(_name
				+ ": Attribute type must not be null");

		return _constraints.get(attribute);
	}

	@Override
	public void setAttributeConstraint(AttributeType attribute, Constraint constraint)
	{
		if (attribute == null)
			throw new IllegalArgumentException(_name
				+ ": Attribute type must not be null");
		if (constraint == null)
			throw new IllegalArgumentException(_name + ": Constraint must not be null");

		// Allow replacing existing constraint
//		if (_constraints.containsKey(attribute))
//			throw new IllegalStateException(_name + ": Duplicate constraint on "
//				+ attribute);

		Set<AttributeType> constrainedSameAxisAttrs =
				new HashSet<AttributeType>(_constraints.keySet());
		constrainedSameAxisAttrs
				.retainAll(AttributeType.getAxisTypes(attribute.getAxis()));

		// Allow replacing existing constraint
		constrainedSameAxisAttrs.remove(attribute);

		if (constrainedSameAxisAttrs.size() >= 2)
			throw new IllegalStateException(_name + ": At most 2 constraints on the "
				+ attribute.getAxis() + " axis allowed");

		_constraints.put(attribute, constraint);
	}

	@Override
	public void removeAttributeConstraint(AttributeType attribute)
	{
		if (attribute == null)
			throw new IllegalArgumentException(_name
				+ ": Attribute type must not be null");

		_constraints.remove(attribute);
	}

	@Override
	public void validateConstraints()
	{
		Set<AttributeType> axisConstraints =
				new HashSet<AttributeType>(_constraints.keySet());
		axisConstraints.retainAll(AttributeType.getAxisTypes(AttributeAxis.HORIZONTAL));
		if (axisConstraints.size() < 1
			|| (axisConstraints.size() == 1 && axisConstraints
					.contains(AttributeType.WIDTH)))
		{
			throw new IllegalStateException(_name + ": underconstrained in x axis");
		}
		axisConstraints.addAll(_constraints.keySet());
		axisConstraints.retainAll(AttributeType.getAxisTypes(AttributeAxis.VERTICAL));
		if (axisConstraints.size() < 1
			|| (axisConstraints.size() == 1 && axisConstraints
					.contains(AttributeType.HEIGHT)))
		{
			throw new IllegalStateException(_name + ": is underconstrained in y axis");
		}
	}

	@Override
	public String getName()
	{
		return _name;
	}

	@Override
	public void computeAttribute(AttributeType attribute, AttributeSource source)
	{
		if (source == null)
			throw new NullPointerException(_name + ": source is null");

		Constraint constraint = _constraints.get(attribute);
		if (constraint != null)
		{
			set(attribute, constraint.getValue(source));
		}
		else
		{
			set(attribute, attribute.deriveValue(this));
		}
	}

	@Override
	public Set<Attribute> getAttributes()
	{
		Set<Attribute> attrs = new HashSet<Attribute>();
		for (AttributeType type : AttributeType.values())
		{
			attrs.add(new Attribute(getName(), type));
		}
		return attrs;
	}

	@Override
	public Set<Attribute> getDependencies(AttributeType attribute)
	{
		Set<Attribute> attrs = new HashSet<Attribute>();

		Constraint constraint = _constraints.get(attribute);

		if (constraint != null)
		{
			// If an attribute is constrained, it depends on some attributes of
			// (other) AttributeOwners
			for (Attribute dependency : constraint.getDependencies())
				attrs.add(dependency);
		}
		else
		{
			// If the attribute is unconstrained, it implicitely depends
			// on all constrained attributes on the same axis, since they are
			// required for attribute derivation.
			// These are dependencies on the instance itself.
			for (AttributeType constrainedAttr : _constraints.keySet())
				if (constrainedAttr.getAxis() == attribute.getAxis())
					attrs.add(new Attribute(_name, constrainedAttr));
		}

		return attrs;
	}

	@Override
	public String toString()
	{
		return _name;
	}
}