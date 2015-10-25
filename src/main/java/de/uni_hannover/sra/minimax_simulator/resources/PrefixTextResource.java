package de.uni_hannover.sra.minimax_simulator.resources;

import java.text.MessageFormat;

/**
 * A {@code PrefixTextResource} is a {@link DefaultTextResource} with a key prefix.
 *
 * @author Martin L&uuml;ck
 */
class PrefixTextResource implements TextResource {

	private final TextResource _res;
	private final String _prefix;

	/**
	 * Constructs a new {@code PrefixTextResource} with the specified {@link TextResource} and key prefix.
	 *
	 * @param res
	 *          the {@code TextResource} containing the resources
	 * @param prefix
	 *          the key prefix
	 */
	PrefixTextResource(TextResource res, String prefix) {
		_res = res;
		_prefix = prefix + ".";
	}

	@Override
	public String get(String key) {
		return _res.get(_prefix + key);
	}

	@Override
	public String format(String key, Object... params) {
		return _res.format(_prefix + key, params);
	}

	@Override
	public MessageFormat createFormat(String key) {
		return _res.createFormat(_prefix + key);
	}

	@Override
	public TextResource using(String prefix) {
		return new PrefixTextResource(this, prefix);
	}
}