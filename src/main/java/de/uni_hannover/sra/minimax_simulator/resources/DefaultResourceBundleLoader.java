package de.uni_hannover.sra.minimax_simulator.resources;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

import static com.google.common.base.Preconditions.checkNotNull;

public class DefaultResourceBundleLoader implements ResourceBundleLoader
{
	private final Control					_defaultControl;
	private final Locale					_defaultLocale;

	private final Map<String, TextResource>	_resourceCache;

	public DefaultResourceBundleLoader(Control defaultControl, Locale defaultLocale)
	{
		_defaultControl = checkNotNull(defaultControl,
				"Invalid null argument: defaultControl");
			_defaultLocale = checkNotNull(defaultLocale,
				"Invalid null argument: defaultLocale");

		_resourceCache = new HashMap<String, TextResource>();
	}

	public Control getDefaultControl()
	{
		return _defaultControl;
	}

	public Locale getDefaultLocale()
	{
		return _defaultLocale;
	}

	/* (non-Javadoc)
	 * 
	 * @see de.luh.sra.mmsim.resources.ResourceBundleLoader#getBundle(java.lang.String) */
	@Override
	public ResourceBundle getBundle(String bundleName)
	{
		ResourceBundle res = ResourceBundle.getBundle(bundleName, _defaultLocale,
			_defaultControl);
		return res;
	}

	/* (non-Javadoc)
	 * 
	 * @see de.luh.sra.mmsim.resources.ResourceBundleLoader#getTextResource(java.lang.String) */
	@Override
	public TextResource getTextResource(String bundleName)
	{
		TextResource res = _resourceCache.get(bundleName);
		if (res != null)
			return res;

		final ResourceBundle bundle = getBundle(bundleName);
		res = new DefaultTextResource(bundle);

		_resourceCache.put(bundleName, res);
		return res;
	}
}