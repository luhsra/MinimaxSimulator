package de.uni_hannover.sra.minimax_simulator.model.user;

import java.util.ArrayList;
import java.util.List;

/**
 * Unfinished class.
 *
 * 
 *
 */
public class ProjectConfiguration
{
	private List<String> _openTabKeys;
	private String _selectedTabKey;

	public ProjectConfiguration()
	{
		_openTabKeys = new ArrayList<String>();
		_selectedTabKey = "";
	}

	public List<String> getOpenTabKeys()
	{
		return _openTabKeys;
	}

	public String getSelectedTabKey()
	{
		return _selectedTabKey;
	}

	public void setSelectedTabKey(String key)
	{
		_selectedTabKey = key;
	}
}