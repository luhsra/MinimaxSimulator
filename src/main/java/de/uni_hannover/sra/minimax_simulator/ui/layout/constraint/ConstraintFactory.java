package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class ConstraintFactory
{
	public static enum AlignMode
	{
		HORIZONTALLY,
		VERTICALLY,
		HORIZONTALLY_VERTICALLY;
	}

	protected abstract ConstraintsManager getManager();

	private void setConstraint(String target, AttributeType targetAttr,
			Constraint constraint)
	{
		getManager().setConstraint(target, targetAttr, constraint);
	}

	private void clearConstraints(String target)
	{
		getManager().clearConstraints(target);
	}

	public void relative(String target, AttributeType targetAttr, String source,
			AttributeType sourceAttr, int offset)
	{
		setConstraint(target, targetAttr, new RelativeConstraint(source, sourceAttr,
			offset));
	}

	public void relative(String target, AttributeType targetAttr, String source,
			AttributeType sourceAttr)
	{
		setConstraint(target, targetAttr, new RelativeConstraint(source, sourceAttr));
	}

	public void relative(String target, AttributeType attr, String source, int offset)
	{
		setConstraint(target, attr, new RelativeConstraint(source, attr, offset));
	}

	public void relative(String target, AttributeType attr, String source)
	{
		setConstraint(target, attr, new RelativeConstraint(source, attr));
	}

	public void absolute(String target, AttributeType targetAttr, int offset)
	{
		setConstraint(target, targetAttr, new AbsoluteConstraint(offset));
	}

	public void below(String target, String source, int offset)
	{
		setConstraint(target, AttributeType.TOP, new RelativeConstraint(source,
			AttributeType.BOTTOM, offset));
	}

	public void below(String target, String source)
	{
		setConstraint(target, AttributeType.TOP, new RelativeConstraint(source,
			AttributeType.BOTTOM, 0));
	}

	public void above(String target, String source, int offset)
	{
		setConstraint(target, AttributeType.BOTTOM, new RelativeConstraint(source,
			AttributeType.TOP, -offset));
	}

	public void above(String target, String source)
	{
		setConstraint(target, AttributeType.BOTTOM, new RelativeConstraint(source,
			AttributeType.TOP, 0));
	}

	public void left(String target, String source, int offset)
	{
		setConstraint(target, AttributeType.RIGHT, new RelativeConstraint(source,
			AttributeType.LEFT, -offset));
	}

	public void leftTo(String target, String source)
	{
		setConstraint(target, AttributeType.RIGHT, new RelativeConstraint(source,
			AttributeType.LEFT, 0));
	}

	public void rightTo(String target, String source, int offset)
	{
		setConstraint(target, AttributeType.LEFT, new RelativeConstraint(source,
			AttributeType.RIGHT, offset));
	}

	public void right(String target, String source)
	{
		setConstraint(target, AttributeType.LEFT, new RelativeConstraint(source,
			AttributeType.RIGHT, 0));
	}

	public void alignHorizontally(String target, String source)
	{
		setConstraint(target, AttributeType.HORIZONTAL_CENTER, new RelativeConstraint(
			source, AttributeType.HORIZONTAL_CENTER));
	}

	public void alignVertically(String target, String source)
	{
		setConstraint(target, AttributeType.VERTICAL_CENTER, new RelativeConstraint(
			source, AttributeType.VERTICAL_CENTER));
	}

	public void align(String target, String source)
	{
		alignHorizontally(target, source);
		alignVertically(target, source);
	}

	public void align(String target, String source, AlignMode mode)
	{
		switch (mode)
		{
			case HORIZONTALLY:
				alignHorizontally(target, source);
				break;
			case VERTICALLY:
				alignVertically(target, source);
				break;
			case HORIZONTALLY_VERTICALLY:
				align(target, source);
				break;
		}
	}

	public void group(String target, String... anchors)
	{
		group(target, new HashSet<String>(Arrays.asList(anchors)));
	}

	public void group(String target, Set<String> anchors)
	{
		// Attributes to minimize
		setConstraint(target, AttributeType.LEFT, new RelativeMinConstraint(anchors,
			AttributeType.LEFT, 0));
		setConstraint(target, AttributeType.TOP, new RelativeMinConstraint(anchors,
			AttributeType.TOP, 0));

		// Attrbutes to maximize
		setConstraint(target, AttributeType.RIGHT, new RelativeMaxConstraint(anchors,
			AttributeType.RIGHT, 0));
		setConstraint(target, AttributeType.BOTTOM, new RelativeMaxConstraint(anchors,
			AttributeType.BOTTOM, 0));
	}

	public void clear(String target)
	{
		clearConstraints(target);
	}
}