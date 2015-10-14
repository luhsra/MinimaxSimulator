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

	public DummyRenderEnvironment() {
		font = new Font("SansSerif", 17);
		fontMetrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(font);
	}

	@Override
	public Color getBackgroundColor() {
		throw new UnsupportedOperationException("This is a dummy render environment for font measuring");
	}

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