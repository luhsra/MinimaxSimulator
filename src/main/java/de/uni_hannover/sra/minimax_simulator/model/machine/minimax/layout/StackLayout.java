package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.*;

import java.util.EnumSet;
import java.util.Set;

class StackLayout implements Layout
{
	private final static EnumSet<AttributeType>	attributes	= EnumSet.of(AttributeType.HORIZONTAL_CENTER,
																AttributeType.BOTTOM);

	private final Constraint					_vertical;
	private final Constraint					_horizontal;

	StackLayout(String anchor, int spacing)
	{
		_vertical = new RelativeConstraint(new Attribute(anchor, AttributeType.TOP),
			-spacing);
		_horizontal = new RelativeConstraint(new Attribute(anchor,
			AttributeType.HORIZONTAL_CENTER));
	}

	@Override
	public Constraint getConstraint(AttributeType attribute)
	{
		switch (attribute)
		{
			case HORIZONTAL_CENTER:
				return _horizontal;
			case BOTTOM:
				return _vertical;

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