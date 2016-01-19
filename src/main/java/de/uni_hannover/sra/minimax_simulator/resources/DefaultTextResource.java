package de.uni_hannover.sra.minimax_simulator.resources;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Default implementation of {@link TextResource}.
 *
 * @author Martin L&uuml;ck
 */
class DefaultTextResource implements TextResource {

    private final ResourceBundle bundle;

    /**
     * Constructs a new {@code DefaultTextResource} using the specified {@link ResourceBundle}.
     *
     * @param bundle
     *          the bundle containing the resources
     */
    DefaultTextResource(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public String get(String key) {
        return bundle.getString(key);
    }

    @Override
    public String format(String key, Object... params) {
        MessageFormat fm = new MessageFormat(bundle.getString(key), bundle.getLocale());
        return fm.format(params);
    }

    @Override
    public MessageFormat createFormat(String key) {
        return new MessageFormat(bundle.getString(key), bundle.getLocale());
    }

    @Override
    public TextResource using(final String prefix) {
        return new PrefixTextResource(this, prefix);
    }
}