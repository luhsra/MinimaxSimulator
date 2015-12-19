package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MemoryAccessListener;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.AbstractSimulation;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.Traceable;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkState;

/**
 * The {@code MinimaxSimulation} is the simulation of the microprogram of a {@link MinimaxMachine}.
 *
 * @author Martin L&uuml;ck
 */
public class MinimaxSimulation extends AbstractSimulation implements MemoryAccessListener {

	private static final Logger LOG = Logger.getLogger(MinimaxSimulation.class.getName());

	private final MinimaxMachine machine;
	private final SignalTable signalTable;

	private int currentSignalRow;
	private SimulationInstance currentInstance;

	private boolean resolvedRead;

	private static final int CYCLE_YIELD_BITMASK = 0x00000100;

	/**
	 * Constructs a new instance of the {@code MinimaxSimulation} with the specified {@link MinimaxMachine}
	 * and {@link SignalTable}.
	 *
	 * @param machine
	 *          the {@code MinimaxMachine} that will be simulated
	 * @param table
	 *          the {@code SignalTable} containing the microprogram that will be simulated
	 */
	public MinimaxSimulation(MinimaxMachine machine, SignalTable table) {
		this.machine = machine;
		signalTable = table;
		currentSignalRow = -1;
		resolvedRead = false;

		if (signalTable.getRowCount() == 0) {
			halt();
		}

		this.machine.getMemory().addMemoryAccessListener(this);
		signalTable.addSignalTableListener(this);
	}

	@Override
	public Traceable<Integer> getAluResult() {
		checkState(currentInstance != null);
		return currentInstance.getAluResult();
	}

	@Override
	public Traceable<Integer> getRegisterValue(String name) {
		checkState(currentInstance != null);
		return currentInstance.getRegisterValue(name);
	}

	@Override
	public MachineMemory getMemoryState() {
		return machine.getMemory();
	}

	@Override
	protected void resetImpl() {
		currentInstance.reset();
		currentSignalRow = 0;
		resolvedRead = false;
		resetCycles();

		machine.getMemory().resetMemoryState();
		machine.getMemory().markMemoryState();
		currentInstance.updateAll();
	}

	@Override
	protected void initImpl() {
		LOG.log(Level.FINE, "Starting simulation.");

		currentInstance = new SimulationInstance(machine);
		currentInstance.reset();
		currentSignalRow = 0;
		resolvedRead = false;
		resetCycles();

		machine.getMemory().markMemoryState();
		currentInstance.updateAll();
	}

	@Override
	protected void stopImpl() {
		LOG.log(Level.FINE, "Stopping simulation.");

		currentSignalRow = -1;
		currentInstance.reset();
		currentInstance = null;
		resolvedRead = false;

		machine.getMemory().resetMemoryState();
	}

	/**
	 * Simulates the next cycle of the machine.
	 *
	 * @param postUpdates
	 *          whether updates will be posted or not
	 */
	private void doStep(boolean postUpdates) {
		SignalRow row = signalTable.getRow(currentSignalRow);

		if (!resolvedRead) {
			if (LOG.isLoggable(Level.FINE)) {
				LOG.log(Level.FINE, "Executing signal row " + currentSignalRow + ": " + row.toString());
			}

			currentInstance.setPortValues(row);
			currentInstance.resolve();
			if (postUpdates) {
				currentInstance.updateAluDisplay();
			}

			resolvedRead = true;
		}
		else {
			currentInstance.nextCycle();
			if (postUpdates) {
				currentInstance.updateRegisterDisplay();
			}
			incrementCycles();

			currentSignalRow = row.getJump().getTargetRow(currentSignalRow, currentInstance.getCond());

			if (currentSignalRow >= signalTable.getRowCount()) {
				halt();
			}
			else if (signalTable.getRow(currentSignalRow).isBreakpoint()) {
				pause();
			}

			resolvedRead = false;
		}
	}

	@Override
	protected void stepImpl() {
		doStep(true);
	}

	@Override
	protected void runImpl() {
		boolean memoryNotify = machine.getMemory().getNotifiesListeners();
		try {
			int i = 0;
			machine.getMemory().setNotifiesListeners(false);
			while (!isHalted() && !paused()) {
				if ((i++ & CYCLE_YIELD_BITMASK) == 1) {
					Thread.yield();
				}

				doStep(false);
			}
			currentInstance.updateAll();
		} finally {
			machine.getMemory().setNotifiesListeners(memoryNotify);
		}
	}

	@Override
	public int getCurrentSignalRow() {
		return currentSignalRow;
	}

	@Override
	public void memoryReadAccess(int address, int value) {
		
	}

	@Override
	public void memoryWriteAccess(int address, int value) {
		
	}

	@Override
	public void memoryReset() {
		// not interested
	}

	@Override
	public void memoryChanged() {
		// not interested
	}

	@Override
	public boolean isResolved() {
		return resolvedRead;
	}
}