package de.uni_hannover.sra.minimax_simulator.ui.common.models;

import javax.swing.AbstractSpinnerModel;

public class NullAwareIntegerSpinnerModel extends AbstractSpinnerModel
{
	private Integer _value;

	@Override
	public Object getValue()
	{
		return _value;
	}

	@Override
	public void setValue(Object value)
	{
		if (value instanceof Integer)
		{
			if (_value == null || !_value.equals(value))
			{
				_value = (Integer) value;
				fireStateChanged();
			}
		}
		else if (value == null)
		{
			if (_value != null)
			{
				_value = null;
				fireStateChanged();
			}
		}
		else
			throw new IllegalArgumentException("Cannot store " + value);
	}

	@Override
	public Object getNextValue()
	{
		if (_value == null)
			throw new IllegalStateException("Set a numerical value first");
		return _value + 1;
	}

	@Override
	public Object getPreviousValue()
	{
		if (_value == null)
			throw new IllegalStateException("Set a numerical value first");
		return _value - 1;
	}
}