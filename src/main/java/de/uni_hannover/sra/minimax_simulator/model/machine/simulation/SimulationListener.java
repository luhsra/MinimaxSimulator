package de.uni_hannover.sra.minimax_simulator.model.machine.simulation;

/**
 * A {@code SimulationListener} is a class that needs to react to a change of the {@link Simulation}.
 *
 * @author Martin L&uuml;ck
 */
public interface SimulationListener {

	/**
	 * Notifies the listener about a change of the {@link SimulationState}.
	 *
	 * @param state
	 *          the new {@code SimulationState}
	 */
	public void stateChanged(SimulationState state);
}