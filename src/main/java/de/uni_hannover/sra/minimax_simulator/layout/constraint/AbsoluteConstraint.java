package de.uni_hannover.sra.minimax_simulator.layout.constraint;

import java.util.Collections;
import java.util.Set;

public class AbsoluteConstraint implements Constraint
{
	private final int _value;

	public AbsoluteConstraint(int value)
	{
		_value = value;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + _value;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		AbsoluteConstraint other = (AbsoluteConstraint) obj;
		if (_value != other._value)
			return false;
		return true;
	}

	@Override
	public Set<Attribute> getDependencies()
	{
		return Collections.emptySet();
	}

	@Override
	public String toString()
	{
		return "abs(" + _value + ")";
	}

	@Override
	public int getValue(AttributeSource attributes)
	{
		return _value;
	}
}