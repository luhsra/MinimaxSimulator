package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import java.util.EnumSet;
import java.util.Set;

import de.uni_hannover.sra.minimax_simulator.layout.constraint.Attribute;
import de.uni_hannover.sra.minimax_simulator.layout.constraint.AttributeType;
import de.uni_hannover.sra.minimax_simulator.layout.constraint.Constraint;
import de.uni_hannover.sra.minimax_simulator.layout.constraint.Layout;
import de.uni_hannover.sra.minimax_simulator.layout.constraint.RelativeConstraint;

class FlowLeftLayout implements Layout
{
	private final static EnumSet<AttributeType>	attributes	= EnumSet.of(AttributeType.RIGHT,
																AttributeType.VERTICAL_CENTER);

	private final Constraint					_vertical;
	private final Constraint					_horizontal;

	FlowLeftLayout(String anchor, int spacing)
	{
		_horizontal = new RelativeConstraint(new Attribute(anchor, AttributeType.LEFT),
			-spacing);
		_vertical = new RelativeConstraint(new Attribute(anchor,
			AttributeType.VERTICAL_CENTER));
	}

	@Override
	public Constraint getConstraint(AttributeType attribute)
	{
		switch (attribute)
		{
			case VERTICAL_CENTER:
				return _vertical;
			case RIGHT:
				return _horizontal;

			default:
				// never happens
				throw new AssertionError();
		}
	}

	@Override
	public Set<AttributeType> getConstrainedAttributes()
	{
		return attributes;
	}
}