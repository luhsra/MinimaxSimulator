package de.uni_hannover.sra.minimax_simulator.ui.schematics.render;

import javafx.scene.canvas.GraphicsContext;

/**
 * A {@code Sprite} is a simple graphical representation of a machine's component.
 *
 * @author Martin L&uuml;ck
 */
public interface Sprite {

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