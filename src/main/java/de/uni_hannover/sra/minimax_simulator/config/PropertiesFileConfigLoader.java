package de.uni_hannover.sra.minimax_simulator.config;

import de.uni_hannover.sra.minimax_simulator.io.IOUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

/**
 * A basic implementation of {@link ConfigurationLoader} using <i>.properties</i> files.
 * 
 * @author Martin L&uuml;ck
 */
public class PropertiesFileConfigLoader implements ConfigurationLoader {

    /**
     * Defines the behavior of the {@code PropertiesFileConfigLoader} in case
     * a property is missing in the property file.
     */
    public enum MissingConfigStrategy {
        /**
         * Use the default value if a property is missing.
         */
        USE_DEFAULT,

        /**
         * Throw an {@code ConfigurationException} if a property is missing.
         */
        THROW
    }

    /** determines what to do if a configuration value is missing */
    private final MissingConfigStrategy missingConfigStrategy;
    /** properties file */
    private final File propertiesFile;

    /**
     * Creates an instance of the {@code PropertiesFileConfigLoader} without an properties file
     * and the missing configuration strategy {@link de.uni_hannover.sra.minimax_simulator.config.PropertiesFileConfigLoader.MissingConfigStrategy#USE_DEFAULT}.
     */
    public PropertiesFileConfigLoader() {
        this(null, MissingConfigStrategy.USE_DEFAULT);
    }

    /**
     * Creates an instance of the {@code PropertiesFileConfigLoader} with the given file and
     * the missing configuration strategy {@link de.uni_hannover.sra.minimax_simulator.config.PropertiesFileConfigLoader.MissingConfigStrategy#USE_DEFAULT}.
     *
     * @param file
     *          the properties file
     */
    public PropertiesFileConfigLoader(File file) {
        this(file, MissingConfigStrategy.USE_DEFAULT);
    }

    /**
     * Creates an instance of the {@code PropertiesFileConfigLoader} without an properties file
     * and the given {@code MissingConfigStrategy}.
     *
     * @param missingConfigStrategy
     *          the {@code MissingConfigStrategy} to use
     */
    public PropertiesFileConfigLoader(MissingConfigStrategy missingConfigStrategy) {
        this(null, missingConfigStrategy);
    }

    /**
     * Creates an instance of the {@code PropertiesFileConfigLoader} with the given file and the
     * given {@code MissingConfigStrategy}.
     *
     * @param file
     *          the properties file
     * @param missingConfigStrategy
     *          the {@code MissingConfigStrategy} to use
     */
    public PropertiesFileConfigLoader(File file, MissingConfigStrategy missingConfigStrategy) {
        this.propertiesFile = file;
        this.missingConfigStrategy = missingConfigStrategy;
    }

    @Override
    public void configure(Class<?>... classes) throws ConfigurationException {
        for (Class<?> clazz : classes) {
            File file;

            ConfigurationFile fileAnnotation = clazz.getAnnotation(ConfigurationFile.class);
            if (fileAnnotation == null) {
                if (propertiesFile == null) {
                    if (missingConfigStrategy == MissingConfigStrategy.THROW) {
                        throw new ConfigurationException("Missing ConfigurationFile annotation for: " + clazz.getName());
                    }

                    file = null;
                }
                else {
                    file = propertiesFile;
                }
            }
            else {
                file = new File(fileAnnotation.file());
            }

            Properties properties = new Properties();

            if (file != null) {
                loadPropertiesFrom(file, properties);
            }

            configureStaticFields(clazz, properties);
        }
    }

    /**
     * Loads the properties from a file.
     *
     * @param configFile
     *          the file to read the properties from
     * @param properties
     *          the {@code Properties} instance to hold the read properties
     * @throws ConfigurationException
     *          thrown if a property is missing and the missing configuration strategy is set to
     *          {@link de.uni_hannover.sra.minimax_simulator.config.PropertiesFileConfigLoader.MissingConfigStrategy#THROW}.
     */
    private void loadPropertiesFrom(File configFile, Properties properties) throws ConfigurationException {
        Reader r = null;
        try {
            r = IOUtils.toBufferedReader(new FileReader(configFile));
            properties.load(r);
        } catch (FileNotFoundException fnfe) {
            if (missingConfigStrategy == MissingConfigStrategy.THROW) {
                throw new ConfigurationException("Configuration file not found: " + configFile.getPath(), fnfe);
            }
            // else: do not modify the Properties instance
        } catch (IOException e) {
            if (missingConfigStrategy == MissingConfigStrategy.THROW) {
                throw new ConfigurationException("Cannot read configuration file: " + configFile.getPath(), e);
            }

            // else: do not modify the Properties instance
        } finally {
            IOUtils.closeQuietly(r);
        }
    }

    /**
     * Sets {@code static} fields of the given class annotated with the {@link Configuration} annotation.
     *
     * @param clazz
     *          the class for which the static fields should be configured
     * @param properties
     *          the {@code Properties} instance that holds all properties
     * @throws ConfigurationException
     *          thrown if the field is not {@code public}, {@code static} and {@code non-final}
     */
    private static void configureStaticFields(Class<?> clazz, Properties properties) throws ConfigurationException {
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            Configuration conf = f.getAnnotation(Configuration.class);
            if (conf == null) {
                // not a configurable property
                continue;
            }

            int mod = f.getModifiers();
            if (Modifier.isFinal(mod) || !Modifier.isStatic(mod) || !Modifier.isPublic(mod)) {
                throw new ConfigurationException("Configurable field of class " + clazz.getName() + " must be public, static and non-final: " + f.getName());
            }

            String key = conf.key();
            FieldType fieldType = conf.type();
            Class<?> fieldClass = f.getType();

            String textValue = properties.getProperty(key, conf.defaultValue());
            Object value;

            switch (fieldType) {
                case BYTESIZE:
                    value = Parser.toFileSize(textValue);
                    break;
                case AUTO:
                    if (fieldClass.isArray() && fieldClass.getComponentType() == String.class) {
                        value = Parser.toStrings(textValue, conf.separator());
                    }
                    else if (fieldClass.isEnum()) {
                        value = Parser.toEnum(textValue, (Class<? extends Enum>) fieldClass);
                    }
                    else {
                        value = Parser.auto(textValue, fieldClass);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported field type: " + fieldType);
            }

            try {
                f.set(null, value);
            } catch (Exception e) {
                // should never happen since the field is writable
                throw new AssertionError(e);
            }
        }
    }
}