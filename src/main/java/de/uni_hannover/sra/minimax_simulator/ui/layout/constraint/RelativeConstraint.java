package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import java.util.Collections;
import java.util.Set;

/**
 * A {@link Constraint} for arranging thing relative to each other.
 *
 * @author Martin L&uuml;ck
 */
public class RelativeConstraint implements Constraint {

    private final Attribute anchor;
    private final int offset;

    private final Set<Attribute> dependency;

    /**
     * Constructs a new {@code RelativeConstraint} with the specified {@link Attribute} as anchor and
     * with the specified offset.
     *
     * @param anchor
     *          the anchor {@code Attribute}
     * @param offset
     *          the offset
     */
    public RelativeConstraint(Attribute anchor, int offset) {
        if (anchor == null) {
            throw new NullPointerException("anchor is null");
        }

        this.anchor = anchor;
        this.offset = offset;
        dependency = Collections.singleton(this.anchor);
    }

    /**
     * Constructs a new {@code RelativeConstraint} with the specified {@link Attribute} as anchor.
     *
     * @param anchor
     *          the anchor {@code Attribute}
     */
    public RelativeConstraint(Attribute anchor) {
        this(anchor, 0);
    }

    /**
     * Constructs a new {@code RelativeConstraint} with the specified name and {@link AttributeType} of
     * the anchor {@link Attribute}.
     *
     * @param anchor
     *          the name of the anchor {@code Attribute}
     * @param type
     *          the {@code AttributeType} of the anchor {@code Attribute}
     */
    public RelativeConstraint(String anchor, AttributeType type) {
        this(new Attribute(anchor, type));
    }

    /**
     * Constructs a new {@code RelativeConstraint} with the specified name and {@link AttributeType} of
     * the anchor {@link Attribute} and the specified offset.
     *
     * @param anchor
     *          the name of the anchor {@code Attribute}
     * @param type
     *          the {@code AttributeType} of the anchor {@code Attribute}
     * @param offset
     *          the offset
     */
    public RelativeConstraint(String anchor, AttributeType type, int offset) {
        this(new Attribute(anchor, type), offset);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((anchor == null) ? 0 : anchor.hashCode());
        result = prime * result + offset;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        else if (obj == null) {
            return false;
        }
        else if (getClass() != obj.getClass()) {
            return false;
        }

        RelativeConstraint other = (RelativeConstraint) obj;
        if (!anchor.equals(other.anchor)) {
            return false;
        }
        if (offset != other.offset) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "rel(" + anchor + " + " + offset + ")";
    }

    @Override
    public int getValue(AttributeSource attributes) {
        return attributes.getValue(anchor) + offset;
    }

    @Override
    public Set<Attribute> getDependencies() {
        return dependency;
    }
}