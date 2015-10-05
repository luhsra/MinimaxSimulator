package de.uni_hannover.sra.minimax_simulator.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

import de.uni_hannover.sra.minimax_simulator.io.IOUtils;

/**
 * A basic implementation of {@link ConfigurationLoader} using <i>.properties</i> files.
 * 
 * @author Martin
 * 
 */
public class PropertiesFileConfigLoader implements ConfigurationLoader
{
	public static enum MissingConfigStrategy
	{
		USE_DEFAULT,
		THROW;
	}

	private final MissingConfigStrategy	_missingConfStrategy;
	private final File					_propertiesFile;

	public PropertiesFileConfigLoader()
	{
		this(null, MissingConfigStrategy.USE_DEFAULT);
	}

	public PropertiesFileConfigLoader(File file)
	{
		this(file, MissingConfigStrategy.USE_DEFAULT);
	}

	public PropertiesFileConfigLoader(MissingConfigStrategy missingConfigStrategy)
	{
		this(null, missingConfigStrategy);
	}

	public PropertiesFileConfigLoader(File file,
			MissingConfigStrategy missingConfigStrategy)
	{
		_propertiesFile = file;
		_missingConfStrategy = missingConfigStrategy;
	}

	@Override
	public void configure(Class<?>... classes) throws ConfigurationException
	{
		for (Class<?> clazz : classes)
		{
			File file;

			ConfigurationFile fileAnnotation = clazz
					.getAnnotation(ConfigurationFile.class);
			if (fileAnnotation == null)
			{
				if (_propertiesFile == null)
				{
					if (_missingConfStrategy == MissingConfigStrategy.THROW)
						throw new ConfigurationException(
							"Missing ConfigurationFile annotation for: "
								+ clazz.getName());
					file = null;
				}
				else
				{
					file = _propertiesFile;
				}
			}
			else
			{
				file = new File(fileAnnotation.file());
			}

			Properties properties = new Properties();

			if (file != null)
				loadPropertiesFrom(file, properties);

			configureStaticFields(clazz, properties);
		}
	}

	private void loadPropertiesFrom(File configFile, Properties properties)
			throws ConfigurationException
	{
		Reader r = null;
		try
		{
			r = IOUtils.toBufferedReader(new FileReader(configFile));
			properties.load(r);
		}
		catch (FileNotFoundException fnfe)
		{
			if (_missingConfStrategy == MissingConfigStrategy.THROW)
				throw new ConfigurationException("Configuration file not found: "
					+ configFile.getPath());
			// else: do not modify the Properties instance
		}
		catch (IOException e)
		{
			if (_missingConfStrategy == MissingConfigStrategy.THROW)
				throw new ConfigurationException("Cannot read configuration file: "
					+ configFile.getPath());
			// else: do not modify the Properties instance
		}
		finally
		{
			IOUtils.closeQuietly(r);
		}
	}

	private static void configureStaticFields(Class<?> clazz, Properties properties)
			throws ConfigurationException
	{
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields)
		{
			Configuration conf = f.getAnnotation(Configuration.class);
			if (conf == null)
			{
				// Not a configurable property
				continue;
			}

			int mod = f.getModifiers();
			if (Modifier.isFinal(mod) || !Modifier.isStatic(mod)
				|| !Modifier.isPublic(mod))
			{
				throw new ConfigurationException("Configurable field of class "
					+ clazz.getName() + " must be public, static and non-final: "
					+ f.getName());
			}

			String key = conf.key();
			FieldType fieldType = conf.type();
			Class<?> fieldClass = f.getType();

			String textValue = properties.getProperty(key, conf.defaultValue());
			Object value;

			switch (fieldType)
			{
				case BYTESIZE:
					value = Parser.toFileSize(textValue);
					break;
				case AUTO:
					if (fieldClass.isArray() && fieldClass.getComponentType() == String.class)
					{
						value = Parser.toStrings(textValue, conf.separator());
					}
					else if (fieldClass.isEnum())
					{
						value = Parser.toEnum(textValue, (Class<? extends Enum>) fieldClass);
					}
					else
					{
						value = Parser.auto(textValue, fieldClass);
					}
					break;

				default:
					throw new IllegalArgumentException("Unsupported field type: " + fieldType);
					
			}

			try
			{
				f.set(null, value);
			}
			catch (Exception e)
			{
				// Should never happen since the field is writable
				throw new AssertionError();
			}
		}
	}
}