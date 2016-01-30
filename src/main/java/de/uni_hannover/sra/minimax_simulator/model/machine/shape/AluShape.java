package de.uni_hannover.sra.minimax_simulator.model.machine.shape;

/**
 * The shape of the ALU.
 *
 * @author Martin L&uuml;ck
 */
public class AluShape extends FixedShape {

    private static final int ALU_WIDTH  = 69;
    private static final int ALU_HEIGHT = 88;

    /**
     * Initializes the {@code AluShape}.
     */
    public AluShape() {
        super(ALU_WIDTH, ALU_HEIGHT);
    }
}