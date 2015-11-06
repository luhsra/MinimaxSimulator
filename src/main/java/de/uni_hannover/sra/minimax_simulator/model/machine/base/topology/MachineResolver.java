package de.uni_hannover.sra.minimax_simulator.model.machine.base.topology;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import de.uni_hannover.sra.minimax_simulator.util.toposort.SimpleTopologicalSorter;
import de.uni_hannover.sra.minimax_simulator.util.toposort.TopologicalDependencyRelation;

import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Resolves the {@link MachineTopology} using a {@link SimpleTopologicalSorter}.
 *
 * @author Martin L&uuml;ck
 */
public class MachineResolver {

	private final static Logger LOG = Logger.getLogger(MachineResolver.class.getName());

	private final ImmutableList<Circuit> resolveOrder;
	private final ImmutableList<SynchronousCircuit> synchronousCircuits;

	/**
	 * Constructs a new {@code MachineResolver} with the specified set of {@code Circuits}.
	 *
	 * @param circuits
	 *          the {@code Circuit}s to resolve
	 */
	public MachineResolver(Set<Circuit> circuits) {
		ArrayList<Circuit> list = new ArrayList<Circuit>(circuits);
		new SimpleTopologicalSorter().sort(circuits, new TopologicalDependencyRelation<Circuit>() {
				@Override
				public boolean dependsOn(Circuit element, Circuit dependency) {
					return dependency.getSuccessors().contains(element);
				}
			}, list);

		resolveOrder = ImmutableList.copyOf(list);
		synchronousCircuits = ImmutableList.copyOf(Iterables.filter(circuits, SynchronousCircuit.class));
	}

	/**
	 * Resolves the {@link Circuit}s.
	 */
	public void resolveCircuits() {
		for (Circuit circuit : resolveOrder) {
			if (LOG.isLoggable(Level.FINEST)) {
				LOG.finest("Resolving " + circuit);
			}

			circuit.update();

			if (LOG.isLoggable(Level.FINEST)) {
				LOG.finest("Resolved to " + circuit);
			}
		}
	}

	/**
	 * Switches the cycles in {@link SynchronousCircuit}s.
	 */
	public void nextCycle() {
		for (SynchronousCircuit circuit : synchronousCircuits) {
			if (LOG.isLoggable(Level.FINEST)) {
				LOG.finest("Switching cycle in " + circuit);
			}

			circuit.nextCycle();

			if (LOG.isLoggable(Level.FINEST)) {
				LOG.finest("Switched cycle in " + circuit);
			}
		}
	}

	/**
	 * Resets the {@link Circuit}s to their default state.
	 */
	public void resetCircuits() {
		resolveOrder.forEach(Circuit::reset);
	}
}