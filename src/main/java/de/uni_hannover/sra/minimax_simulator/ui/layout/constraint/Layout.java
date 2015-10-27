package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import java.util.Set;

/**
 * A {@code Layout} contains the {@link Constraint}s of a single component inside a {@link ConstraintContainer}.
 * Its purpose is to allow simple management of the set of {@code Constraint}s of one component using a {@link LayoutManager}.
 * <br><br>
 * A {@code Layout} is considered mutable, that is the values that are returned by the methods are expected to change over time.
 * <br><br>
 * Therefore when using a {@code LayoutManager} one should not constrain components linked to a {@code Layout}
 * manually since those {@code Constraint}s are not guaranteed to be persistent.
 * 
 * @author Martin L&uuml;ck
 */
public interface Layout {

	/**
	 * Gets all {@link AttributeType}s with {@link Constraint}s.
	 *
	 * @return
	 *          a set of the {@code AttributeType}s
	 */
	public Set<AttributeType> getConstrainedAttributes();

	/**
	 * Gets the {@link Constraint} of the specified {@link AttributeType}.
	 *
	 * @param attribute
	 *          the {@code AttributeType}
	 * @return
	 *          the {@code Constraint}
	 */
	public Constraint getConstraint(AttributeType attribute);
}