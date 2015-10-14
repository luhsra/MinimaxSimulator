package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

public final class Attribute
{
	private final String		_owner;
	private final AttributeType	_type;

	private final int			_cachedHashCode;

	public Attribute(String owner, AttributeType type)
	{
		if (owner == null)
			throw new NullPointerException("owner is null");
		if (type == null)
			throw new NullPointerException("type is null");

		_owner = owner.intern();
		_type = type;
		_cachedHashCode = _owner.hashCode() ^ _type.hashCode();
	}

	public String getOwner()
	{
		return _owner;
	}

	public AttributeType getType()
	{
		return _type;
	}

	@Override
	public int hashCode()
	{
		return _cachedHashCode;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj.getClass() != Attribute.class)
			return false;

		Attribute other = (Attribute) obj;
		if (_owner != other._owner)
			return false;
		if (_type != other._type)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return _owner + "." + _type;
	}
}