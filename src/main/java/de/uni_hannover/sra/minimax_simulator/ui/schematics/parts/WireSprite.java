package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import de.uni_hannover.sra.minimax_simulator.model.machine.part.Junction;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Wire;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Point;
import javafx.scene.canvas.GraphicsContext;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The sprite for a {@link Wire}.
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public class WireSprite extends CircuitSprite {

    private final Wire wire;

    /**
     * Initializes the {@code WireSprite}.
     *
     * @param wire
     *          the {@code Wire} this sprite will represent
     */
    public WireSprite(Wire wire) {
        this.wire = checkNotNull(wire);
    }

    @Override
    public void paint(GraphicsContext gc) {
        Point[] points = wire.getPoints();

        double lineWidth = gc.getLineWidth();
        gc.setLineWidth(1.0);
        int i = 1;
        for (; i < points.length; i++) {
            gc.strokeLine(points[i-1].x + 0.5, points[i-1].y + 0.5, points[i].x + 0.5, points[i].y + 0.5);
        }
        gc.setLineWidth(lineWidth);

        if (!(wire.getDrain() instanceof  Junction)) {
            drawArrow(gc, points[i-2], points[i-1]);
        }
    }
}