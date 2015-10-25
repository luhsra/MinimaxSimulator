package de.uni_hannover.sra.minimax_simulator.resources;

import java.text.MessageFormat;

/**
 * A {@code TextResource} is a text resource.
 * 
 * @author Martin L&uuml;ck
 */
public interface TextResource {

	/**
	 * Gets the text with the specified key.
	 * 
	 * @param key
	 *          the key of the resource
	 * @return
	 *          the text with the specified key
	 */
	public String get(String key);

	/**
	 * Gets the text with the specified key and replaces occurrences of {@code {x}} with
	 * the parameters.<br>
	 * <br>
	 * <b>Example:</b><br>
	 * The resource with the key {@code memtable.page} is {@code page {0} of {1}}. So calling
	 * {@code format("memtable.page", 1, 5)} leads to a return value of {@code page 1 of 5}.
	 * 
	 * @param key
	 *          the key of the resource
	 * @param params
	 *          the values to insert
	 * @return
	 *          the text with the specified key and inserted parameters
	 */
	public String format(String key, Object... params);

	/**
	 * Creates a {@link MessageFormat} of the resource with the specified key.
	 *
	 * @param key
	 *          the key of the resource
	 * @return
	 *          the {@code MessageFormat} of the resource
	 */
	public MessageFormat createFormat(String key);

	/**
	 * Sets the prefix for the keys that will be used for the instance of {@code TextResource}.<br>
	 * <br>
	 * <b>Example:</b><br>
	 * To shorten the keys for calling {@link #get(String)} you can specify the prefix every key has in common
	 * the the ofter keys used with the instance of {@code TextResource}. Using the {@code TextResource} for
	 * getting resources of the memory using {@code using("memory")} leads to shorten keys like {@code memory.import.title}
	 * to {@code import.title}.
	 *
	 * @param prefix
	 *          the prefix
	 * @return
	 *          the {@code TextResource} using the specified prefix
	 */
	public TextResource using(String prefix);
}