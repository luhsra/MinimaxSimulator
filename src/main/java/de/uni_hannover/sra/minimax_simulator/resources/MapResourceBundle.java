package de.uni_hannover.sra.minimax_simulator.resources;

import de.uni_hannover.sra.minimax_simulator.util.IteratorEnumeration;

import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A {@link ResourceBundle} using a map to hold the resources.
 *
 * @author Martin L&uuml;ck
 */
public class MapResourceBundle extends ResourceBundle {

	private final Map<String, Object> map;

	/**
	 * Constructs a new {@code MapResourceBundle} with the specified map of resources.
	 *
	 * @param map
	 *          a map of resources
	 */
	public MapResourceBundle(Map<String, Object> map) {
		this.map = checkNotNull(map);
	}

	@Override
	protected Object handleGetObject(String key) {
		return map.get(key);
	}

	@Override
	public Enumeration<String> getKeys() {
		return new IteratorEnumeration<String>(map.keySet());
	}
}