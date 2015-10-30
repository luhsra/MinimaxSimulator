package de.uni_hannover.sra.minimax_simulator.model.signal;

/**
 * The {@code BinarySignalType} is a {@link SignalType} whose signal value can only be {@code 0} or {@code 1}.
 *
 * @author Martin L&uuml;ck
 */
public class BinarySignalType extends DefaultSignalType {

	/**
	 * Constructs a new {@code BinarySignalType} with the specified ID, name and {@code allows null} property.
	 *
	 * @param id
	 *          the ID of the signal
	 * @param name
	 *          the name of the signal
	 * @param allowsNull
	 *          whether the signal allows null or not
	 */
	public BinarySignalType(String id, String name, boolean allowsNull) {
		super(id, name, 2, allowsNull);
	}
}