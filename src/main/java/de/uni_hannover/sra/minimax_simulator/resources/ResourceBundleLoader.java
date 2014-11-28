package de.uni_hannover.sra.minimax_simulator.resources;

import java.util.ResourceBundle;

public interface ResourceBundleLoader
{
	public ResourceBundle getBundle(String bundleName);

	public TextResource getTextResource(String bundleName);
}