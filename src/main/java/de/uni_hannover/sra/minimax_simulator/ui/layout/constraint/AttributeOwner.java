package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import java.util.Set;

/**
 * An {@code AttributeOwner} is a class that has at least one {@link Attribute}.
 *
 * @author Martin L&uuml;ck
 */
public interface AttributeOwner {

    /**
     * Gets the name of the {@code AttributeOwner}.
     *
     * @return
     *          the name (non-null)
     */
    public String getName();

    /**
     * Checks if the specified {@link AttributeType} is already known for the instance of {@code AttributeOwner}.
     *
     * @param attribute
     *          the {@code AttributeType}
     * @return
     *          {@code true} if the specified {@code AttributeType} is already set for the {@code Attribute Owner}
     *          instance or derived from others of its attributes, {@code false} otherwise
     * @throws NullPointerException
     *          thrown if {@code attribute} is {@code null}
     */
    public boolean hasSet(AttributeType attribute);

    /**
     * Sets the specified attribute for the instance.
     *
     * @param attribute
     *          the {@code AttributeType} of the attribute
     * @param value
     *          the value of the attribute
     * @throws IllegalStateException
     *          thrown if a value for the attribute was already set or derived from other attributes
     * @throws NullPointerException
     *          thrown if {@code attribute} is {@code null}
     */
    public void set(AttributeType attribute, int value);

    /**
     * Gets the current value of the specified {@code AttributeType}.
     *
     * @param attribute
     *          the {@code AttributeType}
     * @return
     *          the value that was set or derived for the instance of {@code AttributeOwner}
     * @throws  IllegalStateException
     *          thrown if the value is not yet known
     * @throws NullPointerException
     *          thrown if {@code attribute} is {@code null}
     */
    public int get(AttributeType attribute);

    /**
     * Resets all attribute values. Until new values are set or calculated, {@link #hasSet(AttributeType)} will
     * always return {@code false} and {@link #get(AttributeType)} will always throw an exception.
     */
    public void clearAttributes();

    /**
     * Removes all constraints that are set on attributes of the instance of {@code AttributeOwner}.
     */
    public void clearConstraints();

    /**
     * Gets the assigned {@link Constraint} for the specified {@link AttributeType}.
     *
     * @param attribute
     *          the constrained {@code AttributeType}
     * @return
     *          the {@code Constraint} that was set for the {@code AttributeType} or {@code null}
     * @throws NullPointerException
     *          thrown if {@code attribute} is {@code null}
     */
    public Constraint getAttributeConstraint(AttributeType attribute);

    /**
     * Sets the {@link Constraint} for the specified {@link AttributeType}.<br>
     * <br>
     * If the attribute is already constrained the attribute constraint will be replaced.
     *
     * @param attribute
     *          the {@code AttributeType} to constrain
     * @param constraint
     *          the new {@code Constrain} for the {@code AttributeType}
     * @throws NullPointerException
     *          thrown if any argument is {@code null}
     * @throws IllegalStateException
     *          thrown if an over-constraining would result from applying the additionl {@code Constraint}
     */
    public void setAttributeConstraint(AttributeType attribute, Constraint constraint);

    /**
     * Clears the current {@link Constraint} for the specified {@link AttributeType}.
     * If no constraint is assigned for {@code attribute} nothing happens.
     *
     * @param attribute
     *          the {@code AttributeType} whose {@code Constraint}s will be removed
     * @throws NullPointerException
     *          thrown if {@code attribute} is {@code null}
     */
    public void removeAttributeConstraint(AttributeType attribute);

    /**
     * Computes the specified {@link AttributeType} for the instance of {@code AttributeOwner} using the
     * specified {@link AttributeSource}.<br>
     * If there is a {@link Constraint} for the attribute that {@code Constraint} will determine the new value.
     * Otherwise the instance will try to derive the attribute from other known attributes (e.g. known values for
     * left and right wil determine the width).
     *
     * @param attribute
     *          the {@code AttributeType} to compute
     * @param source
     *          the {@code AttributeSource} to use
     * @throws NullPointerException
     *          thrown if any argument is {@code null}
     * @throws IllegalStateException
     *          thrown if the instance fails to derive the attribute
     */
    public void computeAttribute(AttributeType attribute, AttributeSource source);

    /**
     * Gets all {@link Attribute}s associated with the instance of {@code AttributeOwner}.
     *
     * @return
     *          a set of the {@code Attribute}s
     */
    public Set<Attribute> getAttributes();

    /**
     * Gets all {@link Attribute}s that are required to know for the specified {@link AttributeType} to calculate.<br>
     * Those dependencies can either follow explicitly from {@link Constraint}s or can result from an {@code Attribute}
     * depending on derivation from other {@code Attribute}s which then will be contained in the returned set.
     *
     * @param attr
     *          the {@code AttributeType} to get the dependencies for
     * @return
     *          a set of the dependencies
     */
    public Set<Attribute> getDependencies(AttributeType attr);

    /**
     * Gets the preferred width of the instance of {@code AttributeOwner} that will apply if the width
     * is otherwise unconstrained and not derivable from other {@link Attribute}s.
     *
     * @return
     *          the preferred width
     */
    public int getPreferredWidth();

    /**
     * Gets the preferred height of the instance of {@code AttributeOwner} that will apply if the height
     * is otherwise unconstrained and not derivable from other {@link Attribute}s.
     *
     * @return
     *          the preferred height
     */
    public int getPreferredHeight();

    /**
     * Validates the {@link Constraint}s of the instance of {@code AttributeOwner}, that is checking if all
     * {@link Attribute}s of the instance are computable.
     *
     * @throws IllegalStateException
     *          thrown if the validation fails
     */
    public void validateConstraints();
}