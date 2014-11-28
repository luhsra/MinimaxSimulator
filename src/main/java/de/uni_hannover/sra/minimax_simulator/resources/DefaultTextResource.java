package de.uni_hannover.sra.minimax_simulator.resources;

import java.text.MessageFormat;
import java.util.ResourceBundle;

class DefaultTextResource implements TextResource
{
	private final ResourceBundle _bundle;

	DefaultTextResource(ResourceBundle bundle)
	{
		_bundle = bundle;
	}

	@Override
	public String get(String key)
	{
		return _bundle.getString(key);
	}

	@Override
	public String format(String key, Object... params)
	{
		MessageFormat fm = new MessageFormat(_bundle.getString(key),
			_bundle.getLocale());
		return fm.format(params);
	}

	@Override
	public MessageFormat createFormat(String key)
	{
		return new MessageFormat(_bundle.getString(key), _bundle.getLocale());
	}

	@Override
	public TextResource using(final String prefix)
	{
		return new PrefixTextResource(this, prefix);
	}
}