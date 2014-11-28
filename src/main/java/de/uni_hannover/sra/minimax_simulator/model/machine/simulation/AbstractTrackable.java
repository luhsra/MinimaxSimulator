package de.uni_hannover.sra.minimax_simulator.model.machine.simulation;

import java.util.ArrayList;

public abstract class AbstractTrackable<T> implements Trackable<T>
{
	private final ArrayList<TrackableChangeListener<T>>	_listeners;

	public AbstractTrackable()
	{
		_listeners = new ArrayList<TrackableChangeListener<T>>(2);
	}

	protected void fireValueChanged()
	{
		T value = get();
		for (TrackableChangeListener<T> listener : _listeners)
			listener.onValueChanged(value);
	}

	@Override
	public void clearListeners()
	{
		_listeners.clear();
	}

	@Override
	public void addChangeListener(TrackableChangeListener<T> listener)
	{
		if (!_listeners.contains(listener))
			_listeners.add(listener);
	}

	@Override
	public void removeChangeListener(TrackableChangeListener<T> listener)
	{
		_listeners.remove(listener);
	}
}