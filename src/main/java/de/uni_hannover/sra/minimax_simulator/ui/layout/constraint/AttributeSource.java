package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

/**
 * While an {@link AttributeOwner} is constrained by {@link Attribute}s an {@code AttributeSource}
 * is the class the {@code Attribute} comes from.
 *
 * @author Martin L&uuml;ck
 */
public interface AttributeSource {

	/**
	 * Gets the value of the specified {@link Attribute}.
	 *
	 * @param attribute
	 *          the {@code Attribute} to get the value for
	 * @return
	 *          the value of the {@code Attribute}
	 */
	public int getValue(Attribute attribute);
}