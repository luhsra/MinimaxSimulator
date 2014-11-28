package de.uni_hannover.sra.minimax_simulator.model.configuration.register;

public class RegisterExtension
{
	private final String _name;
	private final RegisterSize _size;
	private final String _description;
	private final boolean _isExtended;

	private final int _hashCache;

	public RegisterExtension(String name, RegisterSize size, String description, boolean isExtended)
	{
		if (name == null)
			throw new IllegalArgumentException("Invalid argument: register name is null");
		if (name.isEmpty())
			throw new IllegalArgumentException("Invalid argument: register name is empty");

		if (size == null)
			throw new IllegalArgumentException("Invalid argument: register size is null");

		if (description == null)
			description = "";

		_name = name;
		_size = size;
		_description = description;
		_isExtended = isExtended;

		_hashCache = hashcode0();
	}

	public String getName()
	{
		return _name;
	}

	public RegisterSize getSize()
	{
		return _size;
	}

	public String getDescription()
	{
		return _description;
	}

	public boolean isExtended()
	{
		return _isExtended;
	}

	@Override
	public int hashCode()
	{
		return _hashCache;
	}

	private int hashcode0()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + _description.hashCode();
		result = prime * result + (_isExtended ? 1231 : 1237);
		result = prime * result + _name.hashCode();
		result = prime * result + _size.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;

		RegisterExtension other = (RegisterExtension) o;

		if (!_name.equals(other._name))
			return false;

		if (!_description.equals(other._description))
			return false;

		if (_isExtended != other._isExtended)
			return false;

		if (_size != other._size)
			return false;

		return true;
	}

	@Override
	public String toString()
	{
		return _name;
	}
}