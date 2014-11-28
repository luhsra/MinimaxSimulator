package de.uni_hannover.sra.minimax_simulator.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Configuration
{
	public String key();
	public String defaultValue();
	public FieldType type() default FieldType.AUTO;

	// Only needed for String[]
	public String separator() default ",";
}