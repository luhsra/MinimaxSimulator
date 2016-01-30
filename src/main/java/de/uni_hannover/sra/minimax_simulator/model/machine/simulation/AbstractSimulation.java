package de.uni_hannover.sra.minimax_simulator.model.machine.simulation;

import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListener;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalConfigListener;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTableListener;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkState;

/**
 * Basic implementation of {@link Simulation} link with {@link MachineConfigListener},
 * {@link SignalTableListener} and {@link SignalConfigListener}.
 *
 * @author Martin L&uuml;ck
 */
public abstract class AbstractSimulation implements Simulation, MachineConfigListener, SignalTableListener, SignalConfigListener {

    private final ArrayList<SimulationListener> listeners;

    private SimulationState state;
    private int cycleCount;
    private boolean paused;
    private boolean halted;

    /**
     * Constructs a new {code AbstractSimulation} instance.
     */
    protected AbstractSimulation() {
        cycleCount = -1;

        paused = false;
        halted = false;

        listeners = new ArrayList<>();
        state = SimulationState.OFF;
    }

    /**
     * Checks if the simulation is in state {@link SimulationState#IDLE}.
     */
    private void checkIdleState() {
        checkState(state == SimulationState.IDLE, "Simulation must be started");
    }

    /**
     * Checks if the simulation is not in state {@link SimulationState#HALTED}.
     */
    private void checkNotHalted() {
        checkState(!halted, "Simulation already halted");
    }

    @Override
    public void reset() {
        checkIdleState();

        resetImpl();

        state = SimulationState.IDLE;
        halted = false;
        paused = false;

        fireStateChanged();
    }

    /**
     * Implementation dependant reset behaviour.
     */
    protected abstract void resetImpl();

    @Override
    public void init() {
        checkState(state == SimulationState.OFF);

        initImpl();

        state = SimulationState.IDLE;
        halted = false;
        paused = false;

        fireStateChanged();
    }

    /**
     * Implementation dependant initialization behaviour.
     */
    protected abstract void initImpl();

    @Override
    public void stop() {
        checkIdleState();

        stopImpl();

        state = SimulationState.OFF;
        halted = false;
        paused = false;

        fireStateChanged();
    }

    /**
     * Implementation dependant stop behavior.
     */
    protected abstract void stopImpl();

    @Override
    public void step() {
        checkIdleState();
        checkNotHalted();

        state = SimulationState.RUNNING;
        fireStateChanged();

        stepImpl();

        state = SimulationState.IDLE;
        fireStateChanged();
    }

    /**
     * Implementation dependant step behavior.
     */
    protected abstract void stepImpl();

    @Override
    public void run() {
        checkIdleState();
        checkNotHalted();

        state = SimulationState.RUNNING;
        fireStateChanged();

        runImpl();

        state = SimulationState.IDLE;
        fireStateChanged();
    }

    /**
     * Implementation dependant run behavior.
     */
    protected abstract void runImpl();

    protected void halt() {
        checkState(!halted, "Already halted");

        state = SimulationState.HALTED;
        fireStateChanged();

        // the program ended
        halted = true;
    }

    @Override
    public void pause() {
        paused = true;
    }

    /**
     * Gets the value of the {@code paused} property.<br>
     * <br>
     * If {@code true} it toggles the value.
     *
     * @return
     *          {@code true} if the simulation is paused, {@code false} otherwise
     */
    protected boolean paused() {
        if (!paused) {
            return false;
        }
        else {
            paused = false;
            return true;
        }
    }

    /**
     * Notifies the {@link SimulationListener}s about a change of the {@link SimulationState}.
     */
    protected void fireStateChanged() {
        for (SimulationListener listener : listeners) {
            listener.stateChanged(state);
        }
    }

    @Override
    public void addSimulationListener(SimulationListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeSimulationListener(SimulationListener listener) {
        listeners.remove(listener);
    }

    @Override
    public SimulationState getState() {
        return state;
    }

    /**
     * Resets the cycle count to zero.
     */
    protected void resetCycles() {
        cycleCount = 0;
    }

    /**
     * Increments the cycle count and halts the simulation if the cycle count reaches
     * {@link Integer#MAX_VALUE}.
     */
    protected void incrementCycles() {
        cycleCount++;
        if (cycleCount == Integer.MAX_VALUE) {
            halt();
        }
    }

    @Override
    public int getCyclesCount() {
        return cycleCount;
    }

    @Override
    public boolean isHalted() {
        return halted;
    }

    /**
     * Stops the simulation if it is in state {@link SimulationState#RUNNING}.
     */
    private void stopIfRunning() {
        checkState(state != SimulationState.RUNNING, "machine modified while running");

        if (state != SimulationState.OFF) {
            stop();
        }
    }

    @Override
    public void processEvent(MachineConfigEvent event) {
        stopIfRunning();
    }

    @Override
    public void onStructureChanged() {
        stopIfRunning();
    }

    @Override
    public void onRowAdded(int index, SignalRow row) {
        stopIfRunning();
    }

    @Override
    public void onRowRemoved(int index) {
        stopIfRunning();
    }

    @Override
    public void onRowsExchanged(int index1, int index2) {
        stopIfRunning();
    }

    @Override
    public void onRowReplaced(int index, SignalRow row) {
        stopIfRunning();
    }

    @Override
    public void onRowsUpdated(int fromIndex, int toIndex) {
        stopIfRunning();
    }

    @Override
    public void signalStructureChanged() {
        stopIfRunning();
    }
}