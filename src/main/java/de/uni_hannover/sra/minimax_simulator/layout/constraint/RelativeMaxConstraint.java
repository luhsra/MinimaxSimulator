package de.uni_hannover.sra.minimax_simulator.layout.constraint;

import java.util.Set;

public class RelativeMaxConstraint extends RelativeMultiConstraint
{
	public RelativeMaxConstraint(Set<Attribute> anchors)
	{
		super(anchors);
	}

	public RelativeMaxConstraint(Set<String> anchors, AttributeType type, int offset)
	{
		super(anchors, type, offset);
	}

	public RelativeMaxConstraint(Set<Attribute> anchors, int offset)
	{
		super(anchors, offset);
	}

	@Override
	public int getValue(AttributeSource attributes)
	{
		int maximum = Integer.MIN_VALUE;
		for (Attribute attr : _anchors)
		{
			int val = attributes.getValue(attr);
			if (val > maximum)
				maximum = val;
		}
		return maximum + _offset;
	}
}