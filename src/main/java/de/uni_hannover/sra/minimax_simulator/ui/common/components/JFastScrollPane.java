package de.uni_hannover.sra.minimax_simulator.ui.common.components;

import java.awt.Component;

import javax.swing.JScrollPane;

/**
 * The JFastScrollPane behaves like the original JScrollPane, except
 * that it has a faster scrolling speed.
 * 
 * @author Martin
 */
@Deprecated
public final class JFastScrollPane extends JScrollPane
{
	public JFastScrollPane(Component view)
	{
		super(view);
		getHorizontalScrollBar().setUnitIncrement(10); // Standard: 1
		getVerticalScrollBar().setUnitIncrement(10); // Standard: 1
	}

	public JFastScrollPane(Component view, int factor)
	{
		super(view);
		getHorizontalScrollBar().setUnitIncrement(factor); // Standard: 1
		getVerticalScrollBar().setUnitIncrement(factor); // Standard: 1
	}
}