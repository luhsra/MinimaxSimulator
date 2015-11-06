package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

/**
 * An {@code Attribute} is the representation of an attribute for the layouts.
 *
 * @author Martin L&uuml;ck
 */
public final class Attribute {

	private final String owner;
	private final AttributeType type;

	private final int cachedHashCode;

	/**
	 * Constructs a new {@code Attribute} with the specified {@link AttributeOwner} and {@link AttributeType}.
	 *
	 * @param owner
	 *          the name of the owner of the {@code Attribute}
	 * @param type
	 *          the type of the {@code Attribute}
	 */
	public Attribute(String owner, AttributeType type) {
		if (owner == null) {
			throw new NullPointerException("owner is null");
		}
		if (type == null) {
			throw new NullPointerException("type is null");
		}

		this.owner = owner.intern();
		this.type = type;
		cachedHashCode = this.owner.hashCode() ^ this.type.hashCode();
	}

	/**
	 * Gets the name of the {@link AttributeOwner}.
	 *
	 * @return
	 *          the name of the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Gets the {@link AttributeType}.
	 *
	 * @return
	 *          the type of the {@code Attribute}
	 */
	public AttributeType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return cachedHashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		else if (obj == null) {
			return false;
		}
		else if (obj.getClass() != Attribute.class) {
			return false;
		}

		Attribute other = (Attribute) obj;
		if (!owner.equals(other.owner)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return owner + "." + type;
	}
}