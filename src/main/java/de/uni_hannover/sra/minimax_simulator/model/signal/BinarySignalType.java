package de.uni_hannover.sra.minimax_simulator.model.signal;

public class BinarySignalType extends DefaultSignalType
{
	public BinarySignalType(String id, String name, boolean allowsNull)
	{
		super(id, name, 2, allowsNull);
	}
}