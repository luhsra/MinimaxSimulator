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

    private final Control defaultControl;
    private final Locale defaultLocale;

    private final Map<String, TextResource> resourceCache;

    /**
     * Constructs a new {@code DefaultResourceBundleLoader} with the specified {@link Control} and {@link Locale}.
     *
     * @param defaultControl
     *          the {@code Control}
     * @param defaultLocale
     *          the {@code Locale}
     */
    public DefaultResourceBundleLoader(Control defaultControl, Locale defaultLocale) {
        this.defaultControl = checkNotNull(defaultControl, "Invalid null argument: defaultControl");
        this.defaultLocale = checkNotNull(defaultLocale, "Invalid null argument: defaultLocale");

        resourceCache = new HashMap<>();
    }

    /**
     * Gets the {@link Control} of the {@code DefaultResourceBundleLoader}.
     *
     * @return
     *          the {@code Control}
     */
    public Control getDefaultControl() {
        return defaultControl;
    }

    /**
     * Gets the {@link Locale} of the {@code DefaultResourceBundleLoader}.
     *
     * @return
     *          the {@code Locale}
     */
    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    @Override
    public ResourceBundle getBundle(String bundleName) {
        return ResourceBundle.getBundle(bundleName, defaultLocale, defaultControl);
    }

    @Override
    public TextResource getTextResource(String bundleName) {
        TextResource res = resourceCache.get(bundleName);
        if (res != null) {
            return res;
        }

        final ResourceBundle bundle = getBundle(bundleName);
        res = new DefaultTextResource(bundle);

        resourceCache.put(bundleName, res);
        return res;
    }
}