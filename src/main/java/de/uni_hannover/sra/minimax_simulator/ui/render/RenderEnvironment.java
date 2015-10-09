package de.uni_hannover.sra.minimax_simulator.ui.render;

import javafx.scene.canvas.GraphicsContext;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

/**
 * A {@code RenderEnvironment} is used for some basic rendering related methods.
 *
 * @author Martin L&uuml;ck
 */
public interface RenderEnvironment {

	@Deprecated
	public Graphics2D createGraphics(Graphics2D g);

	@Deprecated
	public Color getBackgroundColor();

	@Deprecated
	public Color getForegroundColor();

	@Deprecated
	public Font getFont();

	@Deprecated
	public FontMetrics getFontMetrics();

	/**
	 * Gets the background {@link javafx.scene.paint.Color}.
	 *
	 * @return
	 *          the {@code Color} of the background
	 */
	public javafx.scene.paint.Color getBackgroundColorFX();

	/**
	 * Gets the foreground {@link javafx.scene.paint.Color}.
	 *
	 * @return
	 *          the {@code Color} of the foreground
	 */
	public javafx.scene.paint.Color getForegroundColorFX();

	/**
	 * Gets the currently used {@link javafx.scene.text.Font}.
	 *
	 * @return
	 *          the currently used {@code Font}
	 */
	public javafx.scene.text.Font getFontFX();

	/**
	 * Gets the {@link com.sun.javafx.tk.FontMetrics} of the {@code RenderEnvironment}.
	 *
	 * @return
	 *          the {@code FontMetrics} of the {@code RenderEnvironment}.
	 */
	public com.sun.javafx.tk.FontMetrics getFontMetricsFX();
}