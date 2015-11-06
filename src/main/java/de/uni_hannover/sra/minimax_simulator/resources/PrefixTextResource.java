package de.uni_hannover.sra.minimax_simulator.resources;

import java.text.MessageFormat;

/**
 * A {@code PrefixTextResource} is a {@link DefaultTextResource} with a key prefix.
 *
 * @author Martin L&uuml;ck
 */
class PrefixTextResource implements TextResource {

	private final TextResource res;
	private final String prefix;

	/**
	 * Constructs a new {@code PrefixTextResource} with the specified {@link TextResource} and key prefix.
	 *
	 * @param res
	 *          the {@code TextResource} containing the resources
	 * @param prefix
	 *          the key prefix
	 */
	PrefixTextResource(TextResource res, String prefix) {
		this.res = res;
		this.prefix = prefix + ".";
	}

	@Override
	public String get(String key) {
		return res.get(prefix + key);
	}

	@Override
	public String format(String key, Object... params) {
		return res.format(prefix + key, params);
	}

	@Override
	public MessageFormat createFormat(String key) {
		return res.createFormat(prefix + key);
	}

	@Override
	public TextResource using(String prefix) {
		return new PrefixTextResource(this, prefix);
	}
}