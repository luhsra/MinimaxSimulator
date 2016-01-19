package de.uni_hannover.sra.minimax_simulator.config;

import java.lang.annotation.*;

/**
 * A field annotated with {@code Configuration} is part of the application's configuration.
 *
 * @author Martin L&uuml;ck
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Configuration {

    /**
     * Gets the key of the configuration field.
     *
     * @return
     *          the key of the configuration field
     */
    public String key();

    /**
     * Gets the default value of the configuration field.
     *
     * @return
     *          the default value of the configuration field
     */
    public String defaultValue();

    /**
     * Gets the {@link FieldType} of the configuration field.<br><br>
     * The default value is {@link FieldType#AUTO}.
     *
     * @return
     *          the {@code FieldType} of the configuration field
     */
    public FieldType type() default FieldType.AUTO;

    /**
     * Gets the string separator.<br>
     * This is only needed for String[] configuration fields.<br><br>
     * The default separator is a comma.
     *
     * @return
     *          the separator used for String[] fields
     */
    public String separator() default ",";
}