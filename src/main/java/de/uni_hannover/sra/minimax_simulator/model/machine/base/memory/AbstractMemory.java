package de.uni_hannover.sra.minimax_simulator.model.machine.base.memory;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;

/**
 * Basic implementation of {@link MachineMemory}.
 *
 * @author Martin L&uuml;ck
 */
public abstract class AbstractMemory implements MachineMemory {

	private List<MemoryAccessListener> listeners = new ArrayList<MemoryAccessListener>();

	private final int addressWidth;
	private final int minAddress;
	private final int maxAddress;

	private MemoryState memoryBackupState;
	private MemoryState memoryWorkState;

	private boolean doNotifyListeners;

	/**
	 * Constructs a new {@code AbstractMemory} with the specified address width.
	 *
	 * @param addressWidth
	 *          the width of the {@code AbstractMemory}
	 */
	public AbstractMemory(int addressWidth) {
		this.addressWidth = addressWidth;
		minAddress = 0;
		maxAddress = (1 << addressWidth) - 1;

		doNotifyListeners = true;
	}

	@Override
	public int getAddressWidth() {
		return addressWidth;
	}

	@Override
	public int getMinAddress() {
		return minAddress;
	}

	@Override
	public int getMaxAddress() {
		return maxAddress;
	}

	/**
	 * Creates a new {@link MemoryState}.
	 *
	 * @return
	 *          the created {@code MemoryState}
	 */
	protected abstract MemoryState createMemoryState();

	/**
	 * Clones the specified {@link MemoryState}.
	 *
	 * @param state
	 *          the {@code MemoryState} to clone
	 * @return
	 *          the clone
	 */
	protected abstract MemoryState cloneState(MemoryState state);

	@Override
	public MemoryState getMemoryState() {
		return memoryWorkState;
	}

	@Override
	public void markMemoryState() {
		checkState(memoryBackupState == null);
		memoryBackupState = cloneState(memoryWorkState);
	}

	@Override
	public void resetMemoryState() {
		if (memoryBackupState != null) {
			memoryWorkState = memoryBackupState;
			memoryBackupState = null;
			fireMemoryReset();
		}
	}

	/**
	 * Initializes the memory work state with a new instance of {@link MemoryState}.
	 */
	protected void setupMemoryState() {
		memoryWorkState = createMemoryState();
	}

	@Override
	public void addMemoryAccessListener(MemoryAccessListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	@Override
	public void removeMemoryAccessListener(MemoryAccessListener l) {
		listeners.remove(l);
	}

	/**
	 * Notifies listeners about a read access at the specified address if
	 * {@link #getNotifiesListeners()} returns true.
	 *
	 * @param address
	 *          the accessed address
	 * @param value
	 *          the read value
	 */
	protected void fireReadAccess(int address, int value) {
		if (!doNotifyListeners) {
			return;
		}

		for (MemoryAccessListener l : listeners) {
			l.memoryReadAccess(address, value);
		}
	}

	/**
	 * Notifies listeners about a write access at the specified address if
	 * {@link #getNotifiesListeners()} returns true.
	 *
	 * @param address
	 *          the accessed address
	 * @param value
	 *          the written value
	 */
	protected void fireWriteAccess(int address, int value) {
		if (!doNotifyListeners) {
			return;
		}

		for (MemoryAccessListener l : listeners) {
			l.memoryWriteAccess(address, value);
		}
	}

	/**
	 * Notifies listeners about a memory reset if {@link #getNotifiesListeners()} returns true.
	 */
	protected void fireMemoryReset() {
		if (!doNotifyListeners) {
			return;
		}

		for (MemoryAccessListener l : listeners) {
			l.memoryReset();
		}
	}

	/**
	 * Notifies listeners about a memory change if {@link #getNotifiesListeners()} returns true.
	 */
	protected void fireMemoryChanged() {
		if (!doNotifyListeners) {
			return;
		}

		for (MemoryAccessListener l : listeners) {
			l.memoryChanged();
		}
	}

	@Override
	public boolean getNotifiesListeners() {
		return doNotifyListeners;
	}

	@Override
	public void setNotifiesListeners(boolean notify) {
		boolean oldValue = doNotifyListeners;
		doNotifyListeners = notify;
		if (notify && !oldValue) {
			fireMemoryChanged();
		}
	}
}