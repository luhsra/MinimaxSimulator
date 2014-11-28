package de.uni_hannover.sra.minimax_simulator.model.signal;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSignalTable implements SignalTable
{
	private final List<SignalTableListener>	_listeners;

	public AbstractSignalTable()
	{
		_listeners = new ArrayList<SignalTableListener>(2);
	}

	@Override
	public void addSignalTableListener(SignalTableListener l)
	{
		if (!_listeners.contains(l))
			_listeners.add(l);
	}

	@Override
	public void removeSignalTableListener(SignalTableListener l)
	{
		_listeners.remove(l);
	}

	protected void fireStructureChanged()
	{
		for (SignalTableListener l : _listeners)
			l.onStructureChanged();
	}

	protected void fireRowAdded(int index, SignalRow row)
	{
		for (SignalTableListener l : _listeners)
			l.onRowAdded(index, row);
	}

	protected void fireRowRemoved(int index)
	{
		for (SignalTableListener l : _listeners)
			l.onRowRemoved(index);
	}

	protected void fireRowReplaced(int index, SignalRow row)
	{
		for (SignalTableListener l : _listeners)
			l.onRowReplaced(index, row);
	}

	protected void fireRowsExchanged(int index1, int index2)
	{
		for (SignalTableListener l : _listeners)
			l.onRowsExchanged(index1, index2);
	}

	protected void fireRowsUpdated(int fromIndex, int toIndex)
	{
		for (SignalTableListener l : _listeners)
			l.onRowsUpdated(fromIndex, toIndex);
	}
}