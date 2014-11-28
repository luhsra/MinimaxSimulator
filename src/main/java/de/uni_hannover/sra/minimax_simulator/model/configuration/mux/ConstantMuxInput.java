package de.uni_hannover.sra.minimax_simulator.model.configuration.mux;

public class ConstantMuxInput implements MuxInput
{
	private final int _constant;

	public ConstantMuxInput(int constant)
	{
		_constant = constant;
	}

	public int getConstant()
	{
		return _constant;
	}

	@Override
	public String toString()
	{
		return "ConstantMuxInput[" + _constant + "]";
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

		return ((ConstantMuxInput) o)._constant == _constant;
	}

	@Override
	public int hashCode()
	{
		return 31 * _constant;
	}

	@Override
	public String getName()
	{
		return Integer.toString(_constant);
	}
}