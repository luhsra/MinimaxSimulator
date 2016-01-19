package de.uni_hannover.sra.minimax_simulator.config;

/**
 * The {@code FieldType} of a configuration determines how the configured string is tried to be parsed
 * to a meaningful value.<br>
 * For most fields, the default value ({@link #AUTO}) is reasonable since they are meant to be parsed according
 * to their actual java field types.
 * 
 * @author Martin L&uuml;ck
 */
public enum FieldType {

    /**
     * Automatically deduce the parsing mode from the field type (String, int, long, ...).
     */
    AUTO,

    /**
     * Interpret the string value of the configuration for this field as a size given in bytes.
     * If chosen, the field value can have a binary unit prefix applied, as K, M or G, for
     * kilo, mega, giga.
     */
    BYTESIZE
}