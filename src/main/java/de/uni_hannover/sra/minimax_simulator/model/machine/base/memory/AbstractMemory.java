package de.uni_hannover.sra.minimax_simulator.model.machine.base.memory;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;

public abstract class AbstractMemory implements MachineMemory
{
	private List<MemoryAccessListener>	_listeners	= new ArrayList<MemoryAccessListener>();

	private final int					_addressWidth;
	private final int					_minAddress;
	private final int					_maxAddress;

	private MemoryState					_memoryBackupState;
	private MemoryState					_memoryWorkState;

	private boolean						_doNotifyListeners;

	public AbstractMemory(int addressWidth)
	{
		_addressWidth = addressWidth;
		_minAddress = 0;
		_maxAddress = (1 << addressWidth) - 1;

		_doNotifyListeners = true;
	}

	@Override
	public int getAddressWidth()
	{
		return _addressWidth;
	}

	@Override
	public int getMinAddress()
	{
		return _minAddress;
	}

	@Override
	public int getMaxAddress()
	{
		return _maxAddress;
	}

	protected abstract MemoryState createMemoryState();

	protected abstract MemoryState cloneState(MemoryState state);

	@Override
	public MemoryState getMemoryState()
	{
		return _memoryWorkState;
	}

	@Override
	public void markMemoryState()
	{
		checkState(_memoryBackupState == null);
		_memoryBackupState = cloneState(_memoryWorkState);
	}

	@Override
	public void resetMemoryState()
	{
		if (_memoryBackupState != null)
		{
			_memoryWorkState = _memoryBackupState;
			_memoryBackupState = null;
			fireMemoryReset();
		}
	}

	protected void setupMemoryState()
	{
		_memoryWorkState = createMemoryState();
	}

	@Override
	public void addMemoryAccessListener(MemoryAccessListener l)
	{
		if (!_listeners.contains(l))
			_listeners.add(l);
	}

	@Override
	public void removeMemoryAccessListener(MemoryAccessListener l)
	{
		_listeners.remove(l);
	}

	protected void fireReadAccess(int address, int value)
	{
		if (!_doNotifyListeners)
			return;

		for (MemoryAccessListener l : _listeners)
			l.memoryReadAccess(address, value);
	}

	protected void fireWriteAccess(int address, int value)
	{
		if (!_doNotifyListeners)
			return;

		for (MemoryAccessListener l : _listeners)
			l.memoryWriteAccess(address, value);
	}

	protected void fireMemoryReset()
	{
		if (!_doNotifyListeners)
			return;

		for (MemoryAccessListener l : _listeners)
			l.memoryReset();
	}

	protected void fireMemoryChanged()
	{
		if (!_doNotifyListeners)
			return;

		for (MemoryAccessListener l : _listeners)
			l.memoryChanged();
	}

	@Override
	public boolean getNotifiesListeners()
	{
		return _doNotifyListeners;
	}

	@Override
	public void setNotifiesListeners(boolean notify)
	{
		boolean oldValue = _doNotifyListeners;
		_doNotifyListeners = notify;
		if (notify && !oldValue)
			fireMemoryChanged();
	}
}