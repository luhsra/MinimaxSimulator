package de.uni_hannover.sra.minimax_simulator.resources;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Default implementation of the {@link ResourceBundleLoader}.
 *
 * @author Martin L&uuml;ck
 */
public class DefaultResourceBundleLoader implements ResourceBundleLoader {

	private final Control					_defaultControl;
	private final Locale					_defaultLocale;

	private final Map<String, TextResource>	_resourceCache;

	/**
	 * Constructs a new {@code DefaultResourceBundleLoader} with the specified {@link Control} and {@link Locale}.
	 *
	 * @param defaultControl
	 *          the {@code Control}
	 * @param defaultLocale
	 *          the {@code Locale}
	 */
	public DefaultResourceBundleLoader(Control defaultControl, Locale defaultLocale) {
		_defaultControl = checkNotNull(defaultControl, "Invalid null argument: defaultControl");
			_defaultLocale = checkNotNull(defaultLocale, "Invalid null argument: defaultLocale");

		_resourceCache = new HashMap<String, TextResource>();
	}

	/**
	 * Gets the {@link Control} of the {@code DefaultResourceBundleLoader}.
	 *
	 * @return
	 *          the {@code Control}
	 */
	public Control getDefaultControl() {
		return _defaultControl;
	}

	/**
	 * Gets the {@link Locale} of the {@code DefaultResourceBundleLoader}.
	 *
	 * @return
	 *          the {@code Locale}
	 */
	public Locale getDefaultLocale() {
		return _defaultLocale;
	}

	@Override
	public ResourceBundle getBundle(String bundleName) {
		ResourceBundle res = ResourceBundle.getBundle(bundleName, _defaultLocale, _defaultControl);
		return res;
	}

	@Override
	public TextResource getTextResource(String bundleName) {
		TextResource res = _resourceCache.get(bundleName);
		if (res != null) {
			return res;
		}

		final ResourceBundle bundle = getBundle(bundleName);
		res = new DefaultTextResource(bundle);

		_resourceCache.put(bundleName, res);
		return res;
	}
}