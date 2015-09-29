package de.uni_hannover.sra.minimax_simulator.ui.common.models;

import javax.swing.SpinnerModel;
import javax.swing.event.ChangeListener;

@Deprecated
public class SpinnerNullModel implements SpinnerModel
{
	@Override
	public Object getValue()
	{
		return null;
	}

	@Override
	public void setValue(Object value)
	{
	}

	@Override
	public Object getNextValue()
	{
		return null;
	}

	@Override
	public Object getPreviousValue()
	{
		return null;
	}

	@Override
	public void addChangeListener(ChangeListener l)
	{
	}

	@Override
	public void removeChangeListener(ChangeListener l)
	{
	}
}