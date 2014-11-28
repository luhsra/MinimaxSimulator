package de.uni_hannover.sra.minimax_simulator.model.machine.simulation;

public class TrackableInteger extends AbstractTrackable<Integer>
{
	private Integer _value;

	@Override
	public Integer get()
	{
		return _value;
	}

	@Override
	public void set(Integer value)
	{
		_value = value;
		fireValueChanged();
	}
}