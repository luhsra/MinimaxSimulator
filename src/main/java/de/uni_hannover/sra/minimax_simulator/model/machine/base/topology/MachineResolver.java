package de.uni_hannover.sra.minimax_simulator.model.machine.base.topology;

import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import de.uni_hannover.sra.minimax_simulator.util.toposort.SimpleTopologicalSorter;
import de.uni_hannover.sra.minimax_simulator.util.toposort.TopologicalDependencyRelation;

public class MachineResolver
{
	private final static Logger _log = Logger.getLogger(MachineResolver.class.getName());

	private final ImmutableList<Circuit>			_resolveOrder;
	private final ImmutableList<SynchronousCircuit>	_synchronousCircuits;

	public MachineResolver(Set<Circuit> circuits)
	{
		ArrayList<Circuit> list = new ArrayList<Circuit>(circuits);
		new SimpleTopologicalSorter().sort(circuits,
			new TopologicalDependencyRelation<Circuit>()
			{
				@Override
				public boolean dependsOn(Circuit element, Circuit dependency)
				{
					return dependency.getSuccessors().contains(element);
				}
			}, list);

		_resolveOrder = ImmutableList.copyOf(list);
		_synchronousCircuits = ImmutableList.copyOf(Iterables.filter(circuits,
			SynchronousCircuit.class));
	}

	public void resolveCircuits()
	{
		for (Circuit circuit : _resolveOrder)
		{
			if (_log.isLoggable(Level.FINEST))
				_log.finest("Resolving " + circuit);

			circuit.update();

			if (_log.isLoggable(Level.FINEST))
				_log.finest("Resolved to " + circuit);
		}
	}

	public void nextCycle()
	{
		for (SynchronousCircuit circuit : _synchronousCircuits)
		{
			if (_log.isLoggable(Level.FINEST))
				_log.finest("Switching cycle in " + circuit);

			circuit.nextCycle();

			if (_log.isLoggable(Level.FINEST))
				_log.finest("Switched cycle in " + circuit);
		}
	}

	public void resetCircuits()
	{
		for (Circuit circuit : _resolveOrder)
		{
			circuit.reset();
		}
	}
}