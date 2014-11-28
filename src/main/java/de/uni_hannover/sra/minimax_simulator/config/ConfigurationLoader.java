package de.uni_hannover.sra.minimax_simulator.config;

/**
 * An {@link ConfigurationLoader} can be used to configure a class.<br>
 * The class is assumed to be a <i>configuration class</i>, that is a class
 * with some public, static, non-final fields annotated with a {@link Configuration} annotation.
 * <br><br>
 * The configuration loader is supposed to overwrite the configurable fields with data from
 * some external data source, automatically respecting the fields' types.
 * 
 * @author Martin
 * 
 */
public interface ConfigurationLoader
{
	public void configure(Class<?>... classes) throws ConfigurationException;

	public static class ConfigurationException extends Exception
	{
		public ConfigurationException(String str, Throwable cause)
		{
			super(str, cause);
		}

		public ConfigurationException(String str)
		{
			super(str);
		}
	}
}
