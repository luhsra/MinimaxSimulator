package de.uni_hannover.sra.minimax_simulator.ui.render;

import com.sun.javafx.tk.Toolkit;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * A {@link RenderEnvironment} for font measuring.
 *
 * @author Martin L&uuml;ck
 */
public class DummyRenderEnvironment implements RenderEnvironment {

	private final Font _font;
	private final FontMetrics _fontMetrics;

	private final javafx.scene.text.Font font;
	private final com.sun.javafx.tk.FontMetrics fontMetrics;

	public DummyRenderEnvironment()
	{
		_font = new Font("SansSerif", Font.PLAIN, 17);
		_fontMetrics = new JPanel().getFontMetrics(_font);

		font = new javafx.scene.text.Font("SansSerif", 17);
		fontMetrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(font);
	}

	@Override
	@Deprecated
	public Graphics2D createGraphics(Graphics2D g)
	{
		throw new UnsupportedOperationException("This is a dummy render environment for font measuring");
	}

	@Override
	@Deprecated
	public Color getBackgroundColor()
	{
		throw new UnsupportedOperationException("This is a dummy render environment for font measuring");
	}

	@Override
	@Deprecated
	public Color getForegroundColor()
	{
		throw new UnsupportedOperationException("This is a dummy render environment for font measuring");
	}

	@Override
	@Deprecated
	public Font getFont()
	{
		return _font;
	}

	@Override
	@Deprecated
	public FontMetrics getFontMetrics()
	{
		return _fontMetrics;
	}

	@Override
	public javafx.scene.paint.Color getBackgroundColorFX() {
		throw new UnsupportedOperationException("This is a dummy render environment for font measuring");
	}

	@Override
	public javafx.scene.paint.Color getForegroundColorFX() {
		throw new UnsupportedOperationException("This is a dummy render environment for font measuring");
	}

	@Override
	public javafx.scene.text.Font getFontFX() {
		return this.font;
	}

	@Override
	public com.sun.javafx.tk.FontMetrics getFontMetricsFX() {
		return this.fontMetrics;
	}
}