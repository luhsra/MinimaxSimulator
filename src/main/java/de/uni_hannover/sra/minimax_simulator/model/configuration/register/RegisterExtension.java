package de.uni_hannover.sra.minimax_simulator.model.configuration.register;

/**
 * A {@code RegisterExtension} represents a register of a register machine.
 *
 * @author Martin L&uuml;ck
 */
public class RegisterExtension {

	private final String name;
	private final RegisterSize size;
	private final String description;
	private final boolean isExtended;

	private final int hashCache;

	/**
	 * Constructs a new {@code RegisterExtension} with the specified information.
	 *
	 * @param name
	 *          the name of the {@code RegisterExtension}
	 * @param size
	 *          the {@link RegisterSize} of the {@code RegisterExtension}
	 * @param description
	 *          the description of the {@code RegisterExtension}
	 * @param isExtended
	 *          whether the {@code RegisterExtension} belongs to the base machine or not
	 */
	public RegisterExtension(String name, RegisterSize size, String description, boolean isExtended) {
		if (name == null) {
			throw new IllegalArgumentException("Invalid argument: register name is null");
		}
		if (name.isEmpty()) {
			throw new IllegalArgumentException("Invalid argument: register name is empty");
		}

		if (size == null) {
			throw new IllegalArgumentException("Invalid argument: register size is null");
		}

		if (description == null) {
			description = "";
		}

		this.name = name;
		this.size = size;
		this.description = description;
		this.isExtended = isExtended;

		hashCache = computeHashCode();
	}

	/**
	 * Gets the name of the {@code RegisterExtension}.
	 *
	 * @return
	 *          the name of the {@code RegisterExtension}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the {@link RegisterSize} of the {@code RegisterExtension}.
	 *
	 * @return
	 *          the {@code RegisterSize} of the {@code RegisterExtension}
	 */
	public RegisterSize getSize() {
		return size;
	}

	/**
	 * Gets the description of the {@code RegisterExtension}.
	 *
	 * @return
	 *          the description of the {@code RegisterExtension}
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the value of the {@code isExtended} property.
	 *
	 * @return
	 *          {@code false} if the {@code RegisterExtension} is part of the base machine, {@code true} otherwise
	 */
	public boolean isExtended() {
		return isExtended;
	}

	/**
	 * Computes the hash code for the {@code RegisterExtension}.
	 *
	 * @return
	 *          the computed hash code
	 */
	private int computeHashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + description.hashCode();
		result = prime * result + (isExtended ? 1231 : 1237);
		result = prime * result + name.hashCode();
		result = prime * result + size.hashCode();
		return result;
	}

	@Override
	public int hashCode() {
		return hashCache;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (getClass() != o.getClass()) {
			return false;
		}

		RegisterExtension other = (RegisterExtension) o;

		if (!name.equals(other.name)) {
			return false;
		}

		if (!description.equals(other.description)) {
			return false;
		}

		if (isExtended != other.isExtended) {
			return false;
		}

		if (size != other.size) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return name;
	}
}