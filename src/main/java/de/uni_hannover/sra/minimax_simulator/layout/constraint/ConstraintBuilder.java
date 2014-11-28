package de.uni_hannover.sra.minimax_simulator.layout.constraint;

import java.util.EnumMap;
import java.util.Map;

public class ConstraintBuilder
{
	private final Map<AttributeType, Constraint>	_constraints;

	public ConstraintBuilder()
	{
		_constraints = new EnumMap<AttributeType, Constraint>(AttributeType.class);
	}

	public Map<AttributeType, Constraint> constraints()
	{
		return _constraints;
	}

	public ConstraintBuilder clear()
	{
		_constraints.clear();
		return this;
	}

	public ConstraintBuilder relative(AttributeType targetAttr, String source,
			AttributeType sourceAttr, int offset)
	{
		_constraints.put(targetAttr, new RelativeConstraint(source, sourceAttr, offset));
		return this;
	}

	public ConstraintBuilder relative(AttributeType targetAttr, String source, AttributeType sourceAttr)
	{
		_constraints.put(targetAttr, new RelativeConstraint(source, sourceAttr));
		return this;
	}

	public ConstraintBuilder relative(AttributeType attr, String source, int offset)
	{
		_constraints.put(attr, new RelativeConstraint(source, attr, offset));
		return this;
	}

	public ConstraintBuilder relative(AttributeType attr, String source)
	{
		_constraints.put(attr, new RelativeConstraint(source, attr));
		return this;
	}

	public ConstraintBuilder absolute(AttributeType targetAttr, int offset)
	{
		_constraints.put(targetAttr, new AbsoluteConstraint(offset));
		return this;
	}

	public ConstraintBuilder below(String source, int offset)
	{
		_constraints.put(AttributeType.TOP, new RelativeConstraint(source,
			AttributeType.BOTTOM, offset));
		return this;
	}

	public ConstraintBuilder below(String source)
	{
		_constraints.put(AttributeType.TOP, new RelativeConstraint(source,
			AttributeType.BOTTOM, 0));
		return this;
	}

	public ConstraintBuilder above(String source, int offset)
	{
		_constraints.put(AttributeType.BOTTOM, new RelativeConstraint(source,
			AttributeType.TOP, -offset));
		return this;
	}

	public ConstraintBuilder above(String source)
	{
		_constraints.put(AttributeType.BOTTOM, new RelativeConstraint(source,
			AttributeType.TOP, 0));
		return this;
	}

	public ConstraintBuilder left(String source, int offset)
	{
		_constraints.put(AttributeType.RIGHT, new RelativeConstraint(source,
			AttributeType.LEFT, -offset));
		return this;
	}

	public ConstraintBuilder left(String source)
	{
		_constraints.put(AttributeType.RIGHT, new RelativeConstraint(source,
			AttributeType.LEFT, 0));
		return this;
	}

	public ConstraintBuilder right(String source, int offset)
	{
		_constraints.put(AttributeType.LEFT, new RelativeConstraint(source,
			AttributeType.RIGHT, offset));
		return this;
	}

	public ConstraintBuilder right(String source)
	{
		_constraints.put(AttributeType.LEFT, new RelativeConstraint(source,
			AttributeType.RIGHT, 0));
		return this;
	}

	public ConstraintBuilder alignHorizontally(String source)
	{
		_constraints.put(AttributeType.HORIZONTAL_CENTER, new RelativeConstraint(source,
			AttributeType.HORIZONTAL_CENTER));
		return this;
	}

	public ConstraintBuilder alignVertically(String source)
	{
		_constraints.put(AttributeType.VERTICAL_CENTER, new RelativeConstraint(source,
			AttributeType.VERTICAL_CENTER));
		return this;
	}

	public ConstraintBuilder align(String source)
	{
		alignHorizontally(source);
		return alignVertically(source);
	}
}