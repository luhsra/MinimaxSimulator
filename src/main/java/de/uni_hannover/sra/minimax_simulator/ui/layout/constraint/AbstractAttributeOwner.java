package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Basic implementation of {@link AttributeOwner}.
 *
 * @author Martin L&uuml;ck
 */
abstract class AbstractAttributeOwner implements AttributeOwner {

	private final String name;

	private final EnumMap<AttributeType, Integer> values;
	private final EnumMap<AttributeType, Constraint> constraints;

	/**
	 * Constructs a new {@code AbstractAttributeOwner} with the specified name.
	 *
	 * @param name
	 *          the name of the {@code AbstractAttributeOwner}
	 */
	protected AbstractAttributeOwner(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name must not be null");
		}

		this.name = name;
		values = new EnumMap<AttributeType, Integer>(AttributeType.class);
		constraints = new EnumMap<AttributeType, Constraint>(AttributeType.class);
	}

	@Override
	public boolean hasSet(AttributeType attribute) {
		if (attribute == null) {
			throw new NullPointerException(name + ": attribute is null");
		}

		return values.containsKey(attribute);
	}

	@Override
	public void set(AttributeType attribute, int value) {
		if (attribute == null) {
			throw new NullPointerException(name + ": attribute is null");
		}

		if (values.containsKey(attribute)) {
			throw new IllegalStateException(name + ": Value for " + attribute + " already set");
		}
		values.put(attribute, value);
	}

	@Override
	public int get(AttributeType attribute) {
		if (attribute == null) {
			throw new NullPointerException(name + ": attribute is null");
		}

		if (!values.containsKey(attribute)) {
			throw new IllegalStateException(name + ": Unresolved attribute " + attribute);
		}
		return values.get(attribute);
	}

	@Override
	public void clearAttributes() {
		values.clear();
	}

	@Override
	public void clearConstraints() {
		constraints.clear();
	}

	@Override
	public Constraint getAttributeConstraint(AttributeType attribute) {
		if (attribute == null) {
			throw new IllegalArgumentException(name + ": Attribute type must not be null");
		}

		return constraints.get(attribute);
	}

	@Override
	public void setAttributeConstraint(AttributeType attribute, Constraint constraint) {
		if (attribute == null) {
			throw new IllegalArgumentException(name + ": Attribute type must not be null");
		}
		if (constraint == null) {
			throw new IllegalArgumentException(name + ": Constraint must not be null");
		}

		// Allow replacing existing constraint
//		if (constraints.containsKey(attribute))
//			throw new IllegalStateException(name + ": Duplicate constraint on "
//				+ attribute);

		Set<AttributeType> constrainedSameAxisAttrs =
				new HashSet<AttributeType>(constraints.keySet());
		constrainedSameAxisAttrs
				.retainAll(AttributeType.getAxisTypes(attribute.getAxis()));

		// Allow replacing existing constraint
		constrainedSameAxisAttrs.remove(attribute);

		if (constrainedSameAxisAttrs.size() >= 2) {
			throw new IllegalStateException(name + ": At most 2 constraints on the " + attribute.getAxis() + " axis allowed");
		}

		constraints.put(attribute, constraint);
	}

	@Override
	public void removeAttributeConstraint(AttributeType attribute) {
		if (attribute == null) {
			throw new IllegalArgumentException(name + ": Attribute type must not be null");
		}

		constraints.remove(attribute);
	}

	@Override
	public void validateConstraints() {
		Set<AttributeType> axisConstraints = new HashSet<AttributeType>(constraints.keySet());
		axisConstraints.retainAll(AttributeType.getAxisTypes(AttributeAxis.HORIZONTAL));
		if (axisConstraints.size() < 1 || (axisConstraints.size() == 1 && axisConstraints.contains(AttributeType.WIDTH))) {
			throw new IllegalStateException(name + ": underconstrained in x axis");
		}
		axisConstraints.addAll(constraints.keySet());
		axisConstraints.retainAll(AttributeType.getAxisTypes(AttributeAxis.VERTICAL));
		if (axisConstraints.size() < 1 || (axisConstraints.size() == 1 && axisConstraints.contains(AttributeType.HEIGHT))) {
			throw new IllegalStateException(name + ": is underconstrained in y axis");
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void computeAttribute(AttributeType attribute, AttributeSource source) {
		if (source == null) {
			throw new NullPointerException(name + ": source is null");
		}

		Constraint constraint = constraints.get(attribute);
		if (constraint != null) {
			set(attribute, constraint.getValue(source));
		}
		else {
			set(attribute, attribute.deriveValue(this));
		}
	}

	@Override
	public Set<Attribute> getAttributes() {
		Set<Attribute> attrs = new HashSet<Attribute>();
		for (AttributeType type : AttributeType.values()) {
			attrs.add(new Attribute(getName(), type));
		}
		return attrs;
	}

	@Override
	public Set<Attribute> getDependencies(AttributeType attribute) {
		Set<Attribute> attrs = new HashSet<Attribute>();

		Constraint constraint = constraints.get(attribute);

		if (constraint != null) {
			// if an attribute is constrained, it depends on some attributes of (other) AttributeOwners
			for (Attribute dependency : constraint.getDependencies()) {
				attrs.add(dependency);
			}
		}
		else {
			// If the attribute is unconstrained, it implicitly depends
			// on all constrained attributes on the same axis, since they are
			// required for attribute derivation.
			// These are dependencies on the instance itself.
			for (AttributeType constrainedAttr : constraints.keySet()) {
				if (constrainedAttr.getAxis() == attribute.getAxis()) {
					attrs.add(new Attribute(name, constrainedAttr));
				}
			}
		}

		return attrs;
	}

	@Override
	public String toString() {
		return name;
	}
}