package de.uni_hannover.sra.minimax_simulator.config;

import java.lang.annotation.*;

/**
 * Indicates the name of the file holding the application's configuration.
 *
 * @see Configuration
 * @see Config
 * @author Martin L&uuml;ck
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigurationFile {

    /**
     * Gets the name of the configuration file.
     *
     * @return
     *          the name of the configuration file
     */
    public String file();
}