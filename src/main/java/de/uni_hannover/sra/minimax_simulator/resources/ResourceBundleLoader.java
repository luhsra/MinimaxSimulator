package de.uni_hannover.sra.minimax_simulator.resources;

import java.util.ResourceBundle;

/**
 * A {@code ResourceBundleLoader} loads and manages resources.
 *
 * @author Martin L&uuml;ck
 */
public interface ResourceBundleLoader {

	/**
	 * Gets the {@link ResourceBundle} with the specified name.
	 *
	 * @param bundleName
	 *          the name of the bundle
	 * @return
	 *          the {@code ResourceBundle} with the specified name
	 */
	public ResourceBundle getBundle(String bundleName);

	/**
	 * Gets the {@link TextResource} with the specified name.
	 *
	 * @param bundleName
	 *          the name of the resource
	 * @return
	 *          the {@code TextResource} with the specified name
	 */
	public TextResource getTextResource(String bundleName);
}