package de.uni_hannover.sra.minimax_simulator.model.signal.jump;

public final class DefaultJump implements Jump
{
	public final static Jump INSTANCE = new DefaultJump();

	private DefaultJump()
	{
	}

	@Override
	public int getTargetRow(int currentRow, int condition)
	{
		return currentRow + 1;
	}

	@Override
	public boolean equals(Object o)
	{
		return o == INSTANCE;
	}

	@Override
	public int hashCode()
	{
		return 31;
	}
}