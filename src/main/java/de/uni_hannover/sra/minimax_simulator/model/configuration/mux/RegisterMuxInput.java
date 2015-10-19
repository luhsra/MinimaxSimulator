package de.uni_hannover.sra.minimax_simulator.model.configuration.mux;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A {@code RegisterMuxInput} is a {@link MuxInput} consisting of a register ({@link de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension}).
 *
 * @author Martin L&uuml;ck
 */
public class RegisterMuxInput implements MuxInput {

	private final String _registerName;
	private final String _name;
	private final int _hashCache;

	/**
	 * Constructs a new {@code RegisterMuxInput} using the specified register.
	 *
	 * @param registerName
	 *          the name of the {@link de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension} belonging to the {@code RegisterMuxInput}
	 */
	public RegisterMuxInput(String registerName) {
		this(registerName, registerName);
	}

	/**
	 * Constructs a new {@code RegisterMuxInput} using the specified register and display name.
	 *
	 * @param registerName
	 *          the name of the {@link de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension} belonging to the {@code RegisterMuxInput}
	 * @param name
	 *          the name to display name of the {@code RegisterMuxInput}
	 */
	public RegisterMuxInput(String registerName, String name) {
		_registerName = checkNotNull(registerName);
		_name = checkNotNull(name);

		checkArgument(!registerName.isEmpty());
		checkArgument(!name.isEmpty());

		_hashCache = computeHashCode();
	}

	/**
	 * Gets the name of the register belonging to the {@code RegisterMuxInput}.
	 *
	 * @return
	 *          the name of the register belonging to the {@code RegisterMuxInput}
	 */
	public String getRegisterName() {
		return _registerName;
	}

	@Override
	public String toString() {
		return "RegisterMuxInput[" + _registerName + "]";
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

		return ((RegisterMuxInput) o)._registerName.equals(_registerName) && ((RegisterMuxInput) o)._name.equals(_name);
	}

	/**
	 * Computes the hash code of the {@code RegisterMuxInput}.
	 *
	 * @return
	 *          the computed hash code
	 */
	private int computeHashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _registerName.hashCode();
		result = prime * result + _name.hashCode();
		return result;
	}

	@Override
	public int hashCode() {
		return _hashCache;
	}

	@Override
	public String getName() {
		return _name;
	}
}