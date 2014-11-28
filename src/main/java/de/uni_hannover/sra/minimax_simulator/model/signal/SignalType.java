package de.uni_hannover.sra.minimax_simulator.model.signal;

import java.util.List;

public interface SignalType
{
	public String getId();

	public String getName();

	public String getSignalName(int index);

	public String getSignalName(SignalValue value);

	public SignalValue getDefault();

	public List<SignalValue> getValues();

	public int getBitWidth();
}