package de.uni_hannover.sra.minimax_simulator.model.machine.shape;

import de.uni_hannover.sra.minimax_simulator.ui.layout.Component;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Label;

public class LabelShape extends TextRenderShape {

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

	}
}