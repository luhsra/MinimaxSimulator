package de.uni_hannover.sra.minimax_simulator.model.user;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Under construction!</b><br>
 * The {@code ProjectConfiguration} holds information about the open tabs. This could be used to save user preferences.
 * 
 * @author Martin L&uuml;ck
 */
// TODO: finish
public class ProjectConfiguration {

	private List<String> _openTabKeys;
	private String _selectedTabKey;

	/**
	 * Constructs a new {@code ProjectConfiguration}.
	 */
	public ProjectConfiguration() {
		_openTabKeys = new ArrayList<String>();
		_selectedTabKey = "";
	}

	/**
	 * Gets the keys of the open tabs.
	 *
	 * @return
	 *          a list of the keys of the open tabs
	 */
	public List<String> getOpenTabKeys() {
		return _openTabKeys;
	}

	/**
	 * Gets the key of the selected tab.
	 *
	 * @return
	 *          the key of the selected tab
	 */
	public String getSelectedTabKey() {
		return _selectedTabKey;
	}

	/**
	 * Sets the key of the selected tab.
	 *
	 * @param key
	 *          the key of the selected tab
	 */
	public void setSelectedTabKey(String key) {
		_selectedTabKey = key;
	}
}