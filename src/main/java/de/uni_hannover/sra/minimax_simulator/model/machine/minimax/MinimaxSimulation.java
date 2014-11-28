package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import static com.google.common.base.Preconditions.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MemoryAccessListener;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.AbstractSimulation;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.Trackable;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;

public class MinimaxSimulation extends AbstractSimulation implements MemoryAccessListener
{
	private final static Logger		_log	= Logger.getLogger(MinimaxSimulation.class.getName());

	private final MinimaxMachine	_machine;
	private final SignalTable		_signalTable;

	private int						_currentSignalRow;
	private SimulationInstance		_currentInstance;

	private boolean					_resolvedRead;

	private final static int		CYCLE_YIELD_BITMASK = 0x00000100;

	public MinimaxSimulation(MinimaxMachine machine, SignalTable table)
	{
		_machine = machine;
		_signalTable = table;
		_currentSignalRow = -1;
		_resolvedRead = false;

		if (_signalTable.getRowCount() == 0)
			halt();

		_machine.getMemory().addMemoryAccessListener(this);
		_signalTable.addSignalTableListener(this);
	}

	@Override
	public Trackable<Integer> getAluResult()
	{
		checkState(_currentInstance != null);
		return _currentInstance.getAluResult();
	}

	@Override
	public Trackable<Integer> getRegisterValue(String name)
	{
		checkState(_currentInstance != null);
		return _currentInstance.getRegisterValue(name);
	}

	@Override
	public MachineMemory getMemoryState()
	{
		return _machine.getMemory();
	}

	@Override
	protected void resetImpl()
	{
		_currentInstance.reset();
		_currentSignalRow = 0;
		_resolvedRead = false;
		resetCycles();

		_machine.getMemory().resetMemoryState();
		_machine.getMemory().markMemoryState();
		_currentInstance.updateAll();
	}

	@Override
	protected void initImpl()
	{
		_log.log(Level.FINE, "Starting simulation.");

		_currentInstance = new SimulationInstance(_machine);
		_currentInstance.reset();
		_currentSignalRow = 0;
		_resolvedRead = false;
		resetCycles();

		_machine.getMemory().markMemoryState();
		_currentInstance.updateAll();
	}

	@Override
	protected void stopImpl()
	{
		_log.log(Level.FINE, "Stopping simulation.");

		_currentSignalRow = -1;
		_currentInstance.reset();
		_currentInstance = null;
		_resolvedRead = false;

		_machine.getMemory().resetMemoryState();
	}

	private void doStep(boolean postUpdates)
	{
		SignalRow row = _signalTable.getRow(_currentSignalRow);

		if (!_resolvedRead)
		{
			if (_log.isLoggable(Level.FINE))
				_log.log(Level.FINE,
					"Executing signal row " + _currentSignalRow + ": " + row.toString());

			_currentInstance.setPortValues(row);
			_currentInstance.resolve();
			if (postUpdates)
			{
				_currentInstance.updateAluDisplay();
			}

			_resolvedRead = true;
		}
		else
		{
			_currentInstance.nextCycle();
			if (postUpdates)
			{
				_currentInstance.updateRegisterDisplay();
			}
			incrementCycles();

			_currentSignalRow = row.getJump().getTargetRow(_currentSignalRow, _currentInstance.getCond());

			if (_currentSignalRow >= _signalTable.getRowCount())
			{
				halt();
			}
			else if (_signalTable.getRow(_currentSignalRow).isBreakpoint())
			{
				pause();
			}

			_resolvedRead = false;
		}
	}

	@Override
	protected void stepImpl()
	{
		doStep(true);
	}

	@Override
	protected void runImpl()
	{
		boolean memoryNotify = _machine.getMemory().getNotifiesListeners();
		try
		{
			int i = 0;
			_machine.getMemory().setNotifiesListeners(false);
			while (!isHalted() && !paused())
			{
				if ((i++ & CYCLE_YIELD_BITMASK) == 1)
					Thread.yield();

				doStep(false);
			}
			_currentInstance.updateAll();	
		}
		finally
		{
			_machine.getMemory().setNotifiesListeners(memoryNotify);
		}
	}

	@Override
	public int getCurrentSignalRow()
	{
		return _currentSignalRow;
	}

	@Override
	public void memoryReadAccess(int address, int value)
	{
		
	}

	@Override
	public void memoryWriteAccess(int address, int value)
	{
		
	}

	@Override
	public void memoryReset()
	{
		// Not interested
	}

	@Override
	public void memoryChanged()
	{
		// Not interested
	}

	@Override
	public boolean isResolved()
	{
		return _resolvedRead;
	}
}