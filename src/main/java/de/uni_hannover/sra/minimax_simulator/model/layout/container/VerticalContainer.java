package de.uni_hannover.sra.minimax_simulator.model.layout.container;

import de.uni_hannover.sra.minimax_simulator.layout.Bounds;
import de.uni_hannover.sra.minimax_simulator.layout.Component;
import de.uni_hannover.sra.minimax_simulator.layout.Dimension;
import de.uni_hannover.sra.minimax_simulator.layout.Insets;

public class VerticalContainer extends AbstractListContainer
{
	@Override
	protected void updateMySize()
	{
		int width = 0;
		int height = 0;

		// calculate size
		for (Component child : _children)
		{
			Insets insets = child.getInsets();
			Dimension dim = child.getDimension();

			int itsWidth = dim.w + insets.l + insets.r;
			int itsHeight = dim.h + insets.b + insets.t;

			if (itsWidth > width)
				width = itsWidth;
			height += itsHeight;
		}

		setDimension(new Dimension(width, height));
	}

	@Override
	protected void layoutChildren()
	{
		int x = getBounds().x;
		int y = getBounds().y;
		int w = getBounds().w;

		for (Component child : _children)
		{
			Dimension dim = child.getDimension();
			Insets ins = child.getInsets();

			int space = (w - dim.w) / 2;
			y += ins.t;
			child.setBounds(new Bounds(x + space, y, dim.w, dim.h));
			y += dim.h + ins.b;
		}
	}

	@Override
	public void addComponent(Component component, Object constraint)
	{
		if (constraint instanceof Integer)
		{
			_children.add(((Integer) constraint).intValue(), component);	
		}
		else
		{
			_children.add(component);
		}
	}
}