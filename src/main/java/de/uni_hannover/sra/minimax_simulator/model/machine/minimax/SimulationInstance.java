package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;


import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.ControlPort;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.ResultPort;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineResolver;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Alu;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Port;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.ReadablePort;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Register;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.AbstractTrackable;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.Trackable;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;

class SimulationInstance
{
	private class AluResult extends AbstractTrackable<Integer>
	{
		private int			_lastPostedValue;
		private final Alu	_alu;

		public AluResult(Alu alu)
		{
			_lastPostedValue = Integer.valueOf(alu.getResult());
			_alu = alu;
		}

		@Override
		public Integer get()
		{
			return Integer.valueOf(_alu.getResult());
		}

		@Override
		public void set(Integer value)
		{
			throw new UnsupportedOperationException();
		}

		public void update()
		{
			if (_alu.getResult() == _lastPostedValue)
				return;
			_lastPostedValue = _alu.getResult();
			fireValueChanged();
		}
	}

	private class RegisterValue extends AbstractTrackable<Integer>
	{
		private int				_lastPostedValue;
		private final Register	_register;

		public RegisterValue(Register register)
		{
			_register = register;
			_lastPostedValue = Integer.valueOf(_register.getValue());
		}

		@Override
		public Integer get()
		{
			return _register.getValue();
		}

		@Override
		public void set(Integer value)
		{
			_register.setValue(value);
			fireValueChanged();
		}

		public void update()
		{
			if (_register.getValue() == _lastPostedValue)
				return;
			_lastPostedValue = _register.getValue();
			fireValueChanged();
		}
	}

	private final ResultPort					_aluCond;
	private final AluResult						_aluResult;
	private final Map<String, RegisterValue>	_registerValues;
	private final Map<String, ControlPort>		_registerPort;

	private final MachineResolver				_resolver;

	SimulationInstance(MinimaxMachine machine)
	{
		MachineTopology top = machine.getTopology();

		_aluResult = new AluResult(top.getCircuit(Alu.class, Parts.ALU));
		_aluCond = top.getCircuit(ReadablePort.class, Parts.ALU_COND_PORT);

		_registerValues = new HashMap<String, RegisterValue>();
		_registerPort = new HashMap<String, ControlPort>();
		Map<String, String> registerIdsByName = machine.getRegisterManager().getRegisterIdsByName();
		for (Entry<String, String> entry : registerIdsByName.entrySet())
		{
			Register register = top.getCircuit(Register.class, entry.getValue());
			_registerValues.put(entry.getKey(), new RegisterValue(register));
			_registerPort.put(entry.getKey() + ".W",
				top.getCircuit(Port.class, entry.getValue() + Parts._PORT));
		}

		Set<Circuit> circuits = top.getAllCircuits();

		_resolver = new MachineResolver(circuits);
	}

	Trackable<Integer> getAluResult()
	{
		return _aluResult;
	}

	Trackable<Integer> getRegisterValue(String name)
	{
		return _registerValues.get(name);
	}

	void reset()
	{
		_resolver.resetCircuits();
	}

	@Deprecated
	void step()
	{
		// Resolve ALU calculations
		_resolver.resolveCircuits();
		// Resolve register writings
		_resolver.nextCycle();
	}

	void resolve()
	{
		// Resolve ALU calculations
		_resolver.resolveCircuits();
	}

	void nextCycle()
	{
		// Resolve register writings
		_resolver.nextCycle();
	}

	void updateAluDisplay()
	{
		// broadcast value updates of parts
		_aluResult.update();
	}

	void updateRegisterDisplay()
	{
		for (RegisterValue register : _registerValues.values())
			register.update();
	}

	void updateAll()
	{
		updateAluDisplay();
		updateRegisterDisplay();
	}

	void setPortValues(SignalRow row)
	{
		for (BaseControlPort port : BaseControlPort.values())
		{
			int value = row.getSignalValue(port.name());
			port.port().write(value);
		}
		for (Entry<String, ControlPort> entry : _registerPort.entrySet())
		{
			int writeEnabled = row.getSignalValue(entry.getKey());
			entry.getValue().write(writeEnabled);
		}
	}

	int getCond()
	{
		return _aluCond.read();
	}
}