package de.uni_hannover.sra.minimax_simulator;

import de.uni_hannover.sra.minimax_simulator.config.Configuration;
import de.uni_hannover.sra.minimax_simulator.config.ConfigurationFile;

/**
 * This class holds the actual configuration information of the application.
 *
 * @see Configuration
 * @see ConfigurationFile
 *
 * @author Martin L&uuml;ck
 */
@ConfigurationFile(file = "config.properties")
public class Config {

	/** The language key of the language to use for localized texts. */
	@Configuration(key = "locale", defaultValue = "")
	public static String	LOCALE;

	/** The initial editor slider position. */
	@Configuration(key = "editor.slider-position", defaultValue = "200")
	public static int		EDITOR_SLIDER_POSITION;

	/** The initial value of the {@code expanded} property of the ALU. */
	@Configuration(key = "editor.alu.expanded", defaultValue = "true")
	public static boolean	EDITOR_ALU_EXPANDED;

	/** The initial value of the {@code expanded} property of the registers. */
	@Configuration(key = "editor.register.expanded", defaultValue = "true")
	public static boolean	EDITOR_REGISTER_EXPANDED;

	/** The initial value of the {@code expanded} property of the memory. */
	@Configuration(key = "editor.memory.expanded", defaultValue = "false")
	public static boolean	EDITOR_MEMORY_EXPANDED;

	/** The maximum length of a register name. */
	@Configuration(key = "editor.max-register-length", defaultValue = "20")
	public static int		EDITOR_MAX_REGISTER_LENGTH;

	/** Enable/disable the schematics debugging. */
	@Configuration(key = "debug.schematics", defaultValue = "false")
	public static boolean	DEBUG_SCHEMATICS;
}