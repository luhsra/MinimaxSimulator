package de.uni_hannover.sra.minimax_simulator.model.signal;

public interface SignalTableListener
{
	public void onStructureChanged();

	public void onRowAdded(int index, SignalRow row);

	public void onRowRemoved(int index);

	public void onRowsExchanged(int index1, int index2);

	public void onRowReplaced(int index, SignalRow row);

	public void onRowsUpdated(int fromIndex, int toIndex);
}