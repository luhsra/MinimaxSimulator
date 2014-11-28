package de.uni_hannover.sra.minimax_simulator.ui.common.components.tabbed;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

public class JCloseableTabPanel extends JDraggableTabPanel
{
	private final ArrayList<Tab> _tabs = new ArrayList<Tab>();

	public void addTab(Tab theTab, boolean selectTheTab)
	{
		_tabs.add(theTab);

		addTab(theTab.getTitle(), theTab.getComponent());

		int index = getTabCount() - 1;
		if (theTab instanceof CloseableTab)
		{
			setTabComponentAt(index, new TabButtonComponent(this, theTab));
		}

		if (selectTheTab)
			setSelectedIndex(index);
	}

	public boolean isTabOpen(Tab tab)
	{
		return _tabs.contains(tab);
	}

	public void selectTab(Tab tab)
	{
		int index = _tabs.indexOf(tab);
		if (index != -1)
			setSelectedIndex(index);
	}

	protected List<Tab> getTabs()
	{
		return _tabs;
	}

	protected void closeTab(int i)
	{
		Tab theTab = _tabs.get(i);
		removeTabAt(i);
		theTab.onClose();
	}

	@Override
	public void removeTabAt(int index)
	{
		_tabs.remove(index);
		super.removeTabAt(index);
	}

	@Override
	protected void moveTab(int fromTabNumber, int toTabNumber)
	{
		// Since we override removeTabAt, the Tab object will be removed from the list
		// when it is moved.
		Tab theTab = _tabs.get(fromTabNumber);
		
		super.moveTab(fromTabNumber, toTabNumber);

		// re-add it
		_tabs.add(toTabNumber, theTab);
	}

	@Override
	protected void onDragStop(int fromTabNumber, int toTabNumber)
	{
		// activate close button in all tabs
		int tc = getTabCount();
		for (int i = 0; i < tc; i++)
		{
			Component tabComp = getTabComponentAt(i);
			if (tabComp instanceof TabButtonComponent)
				((TabButtonComponent) tabComp).setClosingEnabled(true);
		}

		super.onDragStop(fromTabNumber, toTabNumber);
	}

	@Override
	protected void onDragStart(int fromTabNumber)
	{
		// deactivate close button in all tabs
		int tc = getTabCount();
		for (int i = 0; i < tc; i++)
		{
			Component tabComp = getTabComponentAt(i);
			if (tabComp instanceof TabButtonComponent)
				((TabButtonComponent) tabComp).setClosingEnabled(false);
		}

		super.onDragStart(fromTabNumber);
	}
}
