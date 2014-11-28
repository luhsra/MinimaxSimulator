package de.uni_hannover.sra.minimax_simulator.ui.render;

import static com.google.common.base.Preconditions.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class DefaultRenderEnvironment implements RenderEnvironment
{
	private final Font _font;
	private final FontMetrics _fontMetrics;

	public DefaultRenderEnvironment(Font font, FontMetrics fontMetrics)
	{
		_font = checkNotNull(font);
		_fontMetrics = checkNotNull(fontMetrics);
	}

	@Override
	public Graphics2D createGraphics(Graphics2D g)
	{
		Graphics2D g2d = (Graphics2D) g.create();

		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		//g2d.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

		g2d.setFont(_font);

		return g2d;
	}

	@Override
	public Color getBackgroundColor()
	{
		return Color.WHITE;
	}

	@Override
	public Color getForegroundColor()
	{
		return Color.BLACK;
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