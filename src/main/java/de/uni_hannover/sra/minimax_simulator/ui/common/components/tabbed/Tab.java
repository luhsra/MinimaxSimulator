package de.uni_hannover.sra.minimax_simulator.ui.common.components.tabbed;

import javax.swing.Icon;
import javax.swing.JComponent;

@Deprecated
public interface Tab
{
	/*
	public void addTabListener(TabListener listener);

	public void removeTabListener(TabListener listener);
	*/

	public String getTitle();

	public Icon getIcon();

	public JComponent getComponent();

	public void onClose();
}