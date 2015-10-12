package de.uni_hannover.sra.minimax_simulator.config;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigurationFile
{
	public String file();
}