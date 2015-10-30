package de.uni_hannover.sra.minimax_simulator.ui.render;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * A {@link RenderEnvironment} for font measuring.
 *
 * @author Martin L&uuml;ck
 */
public class DummyRenderEnvironment implements RenderEnvironment {

	private final Font font;
	private final FontMetrics fontMetrics;

	/**
	 * Constructs a new {@code DummyRenderEnvironment} for font measuring with the font
	 * "SansSerif" and size 17.
	 */
	public DummyRenderEnvironment() {
		font = new Font("SansSerif", 17);
		fontMetrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(font);
	}

	/**
	 * Gets the background {@link Color}.<br>
	 * <b>Caution:</b> Throws {@link UnsupportedOperationException}.
	 *
	 * @return
	 *          the {@code Color} of the background
	 * @throws UnsupportedOperationException
	 *          thrown because {@code DummyRenderEnvironment} is for font measuring only
	 */
	@Override
	public Color getBackgroundColor() {
		throw new UnsupportedOperationException("This is a dummy render environment for font measuring");
	}

	/**
	 * Gets the foreground {@link Color}.<br>
	 * <b>Caution:</b> Throws {@link UnsupportedOperationException}.
	 *
	 * @return
	 *          the {@code Color} of the foreground
	 * @throws UnsupportedOperationException
	 *          thrown because {@code DummyRenderEnvironment} is for font measuring only
	 */
	@Override
	public Color getForegroundColor() {
		throw new UnsupportedOperationException("This is a dummy render environment for font measuring");
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