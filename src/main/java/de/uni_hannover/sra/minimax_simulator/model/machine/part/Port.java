package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.ControlPort;

/**
 * Implementation of a {@link ControlPort} as component part of a machine.
 *
 * @author Martin L&uuml;ck
 */
public class Port extends SimplePart implements ControlPort {

	private int value;

	/**
	 * Constructs a new {@code Port} with the specified name.
	 *
	 * @param name
	 *          the name of the {@code Port}
	 */
	public Port(String name) {
		setName(name);
	}

	/**
	 * Gets the ID of the {@code Port}.
	 *
	 * @return
	 *          the ID of the {@code Port}
	 */
	public String getPortId() {
		return getName();
	}

	@Override
	public void write(int value) {
		this.value = value;
	}

	@Override
	public void update() {
		getDataOut().write(value);
	}

	@Override
	public void reset() {
		value = 0;
	}

	@Override
	public String toString() {
		return "Port[name=" + getName() + ", value=" + value + "]";
	}
}