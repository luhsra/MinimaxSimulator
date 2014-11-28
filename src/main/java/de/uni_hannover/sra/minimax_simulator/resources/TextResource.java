package de.uni_hannover.sra.minimax_simulator.resources;

import java.text.MessageFormat;

/**
 * TODO
 * 
 * @author Martin
 */
public interface TextResource
{
	/**
	 * TODO
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key);

	/**
	 * TODO
	 * 
	 * @param key
	 * @param params
	 * @return
	 */
	public String format(String key, Object... params);

	public MessageFormat createFormat(String key);

	public TextResource using(String prefix);
}