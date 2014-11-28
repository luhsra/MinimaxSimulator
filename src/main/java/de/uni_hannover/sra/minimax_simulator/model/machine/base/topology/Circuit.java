package de.uni_hannover.sra.minimax_simulator.model.machine.base.topology;

import java.util.Set;

/**
 * The Circuit interface represents a single piece of hardware.<br><br>
 * 
 * It has two methods: {@link #update()}, which tells the instance to refresh its internal state
 * (possibly based on topological predecessors in the network), and {@link #getSuccessors()},
 * which returns a list of topological successors, i.e. the pieces of hardware which may change its
 * internal state too as a consequence of this object changing its state.
 * 
 * @author Martin
 *
 */
public interface Circuit
{
	/**
	 * Refresh the internal state of the instance depending on the values of its
	 * predecessors.
	 */
	public void update();

	/**
	 * Returns the direct topological successors of this instance.
	 * <br>
	 * The Set is not modifiable.
	 * 
	 * @return the set of successor circuits
	 */
	public Set<? extends Circuit> getSuccessors();

	/**
	 * Resets the internal state of this instance to a default state.
	 * 
	 */
	public void reset();
}