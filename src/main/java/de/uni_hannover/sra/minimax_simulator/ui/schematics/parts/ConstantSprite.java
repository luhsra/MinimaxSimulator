package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import de.uni_hannover.sra.minimax_simulator.ui.layout.Bounds;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Constant;
import de.uni_hannover.sra.minimax_simulator.ui.render.RenderEnvironment;
import javafx.scene.canvas.GraphicsContext;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The sprite of a {@link Constant} multiplexer input.
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public class ConstantSprite extends CircuitSprite {

	private final Constant constant;

	/**
	 * Initializes the {@code ConstantSprite}.
	 *
	 * @param constant
	 *          the {@code Constant} this sprite will represent
	 */
	public ConstantSprite(Constant constant) {
		this.constant = checkNotNull(constant);
	}

	@Override
	public void paint(GraphicsContext gc, RenderEnvironment env) {
		Bounds b = constant.getBounds();
		debugBounds(gc, b);

		gc.fillText(constant.getConstantStr(), b.x, b.y + b.h);
	}
}