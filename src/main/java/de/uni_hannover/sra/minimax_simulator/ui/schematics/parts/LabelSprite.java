package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import de.uni_hannover.sra.minimax_simulator.model.machine.part.Label;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Bounds;
import javafx.scene.canvas.GraphicsContext;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The sprite of a {@link Label}.
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public class LabelSprite extends CircuitSprite {

    private final Label label;

    /**
     * Initializes the {@code LabelSprite}.
     *
     * @param label
     *          the {@code Label} this sprite will represent
     */
    public LabelSprite(Label label) {
        this.label = checkNotNull(label);
    }

    @Override
    public void paint(GraphicsContext gc) {
        Bounds b = label.getBounds();
        debugBounds(gc, b);
        gc.fillText(label.getMessage(), b.x, b.y + b.h);
    }
}