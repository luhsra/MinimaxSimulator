package de.uni_hannover.sra.minimax_simulator.util;

import java.util.ArrayList;
import java.util.List;

/**
 * This class bundles methods for listeners in a single place.
 * <br>
 * The class is meant to be sub-classed.
 * 
 * @author Martin
 *
 * @param <L> the type of the listener instances
 */
public class ListenerContainer<L>
{
	private List<L> _listeners = new ArrayList<L>(2);

	/**
	 * Adds <code>listener</code> to the internal list if it is not already added. 
	 * 
	 * @param listener the new listener to register at this instance
	 */
	public void addListener(L listener)
	{
		if (!_listeners.contains(listener))
			_listeners.add(listener);
	}

	/**
	 * Removes <code>listener</code> from the internal list if it is added. 
	 * 
	 * @param listener the new listener to unregister from this instance
	 */
	public void removeListener(L listener)
	{
		_listeners.remove(listener);
	}

	protected List<L> getListeners()
	{
		return _listeners;
	}
}