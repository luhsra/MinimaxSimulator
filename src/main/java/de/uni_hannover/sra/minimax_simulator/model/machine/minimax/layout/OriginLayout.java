package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.layout.constraint.AbsoluteConstraint;
import de.uni_hannover.sra.minimax_simulator.layout.constraint.AttributeType;
import de.uni_hannover.sra.minimax_simulator.layout.constraint.Constraint;
import de.uni_hannover.sra.minimax_simulator.layout.constraint.Layout;

import java.util.EnumSet;
import java.util.Set;

public class OriginLayout implements Layout
{
	private static final Set<AttributeType>	attributes	= EnumSet.of(AttributeType.HORIZONTAL_CENTER,
															AttributeType.VERTICAL_CENTER);

	private static final Constraint			zero		= new AbsoluteConstraint(0);

	@Override
	public Constraint getConstraint(AttributeType attribute)
	{
		return zero;
	}

	@Override
	public Set<AttributeType> getConstrainedAttributes()
	{
		return attributes;
	}
}