package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import de.uni_hannover.sra.minimax_simulator.layout.constraint.AttributeType;
import de.uni_hannover.sra.minimax_simulator.layout.constraint.Constraint;
import de.uni_hannover.sra.minimax_simulator.layout.constraint.Layout;
import de.uni_hannover.sra.minimax_simulator.layout.constraint.RelativeMaxConstraint;
import de.uni_hannover.sra.minimax_simulator.layout.constraint.RelativeMinConstraint;

public class GroupLayout implements Layout
{
	private final static Set<AttributeType>	attributes	= EnumSet.of(AttributeType.LEFT,
															AttributeType.RIGHT,
															AttributeType.TOP,
															AttributeType.BOTTOM);

	private final Constraint				_top;
	private final Constraint				_bottom;
	private final Constraint				_left;
	private final Constraint				_right;

	public GroupLayout(Collection<String> groupMembers)
	{
		this(new HashSet<String>(groupMembers));
	}

	public GroupLayout(Set<String> groupMembers)
	{
		_top = new RelativeMinConstraint(groupMembers, AttributeType.TOP, 0);
		_bottom = new RelativeMaxConstraint(groupMembers, AttributeType.BOTTOM, 0);
		_left = new RelativeMinConstraint(groupMembers, AttributeType.LEFT, 0);
		_right = new RelativeMaxConstraint(groupMembers, AttributeType.RIGHT, 0);
	}

	@Override
	public Constraint getConstraint(AttributeType attribute)
	{
		switch (attribute)
		{
			case TOP:
				return _top;
			case BOTTOM:
				return _bottom;
			case LEFT:
				return _left;
			case RIGHT:
				return _right;

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