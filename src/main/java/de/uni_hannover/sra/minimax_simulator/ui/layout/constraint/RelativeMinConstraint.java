package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import java.util.Set;

public class RelativeMinConstraint extends RelativeMultiConstraint
{
	public RelativeMinConstraint(Set<Attribute> anchors)
	{
		super(anchors);
	}

	public RelativeMinConstraint(Set<String> anchors, AttributeType type, int offset)
	{
		super(anchors, type, offset);
	}

	public RelativeMinConstraint(Set<Attribute> anchors, int offset)
	{
		super(anchors, offset);
	}

	@Override
	public int getValue(AttributeSource attributes)
	{
		int minimum = Integer.MAX_VALUE;
		for (Attribute attr : _anchors)
		{
			int val = attributes.getValue(attr);
			if (val < minimum)
				minimum = val;
		}
		return minimum + _offset;
	}
}