package de.uni_hannover.sra.minimax_simulator.model.machine.base;

/**
 * The {@code ControlPort} interface represents the control port of a component part.
 *
 * @author Martin L&uuml;ck
 */
public interface ControlPort {

	/**
	 * Writes the specified value.
	 *
	 * @param value
	 *          the value to write
	 */
	public void write(int value);
}