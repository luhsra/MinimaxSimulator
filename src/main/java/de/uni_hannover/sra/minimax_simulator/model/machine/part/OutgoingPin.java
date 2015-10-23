package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;

import java.util.HashSet;
import java.util.Set;

/**
 * An {@code OutgoingPin} is an output {@link Pin} of a {@link Part}.
 *
 * @author Martin L&uuml;ck
 */
public class OutgoingPin extends Pin {

	private final Set<Wire> _wires;

	/**
	 * Constructs a new {@code OutgoingPin} for the specified {@link Part}.
	 *
	 * @param part
	 *          the {@code Part} the {@code OutgoingPin} comes from
	 */
	public OutgoingPin(Part part) {
		super(part);
		_wires = new HashSet<Wire>();
	}

	/**
	 * Writes the value of the {@code OutgoingPin}.
	 *
	 * @param value
	 *          the value of the {@code OutgoingPin}
	 */
	public void write(int value) {
		setValue(value);
	}

	/**
	 * Gets the successors of the {@code OutgoingPin}.<br>
	 * Obviously the successors of an {@code OutgoingPin} are the {@link Wire}s
	 * it is connected with.
	 *
	 * @return
	 *          a set of the successors of the {@code OutgoingPin}
	 */
	public Set<? extends Circuit> getSuccessors() {
		return _wires;
	}

	/**
	 * Gets the {@link Wire}s the {@code OutgoingPin} is connected with.
	 *
	 * @return
	 *          a set of {@code Wire}s the {@code OutgoingPin} is connected with
	 */
	public Set<Wire> getWires() {
		return _wires;
	}
}