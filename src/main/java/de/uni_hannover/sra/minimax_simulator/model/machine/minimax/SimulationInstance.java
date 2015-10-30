package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;


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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * The instance of the {@link MinimaxSimulation}.
 *
 * @author Martin L&uuml;ck
 */
class SimulationInstance {

	/**
	 * Represents the the result of the ALU.
	 */
	private class AluResult extends AbstractTrackable<Integer> {
		private int			_lastPostedValue;
		private final Alu	_alu;

		/**
		 * Constructs a new instance of the {@code AluResult} for the specified {@link Alu}.
		 *
		 * @param alu
		 *          the {@code Alu} of the machine to simulate
		 */
		public AluResult(Alu alu) {
			_lastPostedValue = Integer.valueOf(alu.getResult());
			_alu = alu;
		}

		@Override
		public Integer get() {
			return Integer.valueOf(_alu.getResult());
		}

		@Override
		public void set(Integer value) {
			throw new UnsupportedOperationException();
		}

		/**
		 * Updates the value of the {@code AluResult} instance.
		 */
		public void update() {
			if (_alu.getResult() == _lastPostedValue) {
				return;
			}
			_lastPostedValue = _alu.getResult();
			fireValueChanged();
		}
	}

	/**
	 * Represents the value of a register.
	 */
	private class RegisterValue extends AbstractTrackable<Integer> {

		private int				_lastPostedValue;
		private final Register	_register;

		/**
		 * Constructs a new {@code RegisterValue} for the specified {@link Register}.
		 *
		 * @param register
		 *          the {@code Register} of the machine to simulate
		 */
		public RegisterValue(Register register) {
			_register = register;
			_lastPostedValue = Integer.valueOf(_register.getValue());
		}

		@Override
		public Integer get() {
			return _register.getValue();
		}

		@Override
		public void set(Integer value) {
			_register.setValue(value);
			fireValueChanged();
		}

		/**
		 * Updates the value of the {@code RegisterValue} instance.
		 */
		public void update() {
			if (_register.getValue() == _lastPostedValue) {
				return;
			}
			_lastPostedValue = _register.getValue();
			fireValueChanged();
		}
	}

	private final ResultPort					_aluCond;
	private final AluResult						_aluResult;
	private final Map<String, RegisterValue>	_registerValues;
	private final Map<String, ControlPort>		_registerPort;

	private final MachineResolver				_resolver;

	/**
	 * Constructs a new instance of the {@code SimulationInstance} for simulation of the specified
	 * {@link MinimaxMachine}.
	 *
	 * @param machine
	 *          the machine to simulate
	 */
	SimulationInstance(MinimaxMachine machine) {
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

	/**
	 * Gets the ALU result.
	 *
	 * @return
	 *          the ALU result
	 */
	Trackable<Integer> getAluResult() {
		return _aluResult;
	}

	/**
	 * Gets the value of the specified register.
	 *
	 * @param name
	 *          the name of the register
	 * @return
	 *          the value of the register
	 */
	Trackable<Integer> getRegisterValue(String name) {
		return _registerValues.get(name);
	}

	/**
	 * Resets the simulation.
	 */
	void reset() {
		_resolver.resetCircuits();
	}

	/**
	 * Resolves the ALU calculations.
	 */
	void resolve() {
		_resolver.resolveCircuits();
	}

	/**
	 * Resolves register writings.
	 */
	void nextCycle() {
		_resolver.nextCycle();
	}

	/**
	 * Broadcasts value updates of parts.
	 */
	void updateAluDisplay() {
		_aluResult.update();
	}

	/**
	 * Triggers all instances of {@link RegisterValue} to update their value.
	 */
	void updateRegisterDisplay() {
		for (RegisterValue register : _registerValues.values()) {
			register.update();
		}
	}

	/**
	 * Triggers the {@link RegisterValue} instances and the {@link AluResult} instance to update their values.
	 */
	void updateAll() {
		updateAluDisplay();
		updateRegisterDisplay();
	}

	/**
	 * Sets the values of the different ports according to the specified {@link SignalRow}.
	 *
	 * @param row
	 *          the {@code SignalRow}
	 */
	void setPortValues(SignalRow row) {
		for (BaseControlPort port : BaseControlPort.values()) {
			int value = row.getSignalValue(port.name());
			port.port().write(value);
		}
		for (Entry<String, ControlPort> entry : _registerPort.entrySet()) {
			int writeEnabled = row.getSignalValue(entry.getKey());
			entry.getValue().write(writeEnabled);
		}
	}

	/**
	 * Gets the ALU condition.
	 *
	 * @return
	 *          {@code 1} if the ALU result is {@code 0}, {@code 0} otherwise
	 */
	int getCond() {
		return _aluCond.read();
	}
}