package de.uni_hannover.sra.minimax_simulator.model.machine.shape;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Register;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Bounds;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Component;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Dimension;

/**
 * The shape of a register component.
 *
 * @author Martin L&uuml;ck
 */
public class RegisterShape extends TextRenderShape {

	private static final int REGISTER_HEIGHT					= 20;
	private static final int REGISTER_MIN_WIDTH					= 90;
	private static final int REGISTER_SPACING					= 50;
	private static final int REGISTER_TEXT_LEFT_RIGHT_PADDING	= 5;

	/**
	 * Initializes the {@code RegisterShape}.
	 *
	 * @param fontProvider
	 *          the {@link FontMetricsProvider} used for font measuring
	 */
	public RegisterShape(FontMetricsProvider fontProvider) {
		super(fontProvider);
	}

	@Override
	public void updateShape(Component component) {
		Register reg = (Register) component;

		Dimension fontDim = getStringDimension(reg.getLabel());
		int width = Math.max(REGISTER_MIN_WIDTH, fontDim.w + REGISTER_TEXT_LEFT_RIGHT_PADDING);
		int height = Math.max(REGISTER_HEIGHT, fontDim.h);

		component.setDimension(new Dimension(width, height));
	}

	@Override
	public void layout(Component component) {
		Register reg = (Register) component;

		Bounds b = reg.getBounds();

		reg.getDataOut().setBounds(new Bounds(b.x, b.y + REGISTER_HEIGHT / 2, 0, 0));
		reg.getDataIn().setBounds(new Bounds(b.x + b.w, b.y + REGISTER_HEIGHT / 2, 0, 0));
		reg.getWriteEnabled().setBounds(new Bounds(b.x + b.w / 2 + 20, b.y, 0, 0));
	}
}