package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import java.util.Set;

/**
 * 
 * 
 * 
 * @author Martin
 *
 */
public interface Constraint
{
	/**
	 * 
	 * @param attributes
	 * @return
	 */
	public int getValue(AttributeSource attributes);

	/**
	 * 
	 * @return
	 */
	public Set<Attribute> getDependencies();
}