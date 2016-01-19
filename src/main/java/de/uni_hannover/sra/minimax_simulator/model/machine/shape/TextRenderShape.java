package de.uni_hannover.sra.minimax_simulator.model.machine.shape;

import com.sun.javafx.tk.FontMetrics;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.ui.layout.ComponentShape;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Dimension;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * A {@code TextRenderShape} is a {@link ComponentShape} containing text.
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public abstract class TextRenderShape implements ComponentShape {

    private FontMetricsProvider fontProvider;

    private Font font;
    private FontMetrics fontMetrics;

    private String cachedString;
    private Dimension cachedDimension;

    /**
     * Initializes the {@code TextRenderShape}.
     *
     * @param fontProvider
     *          the {@link FontMetricsProvider} used for font measuring.
     */
    protected TextRenderShape(FontMetricsProvider fontProvider) {
        if (fontProvider == null) {
            throw new NullPointerException("FontMetricsProvider must not be null");
        }

        this.fontProvider = fontProvider;

        font = this.fontProvider.getFont();
        fontMetrics = this.fontProvider.getFontMetrics();

        cachedString = "";
        cachedDimension = new Dimension(0, 0);
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
        if (font != fontProvider.getFont() || fontMetrics != fontProvider.getFontMetrics()) {
            font = fontProvider.getFont();
            fontMetrics = fontProvider.getFontMetrics();

            cachedString = "";
            cachedDimension = new Dimension(0, 0);
        }

        if (font == null) {
            throw new IllegalStateException("Font is null in layout of " + getClass().getSimpleName());
        }

        if (fontMetrics == null) {
            throw new IllegalStateException("FontMetrics is null in layout of " + getClass().getSimpleName());
        }

        if (str.equals(cachedString)) {
            return cachedDimension;
        }
        else {
            cachedString = str;

            Text text = new Text(cachedString);
            text.setFont(font);

            int width = (int) fontMetrics.computeStringWidth(cachedString);
            int height = Math.round(fontMetrics.getAscent()) - 3;

            cachedDimension = new Dimension(width, height);

            return cachedDimension;
        }
    }
}