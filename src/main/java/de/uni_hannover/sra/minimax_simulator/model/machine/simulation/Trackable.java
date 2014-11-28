package de.uni_hannover.sra.minimax_simulator.model.machine.simulation;

public interface Trackable<T>
{
	public T get();

	public void set(T value);

	public void addChangeListener(TrackableChangeListener<T> listener);

	public void removeChangeListener(TrackableChangeListener<T> listener);

	public void clearListeners();
}