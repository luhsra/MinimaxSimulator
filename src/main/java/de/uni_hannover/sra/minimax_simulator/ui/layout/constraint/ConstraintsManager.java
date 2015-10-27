package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

/**
 * A manager for the {@link Constraint}s of several {@link AttributeOwner}s.
 *
 * @author Martin L&uuml;ck
 */
public interface ConstraintsManager {

	/**
	 * Adds the specified {@link Constraint} to the specified {@link AttributeType} of the {@link AttributeOwner}.
	 *
	 * @param owner
	 *          the name of the {@code AttributeOwner}
	 * @param attribute
	 *          the {@code AttributeType}
	 * @param con
	 *          the {@code Constraint}
	 */
	public void addConstraint(String owner, AttributeType attribute, Constraint con);

	/**
	 * Sets the specified {@link Constraint} to the specified {@link AttributeType} of the {@link AttributeOwner}.
	 *
	 * @param owner
	 *          the name of the {@code AttributeOwner}
	 * @param attribute
	 *          the {@code AttributeType}
	 * @param con
	 *          the {@code Constraint}
	 */
	public void setConstraint(String owner, AttributeType attribute, Constraint con);

	/**
	 * Removes all {@link Constraint}s of the specified {@link AttributeOwner}.
	 *
	 * @param owner
	 *          the name of the {@code AttributeOwner}
	 */
	public void clearConstraints(String owner);

	/**
	 * Removes the {@link Constraint}s of the specified {@link AttributeType} from the specified {@link AttributeOwner}.
	 *
	 * @param owner
	 *          the name of the {@code AttributeOwner}
	 * @param attribute
	 *          the {@code AttributeType}
	 */
	public void removeConstraint(String owner, AttributeType attribute);

	/**
	 * Gets the {@link ConstraintFactory} of the manager.
	 *
	 * @return
	 *          the {@code ConstraintFactory}
	 */
	public ConstraintFactory createConstraintFactory();
}