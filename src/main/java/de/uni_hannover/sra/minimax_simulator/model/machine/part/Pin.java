package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.ui.layout.PointComponent;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Basic implementation of a pin.
 *
 * @author Martin L&uuml;ck
 */
public abstract class Pin extends PointComponent {

	/** The {@link Part} related to the {@code Pin}. */
	private final Part _part;

	private int _value;

	/**
	 * Constructs a new {@code Pin} for the specified {@link Part}.
	 *
	 * @param part
	 *          the {@code Part} related to the {@code Pin}
	 */
	public Pin(Part part) {
		_part = checkNotNull(part);
	}

	/**
	 * Gets the {@link Part} related to the {@code Pin}.
	 *
	 * @return
	 *          the {@code Part}
	 */
	public Part getPart() {
		return _part;
	}

	/**
	 * Gets the value of the {@code Pin}.
	 *
	 * @return
	 *          the value
	 */
	public int getValue() {
		return _value;
	}

	/**
	 * Sets the value of the {@code Pin}.
	 *
	 * @param value
	 *          the new value
	 */
	public void setValue(int value) {
		_value = value;
	}
}