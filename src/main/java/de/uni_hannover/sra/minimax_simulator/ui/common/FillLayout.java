package de.uni_hannover.sra.minimax_simulator.ui.common;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * The FillLayout always should have no children, or exactly one.<br>
 * That is aligned in a way so it fills the parent element completely. (minus the {@link Insets}).<br>
 * FillLayout cannot be instanciated, there exists the shared instance {@link FillLayout.INSTANCE} instead.
 * 
 * @author Martin
 */
@Deprecated
public final class FillLayout implements LayoutManager
{
	/**
	 * The public shared instance of this class, since it contains only methods, no fields.
	 */
	public static final FillLayout INSTANCE = new FillLayout();

	private static final Dimension EMPTY = new Dimension(0, 0);

	private FillLayout()
	{
	}

	@Override
	public void addLayoutComponent(String name, Component comp)
	{
	}

	@Override
	public void removeLayoutComponent(Component comp)
	{
	}

	@Override
	public Dimension preferredLayoutSize(Container parent)
	{
		synchronized (parent.getTreeLock())
		{
			if (parent.getComponentCount() == 0)
				return EMPTY;
			Insets insets = parent.getInsets();
			Component comp = parent.getComponent(0);

			Dimension d = comp.getPreferredSize();
			int w = d.width;
			int h = d.height;

			return new Dimension(insets.left + insets.right + w, insets.top + insets.bottom + h);
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container parent)
	{
		if (parent.getComponentCount() == 0)
			return EMPTY;
		Insets insets = parent.getInsets();
		Component comp = parent.getComponent(0);

		Dimension d = comp.getMinimumSize();
		int w = d.width;
		int h = d.height;

		return new Dimension(insets.left + insets.right + w, insets.top + insets.bottom + h);
	}

	@Override
	public void layoutContainer(Container parent)
	{
		synchronized (parent.getTreeLock())
		{
			if (parent.getComponentCount() == 0)
				return;

			Insets insets = parent.getInsets();
			Component comp = parent.getComponent(0);

			int w = parent.getWidth() - (insets.left + insets.right);
			int h = parent.getHeight() - (insets.top + insets.bottom);

			comp.setBounds(insets.left, insets.top, w, h);
		}
	}
}
