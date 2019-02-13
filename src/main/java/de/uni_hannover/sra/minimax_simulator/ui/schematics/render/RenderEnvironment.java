package de.uni_hannover.sra.minimax_simulator.ui.schematics.render;

import de.uni_hannover.sra.minimax_simulator.ui.gui.util.FontMetrics;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * A {@code RenderEnvironment} is used for some basic rendering related methods.
 *
 * @author Martin L&uuml;ck
 */
public interface RenderEnvironment {

    /**
     * Gets the background {@link Color}.
     *
     * @return
     *          the {@code Color} of the background
     */
    public Color getBackgroundColor();

    /**
     * Gets the foreground {@link Color}.
     *
     * @return
     *          the {@code Color} of the foreground
     */
    public Color getForegroundColor();

    /**
     * Gets the currently used {@link Font}.
     *
     * @return
     *          the currently used {@code Font}
     */
    public Font getFont();

    /**
     * Gets the {@link FontMetrics} of the {@code RenderEnvironment}.
     *
     * @return
     *          the {@code FontMetrics} of the {@code RenderEnvironment}.
     */
    public FontMetrics getFontMetrics();
}