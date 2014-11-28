package de.uni_hannover.sra.minimax_simulator.model.signal.jump;

public final class UnconditionalJump implements Jump
{
	private final int _targetRow;

	public UnconditionalJump(int targetRow)
	{
		_targetRow = targetRow;
	}

	@Override
	public int getTargetRow(int currentRow, int condition)
	{
		return _targetRow;
	}

	public int getTargetRow()
	{
		return _targetRow;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + _targetRow;
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

		UnconditionalJump other = (UnconditionalJump) obj;
		if (_targetRow != other._targetRow)
			return false;
		return true;
	}
}