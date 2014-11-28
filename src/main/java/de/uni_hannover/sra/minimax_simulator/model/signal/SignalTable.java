package de.uni_hannover.sra.minimax_simulator.model.signal;

import com.google.common.collect.ImmutableList;

import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;

public interface SignalTable
{
	public int getRowCount();

	public SignalRow getRow(int index);

	public ImmutableList<SignalRow> getRows();

	public void addSignalTableListener(SignalTableListener l);

	public void removeSignalTableListener(SignalTableListener l);

	// Row list mutators
	public void addSignalRow(SignalRow row);

	public void addSignalRow(int index, SignalRow row);

	public void removeSignalRow(int index);

	public void exchangeSignalRows(int index1, int index2);

	public void moveSignalRows(int firstIndex, int lastIndex, int direction);

	public void setSignalRow(int index, SignalRow row);

	// Breaks running simulation
	public void setRowSignal(int index, String signal, SignalValue value);
	public void setRowJump(int index, Jump jump);
}