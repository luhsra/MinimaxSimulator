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
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.AbstractTraceable;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.Traceable;
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
	private class AluResult extends AbstractTraceable<Integer> {
		private int lastPostedValue;
		private final Alu alu;

		/**
		 * Constructs a new instance of the {@code AluResult} for the specified {@link Alu}.
		 *
		 * @param alu
		 *          the {@code Alu} of the machine to simulate
		 */
		public AluResult(Alu alu) {
			lastPostedValue = Integer.valueOf(alu.getResult());
			this.alu = alu;
		}

		@Override
		public Integer get() {
			return Integer.valueOf(alu.getResult());
		}

		@Override
		public void set(Integer value) {
			throw new UnsupportedOperationException();
		}

		/**
		 * Updates the value of the {@code AluResult} instance.
		 */
		public void update() {
			if (alu.getResult() == lastPostedValue) {
				return;
			}
			lastPostedValue = alu.getResult();
			fireValueChanged();
		}
	}

	/**
	 * Represents the value of a register.
	 */
	private class RegisterValue extends AbstractTraceable<Integer> {

		private int lastPostedValue;
		private final Register register;

		/**
		 * Constructs a new {@code RegisterValue} for the specified {@link Register}.
		 *
		 * @param register
		 *          the {@code Register} of the machine to simulate
		 */
		public RegisterValue(Register register) {
			this.register = register;
			lastPostedValue = Integer.valueOf(this.register.getValue());
		}

		@Override
		public Integer get() {
			return register.getValue();
		}

		@Override
		public void set(Integer value) {
			register.setValue(value);
			fireValueChanged();
		}

		/**
		 * Updates the value of the {@code RegisterValue} instance.
		 */
		public void update() {
			if (register.getValue() == lastPostedValue) {
				return;
			}
			lastPostedValue = register.getValue();
			fireValueChanged();
		}
	}

	private final ResultPort aluCond;
	private final AluResult aluResult;
	private final Map<String, RegisterValue> registerValues;
	private final Map<String, ControlPort> registerPort;

	private final MachineResolver resolver;

	/**
	 * Constructs a new instance of the {@code SimulationInstance} for simulation of the specified
	 * {@link MinimaxMachine}.
	 *
	 * @param machine
	 *          the machine to simulate
	 */
	SimulationInstance(MinimaxMachine machine) {
		MachineTopology top = machine.getTopology();

		aluResult = new AluResult(top.getCircuit(Alu.class, Parts.ALU));
		aluCond = top.getCircuit(ReadablePort.class, Parts.ALU_COND_PORT);

		registerValues = new HashMap<String, RegisterValue>();
		registerPort = new HashMap<String, ControlPort>();
		Map<String, String> registerIdsByName = machine.getRegisterManager().getRegisterIdsByName();
		for (Entry<String, String> entry : registerIdsByName.entrySet())
		{
			Register register = top.getCircuit(Register.class, entry.getValue());
			registerValues.put(entry.getKey(), new RegisterValue(register));
			registerPort.put(entry.getKey() + ".W",
					top.getCircuit(Port.class, entry.getValue() + Parts._PORT));
		}

		Set<Circuit> circuits = top.getAllCircuits();

		resolver = new MachineResolver(circuits);
	}

	/**
	 * Gets the ALU result.
	 *
	 * @return
	 *          the ALU result
	 */
	Traceable<Integer> getAluResult() {
		return aluResult;
	}

	/**
	 * Gets the value of the specified register.
	 *
	 * @param name
	 *          the name of the register
	 * @return
	 *          the value of the register
	 */
	Traceable<Integer> getRegisterValue(String name) {
		return registerValues.get(name);
	}

	/**
	 * Resets the simulation.
	 */
	void reset() {
		resolver.resetCircuits();
	}

	/**
	 * Resolves the ALU calculations.
	 */
	void resolve() {
		resolver.resolveCircuits();
	}

	/**
	 * Resolves register writings.
	 */
	void nextCycle() {
		resolver.nextCycle();
	}

	/**
	 * Broadcasts value updates of parts.
	 */
	void updateAluDisplay() {
		aluResult.update();
	}

	/**
	 * Triggers all instances of {@link RegisterValue} to update their value.
	 */
	void updateRegisterDisplay() {
		for (RegisterValue register : registerValues.values()) {
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
		for (Entry<String, ControlPort> entry : registerPort.entrySet()) {
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
		return aluCond.read();
	}
}