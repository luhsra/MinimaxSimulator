package de.uni_hannover.sra.minimax_simulator.model.machine.shape;

import com.sun.javafx.tk.FontMetrics;
import de.uni_hannover.sra.minimax_simulator.ui.layout.ComponentShape;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Dimension;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * A {@code TextRenderShape} is a {@link ComponentShape} containing text.
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public abstract class TextRenderShape implements ComponentShape {

	private FontMetricsProvider	_fontProvider;

	private Font 				_font;
	private FontMetrics 		_fontMetrics;

	private String				_cachedString;
	private Dimension			_cachedDimension;

	/**
	 * Initializes the {@code TextRenderShape}.
	 *
	 * @param fontProvider
	 *          the {@link FontMetricsProvider} used for font measuring.
	 */
	public TextRenderShape(FontMetricsProvider fontProvider) {
		if (fontProvider == null) {
			throw new NullPointerException("FontMetricsProvider must not be null");
		}

		_fontProvider = fontProvider;

		_font = _fontProvider.getFont();
		_fontMetrics = _fontProvider.getFontMetrics();

		_cachedString = "";
		_cachedDimension = new Dimension(0, 0);
	}

	/**
	 * Measures the width and height of the given {@code String} using the {@code Font}
	 * and {@code FontMetrics} provided by the {@code FontMetricsProvider}.
	 *
	 * @param str
	 *          the {@code String} to measure
	 * @return
	 *          the {@link Dimension} of the {@code String}
	 */
	protected Dimension getStringDimension(String str) {
		if (_font != _fontProvider.getFont() || _fontMetrics != _fontProvider.getFontMetrics()) {
			_font = _fontProvider.getFont();
			_fontMetrics = _fontProvider.getFontMetrics();

			_cachedString = "";
			_cachedDimension = new Dimension(0, 0);
		}

		if (_font == null) {
			throw new IllegalStateException("Font is null in layout of " + getClass().getSimpleName());
		}

		if (_fontMetrics == null) {
			throw new IllegalStateException("FontMetrics is null in layout of " + getClass().getSimpleName());
		}

		if (str.equals(_cachedString)) {
			return _cachedDimension;
		}
		else {
			_cachedString = str;

			Text text = new Text(_cachedString);
			text.setFont(_font);

			int width = (int) _fontMetrics.computeStringWidth(_cachedString);
			int height = Math.round(_fontMetrics.getAscent()) - 3;

			_cachedDimension = new Dimension(width, height);

			return _cachedDimension;
		}
	}
}