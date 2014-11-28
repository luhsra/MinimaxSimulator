package de.uni_hannover.sra.minimax_simulator.ui.actions;

import java.awt.event.ActionEvent;

/**
 * Helper class to be used when an button/menu should be enabled exactly
 * when there is an open project.<br><br>
 * Instances of this class do have an empty <code>actionPerformed()</code> method.
 * 
 * @author Martin
 *
 */
public class ViewDummy extends ProjectAction
{
	public final static ViewDummy INSTANCE = new ViewDummy();

	@Override
	public void actionPerformed(ActionEvent e)
	{
	}
}