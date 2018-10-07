package de.uni_hannover.sra.minimax_simulator.ui.gui.util;

import javafx.geometry.Bounds;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * This is a simple replacement for the {@code com.sun.javafx.tk.FontMetrics} class which is deprecated since JDK 9.
 *
 * @author Philipp Rohde
 * @author Werner Van Belle
 * @link http://werner.yellowcouch.org/log/fontmetrics-jdk9/
 */
public class FontMetrics {
    /** for measurements */
    final private Text internal;
    /** the ascent */
    private float ascent;
    /** the descent */
    private float descent;
    /** the line height */
    private float lineHeight;

    /**
     * Creates a new {@code FontMetrics} instance.
     *
     * @param fnt
     *          the {@code Font} to use
     */
    public FontMetrics(Font fnt) {
        internal = new Text();
        internal.setFont(fnt);
        Bounds b = internal.getLayoutBounds();
        lineHeight = (float) b.getHeight();
        ascent = (float) -b.getMinY();
        descent =(float) b.getMaxY();
    }

    /**
     * Computes the width of the given text using the font of the {@code FontMetrics} instance.
     *
     * @param txt
     *           the text to compute the width for
     * @return
     *           the width of the text using the instance's font
     */
    public float computeStringWidth(String txt) {
        internal.setText(txt);
        return (float) internal.getLayoutBounds().getWidth();
    }

    /**
     * Gets the ascent.
     *
     * @return
     *           the ascent
     */
    public float getAscent() {
        return ascent;
    }

    /**
     * Gets the descent.
     *
     * @return
     *           the descent
     */
    public float getDescent() {
        return descent;
    }

    /**
     * Gets the line height.
     *
     * @return
     *           the line height
     */
    public float getLineHeight() {
        return lineHeight;
    }
}
