package de.uni_hannover.sra.minimax_simulator.model.machine.shape;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.font.TextLayout;

import de.uni_hannover.sra.minimax_simulator.layout.ComponentShape;
import de.uni_hannover.sra.minimax_simulator.layout.Dimension;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;

public abstract class TextRenderShape implements ComponentShape
{
	private FontMetricsProvider	_fontProvider;

	private Font				_font;
	private FontMetrics			_fontMetrics;

	private String				_cachedString;
	private Dimension			_cachedDimension;

	public TextRenderShape(FontMetricsProvider fontProvider)
	{
		if (fontProvider == null)
			throw new NullPointerException("FontMetricsProvider must not be null");
		_fontProvider = fontProvider;

		_font = _fontProvider.getFont();
		_fontMetrics = _fontProvider.getFontMetrics();

		_cachedString = "";
		_cachedDimension = new Dimension(0, 0);
	}

	protected Dimension getStringDimension(String str)
	{
		if (_font != _fontProvider.getFont()
			|| _fontMetrics != _fontProvider.getFontMetrics())
		{
			_font = _fontProvider.getFont();
			_fontMetrics = _fontProvider.getFontMetrics();

			_cachedString = "";
			_cachedDimension = new Dimension(0, 0);
		}

		if (_font == null)
			throw new IllegalStateException("Font is null in layout of "
				+ getClass().getSimpleName());

		if (_fontMetrics == null)
			throw new IllegalStateException("FontMetrics is null in layout of "
				+ getClass().getSimpleName());

		if (str.equals(_cachedString))
		{
			return _cachedDimension;
		}
		else
		{
			_cachedString = str;

			TextLayout textLayout = new TextLayout(_cachedString, _font, _fontMetrics.getFontRenderContext());

			Rectangle rec = textLayout.getPixelBounds(null, 0, 0);
			//GlyphVector g = _font.createGlyphVector(_fontMetrics.getFontRenderContext(),
			//	str);
			//Rectangle rec = g.getVisualBounds().getBounds();
			_cachedDimension = new Dimension(rec.width, rec.height);

			return _cachedDimension;
		}
	}
}