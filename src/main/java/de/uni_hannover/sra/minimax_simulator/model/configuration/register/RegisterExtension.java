package de.uni_hannover.sra.minimax_simulator.model.configuration.register;

/**
 * A {@code RegisterExtension} represents a register of a register machine.
 *
 * @author Martin L&uuml;ck
 */
public class RegisterExtension {

	private final String _name;
	private final RegisterSize _size;
	private final String _description;
	private final boolean _isExtended;

	private final int _hashCache;

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

		_name = name;
		_size = size;
		_description = description;
		_isExtended = isExtended;

		_hashCache = computeHashCode();
	}

	/**
	 * Gets the name of the {@code RegisterExtension}.
	 *
	 * @return
	 *          the name of the {@code RegisterExtension}
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Gets the {@link RegisterSize} of the {@code RegisterExtension}.
	 *
	 * @return
	 *          the {@code RegisterSize} of the {@code RegisterExtension}
	 */
	public RegisterSize getSize() {
		return _size;
	}

	/**
	 * Gets the description of the {@code RegisterExtension}.
	 *
	 * @return
	 *          the description of the {@code RegisterExtension}
	 */
	public String getDescription() {
		return _description;
	}

	/**
	 * Gets the value of the {@code isExtended} property.
	 *
	 * @return
	 *          {@code false} if the {@code RegisterExtension} is part of the base machine, {@code true} otherwise
	 */
	public boolean isExtended() {
		return _isExtended;
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
		result = prime * result + _description.hashCode();
		result = prime * result + (_isExtended ? 1231 : 1237);
		result = prime * result + _name.hashCode();
		result = prime * result + _size.hashCode();
		return result;
	}

	@Override
	public int hashCode() {
		return _hashCache;
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

		if (!_name.equals(other._name)) {
			return false;
		}

		if (!_description.equals(other._description)) {
			return false;
		}

		if (_isExtended != other._isExtended) {
			return false;
		}

		if (_size != other._size) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return _name;
	}
}