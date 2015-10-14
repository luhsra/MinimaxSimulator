package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import java.util.Set;

/**
 * A layout contains the constraints of a single component inside a ConstraintContainer.
 * Its purpose is to allow simple management of the set of constraints
 * of one component using a LayoutManager.
 * <br><br>
 * A Layout is considered mutable, that is, the values that are returned by the
 * methods are expected to change over time.
 * <br><br>
 * Therefore, when using a LayoutManager, one should not constrain components
 * linked to a Layout manually since those constraints are not guaranteed to
 * be persistent.
 * 
 * @author Martin
 *
 */
public interface Layout
{
	public Set<AttributeType> getConstrainedAttributes();

	public Constraint getConstraint(AttributeType attribute);
}