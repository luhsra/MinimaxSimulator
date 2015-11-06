package de.uni_hannover.sra.minimax_simulator.model.machine.shape;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Constant;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Component;

/**
 * The shape for a constant (multiplexer input).
 *
 * @author Martin L&uuml;ck
 */
public class ConstantShape extends TextRenderShape {

	/**
	 * Initializes the {@code ConstantShape}.
	 *
	 * @param fontProvider
	 *          the {@link FontMetricsProvider} used for font measuring
	 */
	public ConstantShape(FontMetricsProvider fontProvider) {
		super(fontProvider);
	}

	@Override
	public void updateShape(Component component) {
		Constant constant = (Constant) component;

		// likely to be cached
		component.setDimension(getStringDimension(constant.getConstantStr()));
	}

	@Override
	public void layout(Component component) {

	}
}