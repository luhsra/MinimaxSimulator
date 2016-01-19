package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import java.util.Set;

/**
 * A {@code Constraint} is used for constraining {@link Attribute}s.
 * 
 * @author Martin L&uuml;ck
 */
public interface Constraint {

    /**
     * Gets the value of the {@code Constraint} using the specified {@link AttributeSource} for derivation.
     *
     * @param attributes
     *          the {@code AttributeSource}
     * @return
     *          the computed value
     */
    public int getValue(AttributeSource attributes);

    /**
     * Gets the dependencies of the {@code Constraint}.
     *
     * @return
     *          a set of the dependencies
     */
    public Set<Attribute> getDependencies();
}