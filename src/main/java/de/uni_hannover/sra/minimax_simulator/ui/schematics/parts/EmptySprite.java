package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import java.awt.Graphics2D;

import de.uni_hannover.sra.minimax_simulator.ui.render.RenderEnvironment;
import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;
import javafx.scene.canvas.GraphicsContext;

/**
 * An empty {@link Sprite}.
 *
 * @author Martin L&uuml;ck
 */
public class EmptySprite implements Sprite {

	@Override
	@Deprecated
	public void paint(Graphics2D g, RenderEnvironment env) {

	}

	@Override
	public void paint(GraphicsContext gc, RenderEnvironment env) {

	}
}