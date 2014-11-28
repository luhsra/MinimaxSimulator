package de.uni_hannover.sra.minimax_simulator;

import de.uni_hannover.sra.minimax_simulator.config.Configuration;
import de.uni_hannover.sra.minimax_simulator.config.ConfigurationFile;

@ConfigurationFile(file = "config.properties")
public class Config
{
	@Configuration(key = "locale", defaultValue = "")
	public static String	LOCALE;

	@Configuration(key = "editor.slider-position", defaultValue = "200")
	public static int		EDITOR_SLIDER_POSITION;

	@Configuration(key = "editor.alu.expanded", defaultValue = "true")
	public static boolean	EDITOR_ALU_EXPANDED;

	@Configuration(key = "editor.register.expanded", defaultValue = "true")
	public static boolean	EDITOR_REGISTER_EXPANDED;

	@Configuration(key = "editor.memory.expanded", defaultValue = "false")
	public static boolean	EDITOR_MEMORY_EXPANDED;

	@Configuration(key = "editor.max-register-length", defaultValue = "20")
	public static int		EDITOR_MAX_REGISTER_LENGTH;

	@Configuration(key = "debug.schematics", defaultValue = "false")
	public static boolean	DEBUG_SCHEMATICS;
}