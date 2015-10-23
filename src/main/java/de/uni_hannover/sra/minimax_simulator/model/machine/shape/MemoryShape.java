package de.uni_hannover.sra.minimax_simulator.model.machine.shape;

/**
 * The shape of the machine's memory.
 *
 * @author Martin L&uuml;ck
 */
public class MemoryShape extends FixedShape {

	private static final int MEM_WIDTH = 90;
	private static final int MEM_HEIGHT = 70;

	/**
	 * Initializes the {@code MemoryShape}.
	 */
	public MemoryShape() {
		super(MEM_WIDTH, MEM_HEIGHT);
	}
}