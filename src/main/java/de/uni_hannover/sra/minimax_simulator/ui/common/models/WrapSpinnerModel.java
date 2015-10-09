package de.uni_hannover.sra.minimax_simulator.ui.common.models;

import static com.google.common.base.Preconditions.*;

import javax.swing.AbstractSpinnerModel;

@Deprecated
public class WrapSpinnerModel extends AbstractSpinnerModel
{
	private Integer _currentValue;

	private final Integer _minValue;
	private final Integer _maxValue;

	public WrapSpinnerModel(Integer value, Integer min, Integer max)
	{
		checkArgument(max.intValue() >= min.intValue());
		_currentValue = value;

		_minValue = min;
		_maxValue = max;
	}

	@Override
	public Object getValue()
	{
		return _currentValue;
	}

	public Integer getMinValue()
	{
		return _minValue;
	}

	public Integer getMaxValue()
	{
		return _maxValue;
	}

	@Override
	public void setValue(Object value)
	{
		if (value instanceof Integer)
		{
			if (_currentValue == null || !_currentValue.equals(value))
			{
				_currentValue = (Integer) value;
				fireStateChanged();
			}
		}
		else if (value == null)
		{
			if (_currentValue != null)
			{
				_currentValue = null;
				fireStateChanged();
			}
		}
		else
			throw new IllegalArgumentException("Cannot store " + value);
	}

	@Override
	public Object getNextValue()
	{
		int next;
		if (_currentValue == null)
			next = 0;
		else
			next = _currentValue + 1;
		if (next > _maxValue)
			next = _minValue;

		return Integer.valueOf(next);
	}

	@Override
	public Object getPreviousValue()
	{
		int next;
		if (_currentValue == null)
			next = 0;
		else
			next = _currentValue - 1;

		if (next < _minValue)
			next = _maxValue;

		return Integer.valueOf(next);
	}
}