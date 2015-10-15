package de.uni_hannover.sra.minimax_simulator.config;

/**
 * A {@code ConfigurationLoader} can be used to configure a class.<br>
 * The class is assumed to be a <i>configuration class</i>, that is a class
 * with some public, static, non-final fields annotated with a {@link Configuration} annotation.
 * <br><br>
 * The configuration loader is supposed to overwrite the configurable fields with data from
 * some external data source, automatically respecting the fields' types.
 * 
 * @author Martin L&uuml;ck
 */
public interface ConfigurationLoader {

	/**
	 * Configures all fields annotated with a {@code Configuration} annotation of the given classes.
	 *
	 * @param classes
	 *          the classes to configure
	 * @throws ConfigurationException
	 *          thrown if there was an error during configuration
	 */
	public void configure(Class<?>... classes) throws ConfigurationException;

	/**
	 * A {@code ConfigurationException} indicates an error during configuring a class.
	 *
	 * @author Martin L&uuml;ck
	 */
	public static class ConfigurationException extends Exception {

		/**
		 * Constructs a new {@code ConfigurationException} with the specified detail message and cause.
		 *
		 * @param str
		 *          the detail message
		 * @param cause
		 *          the cause of the exception
		 */
		public ConfigurationException(String str, Throwable cause)
		{
			super(str, cause);
		}

		/**
		 * Constructs a new {@code ConfigurationException} with the specified detail message.
		 * The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause}.
		 *
		 * @param str
		 *          the detail message
		 */
		public ConfigurationException(String str)
		{
			super(str);
		}
	}
}
