package de.uni_hannover.sra.minimax_simulator.model.configuration.mux;

import static com.google.common.base.Preconditions.*;

public class RegisterMuxInput implements MuxInput
{
	private final String _registerName;
	private final String _name;
	private final int _hashCache;

	public RegisterMuxInput(String registerName)
	{
		this(registerName, registerName);
	}

	public RegisterMuxInput(String registerName, String name)
	{
		_registerName = checkNotNull(registerName);
		_name = checkNotNull(name);

		checkArgument(!registerName.isEmpty());
		checkArgument(!name.isEmpty());

		_hashCache = hashCode0();
	}

	public String getRegisterName()
	{
		return _registerName;
	}

	@Override
	public String toString()
	{
		return "RegisterMuxInput[" + _registerName + "]";
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

		return ((RegisterMuxInput) o)._registerName.equals(_registerName)
				&& ((RegisterMuxInput) o)._name.equals(_name);
	}

	private int hashCode0()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + _registerName.hashCode();
		result = prime * result + _name.hashCode();
		return result;
	}

	@Override
	public int hashCode()
	{
		return _hashCache;
	}

	@Override
	public String getName()
	{
		return _name;
	}
}