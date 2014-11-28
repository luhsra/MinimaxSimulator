package de.uni_hannover.sra.minimax_simulator.ui.common.components;

import java.awt.Dimension;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4618607 
public class JWideComboBox extends JComboBox
{
	public JWideComboBox()
	{
	}

	public JWideComboBox(ComboBoxModel aModel)
	{
		super(aModel);
	}

	private boolean	layingOut	= false;

	@Override
	public void doLayout()
	{
		try
		{
			layingOut = true;
			super.doLayout();
		}
		finally
		{
			layingOut = false;
		}
	}

	@Override
	public Dimension getSize()
	{
		Dimension dim = super.getSize();
		if (!layingOut)
			dim.width = Math.max(dim.width, getPreferredSize().width);
		return dim;
	}
}