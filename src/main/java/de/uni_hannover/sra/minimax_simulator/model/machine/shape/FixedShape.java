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

	private final Dimension _dimension;
	private final Insets _insets;

	public FixedShape(Dimension dim, Insets insets) {
		_dimension = dim;
		_insets = insets;
	}

	public FixedShape(Dimension dim) {
		this(dim, new Insets(0, 0, 0, 0));
	}

	public FixedShape(int w, int h) {
		this(new Dimension(w, h));
	}

	@Override
	public void updateShape(Component component) {
		component.setDimension(_dimension);
		component.setInsets(_insets);
	}

	@Override
	public void layout(Component component) {

	}
}