package de.uni_hannover.sra.minimax_simulator.model.machine.shape;

import de.uni_hannover.sra.minimax_simulator.ui.layout.Bounds;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Component;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Dimension;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.SignExtension;

/**
 * The shape of the {@link SignExtension}.
 *
 * @author Martin L&uuml;ck
 */
public class SignExtShape extends TextRenderShape {

	/**
	 * Initializes the {@code SignExtShape}.
	 *
	 * @param fontProvider
	 *          the {@link FontMetricsProvider} used for font measuring
	 */
	public SignExtShape(FontMetricsProvider fontProvider) {
		super(fontProvider);
	}

	@Override
	public void updateShape(Component component) {
		SignExtension s = (SignExtension) component;

		Dimension dim = getStringDimension(s.getLabel());
		s.setDimension(new Dimension(dim.w + 10, dim.h * 2));
	}

	@Override
	public void layout(Component component) {
		SignExtension s = (SignExtension) component;

		Bounds b = s.getBounds();

		s.getDataOut().setBounds(new Bounds(b.x + b.w / 2, b.y, 0, 0));
		s.getDataIn().setBounds(new Bounds(b.x + b.w / 2, b.y + b.h, 0, 0));
	}
}