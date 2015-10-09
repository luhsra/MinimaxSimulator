package de.uni_hannover.sra.minimax_simulator.model.signal.jump;

public final class ConditionalJump implements Jump
{
	private final int _condZeroTarget;
	private final int _condOneTarget;

	public ConditionalJump(int condZeroTarget, int condOneTarget)
	{
		_condZeroTarget = condZeroTarget;
		_condOneTarget = condOneTarget;
	}

	@Override
	public int getTargetRow(int currentRow, int condition) {
		return getTargetRow(condition);
	}

	public int getTargetRow(int condition)
	{
		return condition == 0 ? _condZeroTarget : _condOneTarget;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + _condOneTarget;
		result = prime * result + _condZeroTarget;
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

		ConditionalJump other = (ConditionalJump) obj;
		if (_condOneTarget != other._condOneTarget)
			return false;
		if (_condZeroTarget != other._condZeroTarget)
			return false;
		return true;
	}
}