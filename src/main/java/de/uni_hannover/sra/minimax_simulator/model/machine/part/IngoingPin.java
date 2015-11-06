package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import com.google.common.collect.ImmutableSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;

import java.util.Set;

/**
 * An {@code IngoingPin} is an input {@link Pin} of a {@link Part}.
 *
 * @author Martin L&uuml;ck
 */
public class IngoingPin extends Pin {

	private Wire wire;
	private Set<? extends Circuit> thePart;

	/**
	 * Constructs a new {@code IngoingPin} for the specified {@link Part}.
	 *
	 * @param part
	 *          the {@code Part} the {@code IngoingPin} goes in
	 */
	public IngoingPin(Part part) {
		super(part);
		thePart = ImmutableSet.of(part);
	}

	/**
	 * Gets the {@link Wire} connected to the {@code IngoingPin}.
	 *
	 * @return
	 *          the {@code Wire}
	 */
	public Wire getWire() {
		return wire;
	}

	/**
	 * Sets the {@link Wire} connected to the {@code IngoingPin}.
	 *
	 * @param wire
	 *          the {@code Wire}
	 */
	public void setWire(Wire wire) {
		this.wire = wire;
	}

	/**
	 * Reads the value at the {@code IngoingPin}.
	 *
	 * @return
	 *          the value of the {@code IngoingPin}
	 */
	public int read() {
		return getValue();
	}

	/**
	 * Gets the successors of the {@code IngoingPin}.<br>
	 * Obviously the successor of an {@code IngoingPin} is the {@link Part} it goes in.
	 *
	 * @return
	 *          a set of the successors of {@code IngoingPin}
	 */
	public Set<? extends Circuit> getSuccessors() {
		return thePart;
	}
}