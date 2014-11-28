package de.uni_hannover.sra.minimax_simulator.ui.common.components.tabbed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JPersistentTabPanel extends JCloseableTabPanel
{
	private final Map<String, TabFactory> _tabFactories = new HashMap<String, TabFactory>();

	private static class TabEntry
	{
		public String key;
		public Tab tab;

		public TabEntry(String key, Tab tab)
		{
			this.key = key;
			this.tab = tab;
		}
	}

	// preserve insertion order
	//private final LinkedHashMap<String, Tab> _openTabs = new LinkedHashMap<String, Tab>();
	private final ArrayList<TabEntry> _openTabs = new ArrayList<TabEntry>();
	
	public void registerTabFactory(String key, TabFactory factory)
	{
		_tabFactories.put(key, factory);
	}

	public void unregisterTabFactory(String factoryKey)
	{
		_tabFactories.remove(factoryKey);
	}

	public void addTab(String factoryKey, boolean selectTheTab)
	{
		TabFactory factory = _tabFactories.get(factoryKey);
		if (factory == null)
			throw new IllegalArgumentException("Unknown tab factory: " + factoryKey);

		if (isTabOpen(factoryKey))
			throw new IllegalArgumentException("Tab " + factoryKey + " is already open");

		Tab theTab = factory.buildTab();
		addTab(theTab, selectTheTab);
		_openTabs.add(new TabEntry(factoryKey, theTab));
	}

	public boolean isTabOpen(String factoryKey)
	{
		for (TabEntry te : _openTabs)
			if (te.key.equals(factoryKey))
				return true;
		return false;
	}

	public void selectTab(String factoryKey, boolean openIfClosed)
	{
		if (!isTabOpen(factoryKey))
		{
			if (openIfClosed)
				addTab(factoryKey, true);
		}
		else
		{
			for (TabEntry te : _openTabs)
			{
				if (te.key.equals(factoryKey))
				{
					selectTab(te.tab);
					return;
				}
			}
		}
	}

	public Tab getTab(String key)
	{
		for (TabEntry te : _openTabs)
			if (te.key.equals(key))
				return te.tab;
		throw new IllegalArgumentException("Tab " + key + " is not open");
	}

	@Override
	public void removeTabAt(int index)
	{
		super.removeTabAt(index);
		_openTabs.remove(index);
	}

	@Override
	protected void moveTab(int fromTabNumber, int toTabNumber)
	{
		// Since we override removeTabAt, the Tab object will be removed from the map
		// when it is moved.
		TabEntry te = _openTabs.get(fromTabNumber);
		
		super.moveTab(fromTabNumber, toTabNumber);

		// re-add it
		_openTabs.add(toTabNumber, te);
		
	}

	public List<String> getOpenTabList()
	{
		ArrayList<String> tabKeys = new ArrayList<String>(_openTabs.size());
		for (TabEntry te : _openTabs)
			tabKeys.add(te.key);
		return tabKeys;
	}
}
