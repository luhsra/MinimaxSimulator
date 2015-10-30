package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;

import java.util.Set;

/**
 * Basic implementation of a simple component part.<br>
 * A {@code SimplePart} differs from a basic {@link Part} in having an {@link OutgoingPin} attached.
 *
 * @author Martin L&uuml;ck
 */
public abstract class SimplePart extends Part {

	private final OutgoingPin	_dataOut;

	/**
	 * Constructs a new {@code SimplePart}.
	 */
	public SimplePart() {
		_dataOut = new OutgoingPin(this);
	}

	/**
	 * Gets the {@link OutgoingPin}.
	 *
	 * @return
	 *          the output pin
	 */
	public OutgoingPin getDataOut() {
		return _dataOut;
	}

	@Override
	public Set<? extends Circuit> getSuccessors() {
		return _dataOut.getSuccessors();
	}

	@Override
	public void reset() {

	}
}