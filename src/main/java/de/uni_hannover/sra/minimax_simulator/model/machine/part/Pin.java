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
	private final Part part;

	private int value;

	/**
	 * Constructs a new {@code Pin} for the specified {@link Part}.
	 *
	 * @param part
	 *          the {@code Part} related to the {@code Pin}
	 */
	public Pin(Part part) {
		this.part = checkNotNull(part);
	}

	/**
	 * Gets the {@link Part} related to the {@code Pin}.
	 *
	 * @return
	 *          the {@code Part}
	 */
	public Part getPart() {
		return part;
	}

	/**
	 * Gets the value of the {@code Pin}.
	 *
	 * @return
	 *          the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Sets the value of the {@code Pin}.
	 *
	 * @param value
	 *          the new value
	 */
	public void setValue(int value) {
		this.value = value;
	}
}