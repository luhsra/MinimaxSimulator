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

    private List<String> openTabKeys;
    private String selectedTabKey;

    /**
     * Constructs a new {@code ProjectConfiguration}.
     */
    public ProjectConfiguration() {
        openTabKeys = new ArrayList<>();
        selectedTabKey = "";
    }

    /**
     * Gets the keys of the open tabs.
     *
     * @return
     *          a list of the keys of the open tabs
     */
    public List<String> getOpenTabKeys() {
        return openTabKeys;
    }

    /**
     * Gets the key of the selected tab.
     *
     * @return
     *          the key of the selected tab
     */
    public String getSelectedTabKey() {
        return selectedTabKey;
    }

    /**
     * Sets the key of the selected tab.
     *
     * @param key
     *          the key of the selected tab
     */
    public void setSelectedTabKey(String key) {
        selectedTabKey = key;
    }
}