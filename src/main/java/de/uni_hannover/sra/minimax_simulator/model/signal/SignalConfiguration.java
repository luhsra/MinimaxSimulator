package de.uni_hannover.sra.minimax_simulator.model.signal;

import java.util.List;

public interface SignalConfiguration
{
	// TODO: use immutable lists for other interfaces, too
	public List<SignalType> getSignalTypes();

	public void addSignalType(int index, SignalType signal);

	public void removeSignalType(int index);

	public void exchangeSignalsType(int index1, int index2);

	public void replaceSignalType(int index, SignalType signal);

	public void addSignalConfigListener(SignalConfigListener l);

	public void removeSignalConfigListener(SignalConfigListener l);
}