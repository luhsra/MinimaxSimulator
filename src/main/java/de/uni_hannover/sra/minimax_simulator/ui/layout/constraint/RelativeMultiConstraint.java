package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import java.util.HashSet;
import java.util.Set;

public abstract class RelativeMultiConstraint implements Constraint
{
	protected final int _offset;

	protected final Set<Attribute> _anchors;

	public RelativeMultiConstraint(Set<Attribute> anchors)
	{
		this(anchors, 0);
	}

	public RelativeMultiConstraint(Set<String> anchors, AttributeType type, int offset)
	{
		this(toAttributeSet(anchors, type), offset);
	}

	public RelativeMultiConstraint(Set<Attribute> anchors, int offset)
	{
		if (anchors.isEmpty())
			throw new IllegalArgumentException("Empty anchor set is invalid for group");

		_offset = offset;
		_anchors = new HashSet<Attribute>(anchors);
	}

	@Override
	public String toString()
	{
		return "rel(" + _anchors + " + " + _offset + ")";
	}

	@Override
	public Set<Attribute> getDependencies()
	{
		return _anchors;
	}

	private static Set<Attribute> toAttributeSet(Set<String> anchors, AttributeType type)
	{
		Set<Attribute> attrs = new HashSet<Attribute>(anchors.size());
		for (String anchor : anchors)
			attrs.add(new Attribute(anchor, type));
		return attrs;
	}

}