package de.uni_hannover.sra.minimax_simulator.resources;

import static com.google.common.base.Preconditions.*;

import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;

import de.uni_hannover.sra.minimax_simulator.util.IteratorEnumeration;

public class MapResourceBundle extends ResourceBundle
{
	private final Map<String, Object> _map;

	public MapResourceBundle(Map<String, Object> map)
	{
		_map = checkNotNull(map);
	}

	@Override
	protected Object handleGetObject(String key)
	{
		return _map.get(key);
	}

	@Override
	public Enumeration<String> getKeys()
	{
		return new IteratorEnumeration<String>(_map.keySet());
	}
}