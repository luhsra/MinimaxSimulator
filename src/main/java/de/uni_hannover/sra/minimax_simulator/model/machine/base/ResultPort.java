package de.uni_hannover.sra.minimax_simulator.model.machine.base;

/**
 * The {@code ResultPort} interface represents the result / output port of a component part.
 *
 * @author Martin L&uuml;ck
 */
public interface ResultPort {

	/**
	 * Reads the value of the component part.
	 *
	 * @return
	 *          the read value of the component part
	 */
	public int read();
}