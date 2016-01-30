package de.uni_hannover.sra.minimax_simulator.model.machine.shape;

import de.uni_hannover.sra.minimax_simulator.model.machine.part.Multiplexer;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Component;
import de.uni_hannover.sra.minimax_simulator.ui.layout.ComponentShape;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Dimension;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Insets;

/**
 * The shape of a multiplexer.
 *
 * @author Martin L&uuml;ck
 */
public class MuxShape implements ComponentShape {

    private static final int MUX_WIDTH          = 20;
    public static final int MUX_HEIGHT_PER_PIN  = 18;
    public static final int MUX_CORNER_SPACING  = 10;

    private static final int MUX_SPACING        = 15;

    private final Insets insets = new Insets(MUX_SPACING, MUX_SPACING, 0, 0);

    @Override
    public void updateShape(Component component) {
        Multiplexer mux = (Multiplexer) component;

        int height;

        int size = mux.getDataInputs().size();
        if (size > 0) {
            height = (size - 1) * MUX_HEIGHT_PER_PIN + MUX_CORNER_SPACING * 2;
        }
        else {
            height = MUX_CORNER_SPACING * 2;
        }

        mux.setDimension(new Dimension(MUX_WIDTH, height));
        mux.setInsets(insets);
    }

    @Override
    public void layout(Component component) {
        // there is no need for this method for a MuxShape
    }
}