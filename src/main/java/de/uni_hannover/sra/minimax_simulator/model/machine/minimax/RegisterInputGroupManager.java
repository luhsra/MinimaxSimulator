package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.collect.Lists;

import de.uni_hannover.sra.minimax_simulator.layout.constraint.ConstraintBuilder;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.RegisterMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.MuxInputManager.InputEntry;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.AbstractGroup;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.DefaultLayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.IngoingPin;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Junction;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.OutgoingPin;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Wire;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;

class RegisterInputGroupManager extends RegisterManager implements MuxInputGroupManager
{
	private final MachineTopology						_circuitRegistry;
	private final GroupManager							_groupManager;

	private final SortedMap<MuxType, List<InputEntry>>	_inputEntriesByMux;
	private final Map<String, List<InputEntry>>			_inputsByRegisterId;

	public RegisterInputGroupManager(MinimaxMachine machine)
	{
		super(machine.getGroupManager());

		_groupManager = machine.getGroupManager();
		_circuitRegistry = machine.getTopology();

		_inputEntriesByMux = new TreeMap<MuxType, List<InputEntry>>();
		for (MuxType type : MuxType.values())
			_inputEntriesByMux.put(type, new ArrayList<InputEntry>());

		_inputsByRegisterId = new HashMap<String, List<InputEntry>>();
	}

	@Override
	public void update(MuxInputManager muxInputs)
	{
		// Replace inputs of given multiplexer
		{
			List<InputEntry> entries = _inputEntriesByMux.get(muxInputs.getMuxType());
			entries.clear();
			entries.addAll(muxInputs.getMuxInputs());
		}

		for (Entry<String, List<InputEntry>> registerInputs : _inputsByRegisterId.entrySet())
		{
			destroyGroups(registerInputs.getKey(), registerInputs.getValue());
		}

		// Rebuild input list per register
		_inputsByRegisterId.clear();
		for (List<InputEntry> entries : _inputEntriesByMux.values())
		{
			for (InputEntry entry : entries)
			{
				if (entry.input instanceof RegisterMuxInput)
				{
					RegisterMuxInput reg = (RegisterMuxInput) entry.input;

					String registerId = getRegisterId(reg);
					if (registerId == null)
						throw new IllegalStateException("No register with name " + reg.getRegisterName());

					List<InputEntry> regInputs = _inputsByRegisterId.get(registerId);
					if (regInputs == null)
					{
						regInputs = new ArrayList<InputEntry>();
						_inputsByRegisterId.put(registerId, regInputs);
					}
					regInputs.add(entry);
				}
			}
		}

		for (Entry<String, List<InputEntry>> registerInputs : _inputsByRegisterId.entrySet())
		{
			createGroups(registerInputs.getKey(), registerInputs.getValue());
		}
	}

	private void createGroups(String registerId, List<InputEntry> entries)
	{
		String sourceJunctionId = registerId + Parts._OUT_JUNCTION;
		Junction sourceJunction = _circuitRegistry.getCircuit(Junction.class,
			sourceJunctionId);

		for (InputEntry entry : Lists.reverse(entries))
		{
			RegisterInputGroup group = new RegisterInputGroup(entry, sourceJunction,
				sourceJunctionId);
			_groupManager.initializeGroup(entry.pinId + Parts._REGISTER, group);

			sourceJunction = group.junction;
			sourceJunctionId = group.sourceJunctionId;
		}
	}

	private void destroyGroups(String registerId, List<InputEntry> entries)
	{
		for (InputEntry entry : entries)
		{
			_groupManager.removeGroup(entry.pinId + Parts._REGISTER);
		}

		Junction registerJunction = _circuitRegistry.getCircuit(Junction.class,
			registerId + Parts._OUT_JUNCTION);
		registerJunction.getDataOuts().clear();
	}

	private class RegisterInputGroup extends AbstractGroup
	{
		public IngoingPin	pin;
		public String		pinId;
		public Junction		junction;
		public String		junctionId;
		public Junction		sourceJunction;
		public String		sourceJunctionId;

		public RegisterInputGroup(InputEntry entry, Junction sourceJunction,
				String sourceJunctionId)
		{
			pin = entry.pin;
			pinId = entry.pinId;
			junction = new Junction(1);
			junctionId = pinId + Parts._JUNCTION;
			this.sourceJunction = sourceJunction;
			this.sourceJunctionId = sourceJunctionId;
		}

		@Override
		public void initialize(MachineTopology cr, FontMetricsProvider fontProvider)
		{
			Wire wireOut = new Wire(2, junction.getDataOuts().get(0), pin);
			add(junction, junctionId);
			addWire(wireOut, junctionId + Parts._WIRE_DATA_OUT);

			OutgoingPin sourcePin = new OutgoingPin(sourceJunction);
			sourceJunction.getDataOuts().add(sourcePin);

			Wire wireIn = new Wire(2, sourcePin, junction.getDataIn());
			addWire(wireIn, junctionId + Parts._WIRE_DATA_IN);
		}

		@Override
		public boolean hasLayouts()
		{
			return true;
		}

		@Override
		public LayoutSet createLayouts()
		{
			String junction = pinId + Parts._JUNCTION;
			String wireOut = pinId + Parts._JUNCTION + Parts._WIRE_DATA_OUT;
			String wireIn = pinId + Parts._JUNCTION + Parts._WIRE_DATA_IN;

			ConstraintBuilder cb = new ConstraintBuilder();
			DefaultLayoutSet set = new DefaultLayoutSet();
			set.addLayout(junction,
				cb.alignVertically(pinId).alignHorizontally(sourceJunctionId));
			set.addLayout(wireOut + ".0", cb.align(junction));
			set.addLayout(wireOut + ".1", cb.align(pinId));
			set.addLayout(wireIn + ".0", cb.align(sourceJunctionId));
			set.addLayout(wireIn + ".1", cb.align(junction));

			return set;
		}
	}
}