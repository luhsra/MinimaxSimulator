package de.uni_hannover.sra.minimax_simulator.model.machine.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;

public class DefaultCircuitTopology implements MachineTopology
{
	private static class CircuitEntry
	{
		Class<? extends Circuit>	clazz;
		Circuit						circuit;

		CircuitEntry(Class<? extends Circuit> clazz, Circuit circuit)
		{
			this.clazz = clazz;
			this.circuit = circuit;
		}
	}

	//private Table<String, Class<? extends Circuit>, Circuit>	_circuitTable;

	private final Set<Circuit>									_allCircuits;
	private final Map<String, CircuitEntry>						_circuits;

	public DefaultCircuitTopology()
	{
		_allCircuits = new HashSet<Circuit>();
		_circuits = new HashMap<String, CircuitEntry>();

		//_circuitTable = HashBasedTable.create();
	}

	@Override
	public Set<Circuit> getAllCircuits()
	{
		//_circuitTable.values();
		return Collections.unmodifiableSet(_allCircuits);
	}

	@Override
	public <T extends Circuit> T getCircuit(Class<T> clazz, String id)
	{
		CircuitEntry entry = _circuits.get(id);
		if (entry == null)
			throw new NullPointerException("No circuit for the id " + id);

		if (clazz.isAssignableFrom(entry.clazz))
		{
			return clazz.cast(entry.circuit);
		}
		throw new ClassCastException(id + " is a " + entry.clazz.getName() + ", not a "
			+ clazz.getName());
	}

	@Override
	public <T extends Circuit> void addCircuit(Class<T> clazz, String id, T circuit)
	{
		CircuitEntry entry = _circuits.get(id);
		if (entry != null)
			throw new IllegalStateException("Circuit already defined as: "
				+ entry.circuit);

		_circuits.put(id, new CircuitEntry(clazz, circuit));
		_allCircuits.add(circuit);
		//fireCircuitAdded(circuit);
	}

	@Override
	public void addCircuit(String id, Circuit circuit)
	{
		CircuitEntry entry = _circuits.get(id);
		if (entry != null)
			throw new IllegalStateException("Circuit " + id + " already defined as: "
				+ entry.circuit);

		_circuits.put(id, new CircuitEntry(circuit.getClass(), circuit));
		_allCircuits.add(circuit);
		//fireCircuitAdded(circuit);
	}

	@Override
	public void removeCircuit(String id)
	{
		CircuitEntry entry = _circuits.remove(id);
		if (entry != null)
		{
			_allCircuits.remove(entry.circuit);
			//fireCircuitRemoved(entry.circuit);
		}
	}
}