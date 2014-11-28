package de.uni_hannover.sra.minimax_simulator.model.configuration.mux;

public class NullMuxInput implements MuxInput
{
	public final static MuxInput INSTANCE = new NullMuxInput();

	private NullMuxInput()
	{
	}

	@Override
	public String getName()
	{
		return "";
	}

	@Override
	public boolean equals(Object o)
	{
		return this == o;
	}

	@Override
	public int hashCode()
	{
		return 31;
	}
}