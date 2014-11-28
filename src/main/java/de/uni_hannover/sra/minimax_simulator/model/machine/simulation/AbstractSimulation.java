package de.uni_hannover.sra.minimax_simulator.model.machine.simulation;

import static com.google.common.base.Preconditions.*;

import java.util.ArrayList;

import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListener;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalConfigListener;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTableListener;

public abstract class AbstractSimulation implements Simulation, MachineConfigListener,
		SignalTableListener, SignalConfigListener
{
	private final ArrayList<SimulationListener>	_listeners;

	private SimulationState						_state;
	private int									_cycleCount;
	private boolean								_paused;
	private boolean								_halted;

	public AbstractSimulation()
	{
		_cycleCount = -1;

		_paused = false;
		_halted = false;

		_listeners = new ArrayList<SimulationListener>();
		_state = SimulationState.OFF;
	}

	private void checkIdleState()
	{
		checkState(_state == SimulationState.IDLE, "Simulation must be started");
	}

	private void checkNotHalted()
	{
		checkState(!_halted, "Simulation already halted");
	}

	@Override
	public void reset()
	{
		checkIdleState();

		resetImpl();

		_state = SimulationState.IDLE;
		_halted = false;
		_paused = false;

		fireStateChanged();
	}

	protected abstract void resetImpl();

	@Override
	public void init()
	{
		checkState(_state == SimulationState.OFF);

		initImpl();

		_state = SimulationState.IDLE;
		_halted = false;
		_paused = false;

		fireStateChanged();
	}

	protected abstract void initImpl();

	@Override
	public void stop()
	{
		checkIdleState();

		stopImpl();

		_state = SimulationState.OFF;
		_halted = false;
		_paused = false;

		fireStateChanged();
	}

	protected abstract void stopImpl();

	@Override
	public void step()
	{
		checkIdleState();
		checkNotHalted();

		_state = SimulationState.RUNNING;
		fireStateChanged();

		stepImpl();

		_state = SimulationState.IDLE;
		fireStateChanged();
	}

	protected abstract void stepImpl();

	@Override
	public void run()
	{
		checkIdleState();
		checkNotHalted();

		_state = SimulationState.RUNNING;
		fireStateChanged();

		runImpl();

		_state = SimulationState.IDLE;
		fireStateChanged();
	}

	protected abstract void runImpl();

	protected void halt()
	{
		checkState(!_halted, "Already halted");

		// The program ended
		_halted = true;
	}

	@Override
	public void pause()
	{
		_paused = true;
	}

	protected boolean paused()
	{
		if (!_paused)
			return false;
		_paused = false;
		return true;
	}

	protected void fireStateChanged()
	{
		for (SimulationListener listener : _listeners)
			listener.stateChanged(_state);
	}

	@Override
	public void addSimulationListener(SimulationListener listener)
	{
		if (!_listeners.contains(listener))
			_listeners.add(listener);
	}

	@Override
	public void removeSimulationListener(SimulationListener listener)
	{
		_listeners.remove(listener);
	}

	@Override
	public SimulationState getState()
	{
		return _state;
	}

	protected void resetCycles()
	{
		_cycleCount = 0;
	}

	protected void incrementCycles()
	{
		_cycleCount++;
		if (_cycleCount == Integer.MAX_VALUE)
			halt();
	}

	@Override
	public int getCyclesCount()
	{
		return _cycleCount;
	}

	@Override
	public boolean isHalted()
	{
		return _halted;
	}

	private void stopIfRunning()
	{
		checkState(_state != SimulationState.RUNNING, "machine modified while running");

		if (_state != SimulationState.OFF)
			stop();
	}

	@Override
	public void processEvent(MachineConfigEvent event)
	{
		stopIfRunning();
	}

	@Override
	public void onStructureChanged()
	{
		stopIfRunning();
	}

	@Override
	public void onRowAdded(int index, SignalRow row)
	{
		stopIfRunning();
	}

	@Override
	public void onRowRemoved(int index)
	{
		stopIfRunning();
	}

	@Override
	public void onRowsExchanged(int index1, int index2)
	{
		stopIfRunning();
	}

	@Override
	public void onRowReplaced(int index, SignalRow row)
	{
		stopIfRunning();
	}

	@Override
	public void onRowsUpdated(int fromIndex, int toIndex)
	{
		stopIfRunning();
	}

	@Override
	public void signalStructureChanged()
	{
		stopIfRunning();
	}
}