package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

public interface ConstraintsManager
{
	public void addConstraint(String owner, AttributeType attribute, Constraint con);

	public void setConstraint(String owner, AttributeType attribute, Constraint con);

	public void clearConstraints(String owner);

	public void removeConstraint(String owner, AttributeType attribute);

	public ConstraintFactory createConstraintFactory();
}