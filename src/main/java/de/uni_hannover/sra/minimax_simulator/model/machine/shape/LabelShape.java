package de.uni_hannover.sra.minimax_simulator.model.machine.shape;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Label;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Component;

/**
 * A shape for {@link Label}.
 *
 * @author Martin L&uuml;ck
 */
public class LabelShape extends TextRenderShape {

    /**
     * Initializes the {@code LabelShape}.
     *
     * @param fontProvider
     *          the {@link FontMetricsProvider} used for font measuring
     */
    public LabelShape(FontMetricsProvider fontProvider) {
        super(fontProvider);
    }

    @Override
    public void updateShape(Component component) {
        Label label = (Label) component;
        component.setDimension(getStringDimension(label.getMessage()));
    }

    @Override
    public void layout(Component component) {
        // there is no need for this method for a LabelShape
    }
}