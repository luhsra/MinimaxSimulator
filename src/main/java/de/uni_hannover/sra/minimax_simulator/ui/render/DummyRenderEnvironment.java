package de.uni_hannover.sra.minimax_simulator.ui.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class DummyRenderEnvironment implements RenderEnvironment
{
	private final Font _font;
	private final FontMetrics _fontMetrics;

	public DummyRenderEnvironment()
	{
		_font = new Font("SansSerif", Font.PLAIN, 17);
		_fontMetrics = new JPanel().getFontMetrics(_font);
	}

	@Override
	public Graphics2D createGraphics(Graphics2D g)
	{
		throw new UnsupportedOperationException("This is a dummy render environment for font measuring");
	}

	@Override
	public Color getBackgroundColor()
	{
		throw new UnsupportedOperationException("This is a dummy render environment for font measuring");
	}

	@Override
	public Color getForegroundColor()
	{
		throw new UnsupportedOperationException("This is a dummy render environment for font measuring");
	}

	@Override
	public Font getFont()
	{
		return _font;
	}

	@Override
	public FontMetrics getFontMetrics()
	{
		return _fontMetrics;
	}
}