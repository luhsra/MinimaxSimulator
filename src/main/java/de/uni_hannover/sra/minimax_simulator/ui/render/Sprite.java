package de.uni_hannover.sra.minimax_simulator.ui.render;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

import java.awt.Graphics2D;

/**
 * A {@code Sprite} is a simple graphical representation of a {@link de.uni_hannover.sra.minimax_simulator.model.machine.base.Machine}'s component.
 *
 * @author Martin L&uuml;ck
 */
public interface Sprite {
	@Deprecated
	public void paint(Graphics2D g, RenderEnvironment env);

	/**
	 * Draws the {@code Sprite} on a {@link GraphicsContext} of a {@link javafx.scene.canvas.Canvas}
	 * using a {@link RenderEnvironment}.
	 *
	 * @param gc
	 *          the {@code GraphicsContext} the {@code Sprite} will be drawn on
	 * @param env
	 *          the {@code RenderEnvironment} used for rendering
	 */
	public void paint(GraphicsContext gc, RenderEnvironment env);
}