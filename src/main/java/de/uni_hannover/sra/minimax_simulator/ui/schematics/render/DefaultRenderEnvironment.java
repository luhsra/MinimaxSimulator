package de.uni_hannover.sra.minimax_simulator.ui.schematics.render;

import com.sun.javafx.tk.FontMetrics;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Default {@link RenderEnvironment}.
 *
 * @author Martin L&uuml;ck
 */
public class DefaultRenderEnvironment implements RenderEnvironment {

	private final Font font;
	private final FontMetrics fontMetrics;

	/**
	 * Constructs a new {@code DefaultRenderEnvironment} with the specified {@link Font} and {@link FontMetrics}.
	 *
	 * @param font
	 *          the font to use
	 * @param fontMetrics
	 *          the font metrics of the font
	 */
	public DefaultRenderEnvironment(Font font, FontMetrics fontMetrics) {
		this.font = checkNotNull(font);
		this.fontMetrics = checkNotNull(fontMetrics);
	}

	/**
	 * Gets the background {@link Color}.
	 *
	 * @return
	 *          {@link Color#WHITE}
	 */
	@Override
	public Color getBackgroundColor() {
		return Color.WHITE;
	}

	/**
	 * Gets the foreground {@link Color}.
	 *
	 * @return
	 *          {@link Color#BLACK}
	 */
	@Override
	public Color getForegroundColor() {
		return Color.BLACK;
	}

	@Override
	public Font getFont() {
		return this.font;
	}

	@Override
	public FontMetrics getFontMetrics() {
		return this.fontMetrics;
	}
}