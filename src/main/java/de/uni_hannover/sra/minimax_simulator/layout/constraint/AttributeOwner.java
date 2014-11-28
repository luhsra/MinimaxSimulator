package de.uni_hannover.sra.minimax_simulator.layout.constraint;

import java.util.Set;

public interface AttributeOwner
{
	/**
	 * @return the name of this attribute owner (a non-null string).
	 */
	public String getName();

	/**
	 * Returns if the given attribute is already known for this instance.
	 * 
	 * @param attr
	 * @return <code>true</code> if the given attribute for this instance is already set or derived from others of its attributes,
	 * otherwise <code>false</code>
	 * @throws NullPointerException if <code>attribute</code> is null
	 */
	public boolean hasSet(AttributeType attribute);

	/**
	 * Set the given attribute for this instance.
	 * 
	 * @param attr the attribute type of the value
	 * @param value the value
	 * @throws IllegalStateException if a value for the attribute was already set or derived from other attributes.
	 * @throws NullPointerException if <code>attribute</code> is null
	 * 	 */
	public void set(AttributeType attribute, int value);

	/**
	 * Retrieves the current value for the given attribute type.<br><br>

	 * @param attr the attribute type (e.g. left, top, bottom)
	 * @return the value that was set or derived in this instance for the given attribute type
	 * @throws IllegalStateException if the value is not yet known
	 * @throws NullPointerException if <code>attribute</code> is null
	 */
	public int get(AttributeType attribute);

	/**
	 * Resets all attribute values. Until new values are set or calculated, {@link #hasSet(AttributeType)} will always return false,
	 * and {@link #get(AttributeType)} will always throw an exception.
	 * 
	 */
	public void clearAttributes();

	/**
	 * Removes all constraints that are set on attributes of this instance.
	 */
	public void clearConstraints();

	/**
	 * Returns the assigned constraint for the given attribute.<br><br>

	 * @param attribute the constrained attribute type (e.g. left, top, bottom)
	 * @return the constraint that was set for the attribute type, or null
	 * @throws NullPointerException if the parameter is null
	 */
	public Constraint getAttributeConstraint(AttributeType attribute);


	/**
	 * Sets the constraint for the given attribute.<br><br>
	 * 
	 * If the attribute is already constrained, the attribute constraint will
	 * be replaced by the parameter.
	 * 
	 * @param attribute the attribute type to constrain (e.g. left, top, bottom)
	 * @param constraint the new constraint for the given attribute type
	 * @throws NullPointerException if any parameter is null
	 * @throws IllegalStateException if an over-constraining would result from applying the additional constraint
	 */
	public void setAttributeConstraint(AttributeType attribute, Constraint constraint);

	/**
	 * Clears the current constraint for the given attribute type.
	 * If no constraint is assigned for the attribute type <code>attribute</code>,
	 * nothing happens.
	 * 
	 * @param attribute the attribute whose constraints will be removed
	 * @throws NullPointerException if <code>attribute</code> is null
	 */
	public void removeAttributeConstraint(AttributeType attribute);

	/**
	 * Computes the given attribute type for this instance, using the given attribute source.<br>
	 * If there is a constraint for the attribute type, that constraint will determine the new value. Otherwise
	 * the instance will try to derive the attribute from other known attributes (e.g. known values for left and right
	 * will determine the width).<br>
	 * 
	 * @param attribute
	 * @param source
	 * @throws NullPointerException if any parameter is null
	 * @throws IllegalStateException if the instance fails to derive the attribute
	 */
	public void computeAttribute(AttributeType attribute, AttributeSource source);

	/**
	 * Creates a set containing all attributes that are associated with this instance.
	 * 
	 * @return the set
	 */
	public Set<Attribute> getAttributes();

	/**
	 * Creates a set of all attributes that are required to know for the given
	 * attribute type to calculate.<br>
	 * Those dependencies can either follow explicitely from constraints or can result
	 * from an attribute depending on derivation from other attributes, which then will be 
	 * contained in the returned set. 
	 * 
	 * @return the set
	 */
	public Set<Attribute> getDependencies(AttributeType attr);

	/**
	 * Returns the preferred width of this instance that will apply if the width
	 * is otherwise unconstrained and not derivable from other attributes.
	 * 
	 * @return the width
	 */
	public int getPreferredWidth();

	/**
	 * Returns the preferred height of this instance that will apply if the height
	 * is otherwise unconstrained and not derivable from other attributes.
	 * 
	 * @return the height
	 */
	public int getPreferredHeight();

	/**
	 * Validates the constraints on this instance, that is, checks if all attributes of this
	 * instance can be computed.
	 * 
	 * @throws IllegalStateException if the validation fails
	 */
	public void validateConstraints();
}