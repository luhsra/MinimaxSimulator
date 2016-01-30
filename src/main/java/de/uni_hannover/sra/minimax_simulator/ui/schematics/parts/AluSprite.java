package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Alu;
import javafx.scene.canvas.GraphicsContext;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The ALU sprite.
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public class AluSprite extends CircuitSprite {

    private static final double[][] POINTS = new double[][] {
            // x      y
            { -9.5,   0.5},
            {-34.5,  12.5},
            {-34.5,  44.5},
            { 34.5,  10.5},
            { 34.5, -10.5},
            {-34.5, -44.5},
            {-34.5, -12.5},
            { -9.5,   0.5}
    };

    private static final String NAME = "ALU";
    private static final String PIN_A = "A";
    private static final String PIN_B = "B";

    private final Alu alu;

    /**
     * Initializes the {@code AluSprite}.
     *
     * @param alu
     *          the {@link Alu} this sprite will represent
     */
    public AluSprite(Alu alu) {
        this.alu = checkNotNull(alu);
    }

    @Override
    public void paint(GraphicsContext gc) {
        debugBounds(gc, alu.getBounds());

        double xCenter = alu.getBounds().x + alu.getBounds().w / 2.0;
        double yCenter = alu.getBounds().y + alu.getBounds().h / 2.0;

        gc.save();
        gc.setLineWidth(1);
        for (int i = 1; i < POINTS.length; i++) {
            gc.strokeLine(POINTS[i - 1][0] + xCenter, POINTS[i - 1][1] + yCenter, POINTS[i][0] + xCenter, POINTS[i][1] + yCenter);
        }
        gc.restore();

        FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(gc.getFont());

        double xTextAlu = xCenter - fm.computeStringWidth(NAME) / 2 + 16;
        double yTextAlu = yCenter + fm.getLineHeight() / 4;

        double xTextA = xCenter - fm.computeStringWidth(PIN_A) / 2 - 25;
        double yTextA = yCenter + fm.getLineHeight() / 4 - 23;

        double xTextB = xTextA;
        double yTextB = yCenter + fm.getLineHeight() / 4 + 23;

        gc.fillText(NAME, xTextAlu, yTextAlu);
        gc.fillText(PIN_A, xTextA, yTextA);
        gc.fillText(PIN_B, xTextB, yTextB);

        debugPin(gc, alu.getInA());
        debugPin(gc, alu.getInB());
        debugPin(gc, alu.getInCtrl());
        debugPin(gc, alu.getOutData());
        debugPin(gc, alu.getOutZero());
    }

}