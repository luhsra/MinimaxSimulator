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

	/**
	 * Initializes the {@code FixedShape} with the specified {@link Dimension} and {@link Insets}.
	 *
	 * @param dim
	 *          the {@code Dimension} of the shape
	 * @param insets
	 *          the {@code Insets} of the shape
	 */
	public FixedShape(Dimension dim, Insets insets) {
		_dimension = dim;
		_insets = insets;
	}

	/**
	 * Initializes the {@code FixedShape} with the specified {@link Dimension}.
	 *
	 * @param dim
	 *          the {@code Dimension} of the shape
	 */
	public FixedShape(Dimension dim) {
		this(dim, new Insets(0, 0, 0, 0));
	}

	/**
	 * Initializes the {@code FixedShape} with the specified width and height.
	 *
	 * @param w
	 *          the width of the shape
	 * @param h
	 *          the height of the shape
	 */
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