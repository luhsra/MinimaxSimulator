package de.uni_hannover.sra.minimax_simulator.model.machine.shape;

public class AluShape extends FixedShape {
	//TODO: really public?
	public final static int	ALU_WIDTH	= 69;
	public final static int	ALU_HEIGHT	= 88;

	// public final static int ALU_UPPER_SPACING = 30;

	public AluShape() {
		super(ALU_WIDTH, ALU_HEIGHT);
	}
}