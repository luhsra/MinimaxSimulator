package de.uni_hannover.sra.minimax_simulator.resources;

import java.text.MessageFormat;

class PrefixTextResource implements TextResource
{
	private final TextResource _res;
	private final String _prefix;

	PrefixTextResource(TextResource res, String prefix)
	{
		_res = res;
		_prefix = prefix + ".";
	}

	@Override
	public String get(String key)
	{
		return _res.get(_prefix + key);
	}

	@Override
	public String format(String key, Object... params)
	{
		return _res.format(_prefix + key, params);
	}

	@Override
	public MessageFormat createFormat(String key)
	{
		return _res.createFormat(_prefix + key);
	}

	@Override
	public TextResource using(String prefix)
	{
		return new PrefixTextResource(this, prefix);
	}
}