package de.uni_hannover.sra.minimax_simulator.model.machine.shape;

import de.uni_hannover.sra.minimax_simulator.layout.Component;
import de.uni_hannover.sra.minimax_simulator.layout.Dimension;
import de.uni_hannover.sra.minimax_simulator.layout.Insets;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Label;

public class CuLabelShape extends LabelShape {

	public CuLabelShape(FontMetricsProvider fontProvider) {
		super(fontProvider);
	}

	@Override
	public void updateShape(Component component) {
		Label label = (Label) component;
		Dimension textDim = getStringDimension(label.getMessage());

		Insets in = new Insets(15, 15, 20, 20);
		textDim = textDim.addInsets(in);
		component.setDimension(textDim);
	}
}