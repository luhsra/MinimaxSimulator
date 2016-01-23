package de.uni_hannover.sra.minimax_simulator.model.machine.shape;

import de.uni_hannover.sra.minimax_simulator.ui.layout.Component;
import de.uni_hannover.sra.minimax_simulator.ui.layout.ComponentShape;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Dimension;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Insets;

/**
 * A {@link ComponentShape} with a fixed size.
 *
 * @author Martin L&uuml;ck
 */
public class FixedShape implements ComponentShape {

    private final Dimension dimension;
    private final Insets insets;

    /**
     * Initializes the {@code FixedShape} with the specified {@link Dimension} and {@link Insets}.
     *
     * @param dimension
     *          the {@code Dimension} of the shape
     * @param insets
     *          the {@code Insets} of the shape
     */
    public FixedShape(Dimension dimension, Insets insets) {
        this.dimension = dimension;
        this.insets = insets;
    }

    /**
     * Initializes the {@code FixedShape} with the specified {@link Dimension}.
     *
     * @param dimension
     *          the {@code Dimension} of the shape
     */
    public FixedShape(Dimension dimension) {
        this(dimension, new Insets(0, 0, 0, 0));
    }

    /**
     * Initializes the {@code FixedShape} with the specified width and height.
     *
     * @param width
     *          the width of the shape
     * @param height
     *          the height of the shape
     */
    public FixedShape(int width, int height) {
        this(new Dimension(width, height));
    }

    @Override
    public void updateShape(Component component) {
        component.setDimension(dimension);
        component.setInsets(insets);
    }

    @Override
    public void layout(Component component) {
        // there is no need for this method for a FixedShape
    }
}