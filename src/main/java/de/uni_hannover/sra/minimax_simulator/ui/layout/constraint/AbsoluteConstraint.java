package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import java.util.Collections;
import java.util.Set;

/**
 * An absolute implementation of {@link Constraint}.
 *
 * @author Martin L&uuml;ck
 */
public class AbsoluteConstraint implements Constraint {

	private final int _value;

	/**
	 * Constructs a new {@code AbsoluteConstraint} with the specified value.
	 *
	 * @param value
	 *          the value of the {@code AbsoluteConstraint}
	 */
	public AbsoluteConstraint(int value) {
		_value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _value;
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

		AbsoluteConstraint other = (AbsoluteConstraint) obj;
		if (_value != other._value) {
			return false;
		}
		return true;
	}

	@Override
	public Set<Attribute> getDependencies() {
		return Collections.emptySet();
	}

	@Override
	public String toString() {
		return "abs(" + _value + ")";
	}

	@Override
	public int getValue(AttributeSource attributes) {
		return _value;
	}
}