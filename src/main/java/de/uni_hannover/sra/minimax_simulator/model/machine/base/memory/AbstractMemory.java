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

	private List<MemoryAccessListener>	_listeners	= new ArrayList<MemoryAccessListener>();

	private final int					_addressWidth;
	private final int					_minAddress;
	private final int					_maxAddress;

	private MemoryState					_memoryBackupState;
	private MemoryState					_memoryWorkState;

	private boolean						_doNotifyListeners;

	/**
	 * Constructs a new {@code AbstractMemory} with the specified address width.
	 *
	 * @param addressWidth
	 *          the width of the {@code AbstractMemory}
	 */
	public AbstractMemory(int addressWidth) {
		_addressWidth = addressWidth;
		_minAddress = 0;
		_maxAddress = (1 << addressWidth) - 1;

		_doNotifyListeners = true;
	}

	@Override
	public int getAddressWidth() {
		return _addressWidth;
	}

	@Override
	public int getMinAddress() {
		return _minAddress;
	}

	@Override
	public int getMaxAddress() {
		return _maxAddress;
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
		return _memoryWorkState;
	}

	@Override
	public void markMemoryState() {
		checkState(_memoryBackupState == null);
		_memoryBackupState = cloneState(_memoryWorkState);
	}

	@Override
	public void resetMemoryState() {
		if (_memoryBackupState != null) {
			_memoryWorkState = _memoryBackupState;
			_memoryBackupState = null;
			fireMemoryReset();
		}
	}

	/**
	 * Initializes the memory work state with a new instance of {@link MemoryState}.
	 */
	protected void setupMemoryState() {
		_memoryWorkState = createMemoryState();
	}

	@Override
	public void addMemoryAccessListener(MemoryAccessListener l) {
		if (!_listeners.contains(l)) {
			_listeners.add(l);
		}
	}

	@Override
	public void removeMemoryAccessListener(MemoryAccessListener l) {
		_listeners.remove(l);
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
		if (!_doNotifyListeners) {
			return;
		}

		for (MemoryAccessListener l : _listeners) {
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
		if (!_doNotifyListeners) {
			return;
		}

		for (MemoryAccessListener l : _listeners) {
			l.memoryWriteAccess(address, value);
		}
	}

	/**
	 * Notifies listeners about a memory reset if {@link #getNotifiesListeners()} returns true.
	 */
	protected void fireMemoryReset() {
		if (!_doNotifyListeners) {
			return;
		}

		for (MemoryAccessListener l : _listeners) {
			l.memoryReset();
		}
	}

	/**
	 * Notifies listeners about a memory change if {@link #getNotifiesListeners()} returns true.
	 */
	protected void fireMemoryChanged() {
		if (!_doNotifyListeners) {
			return;
		}

		for (MemoryAccessListener l : _listeners) {
			l.memoryChanged();
		}
	}

	@Override
	public boolean getNotifiesListeners() {
		return _doNotifyListeners;
	}

	@Override
	public void setNotifiesListeners(boolean notify) {
		boolean oldValue = _doNotifyListeners;
		_doNotifyListeners = notify;
		if (notify && !oldValue) {
			fireMemoryChanged();
		}
	}
}