package de.uni_hannover.sra.minimax_simulator.ui.render;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

import java.awt.Graphics2D;

public interface Sprite {

	@Deprecated
	public void paint(Graphics2D g, RenderEnvironment env);

	public void paint(GraphicsContext gc, RenderEnvironment env);

	default public void changeColor(GraphicsContext gc, Paint paint) {
		gc.setStroke(paint);
		gc.setFill(paint);
	}
}