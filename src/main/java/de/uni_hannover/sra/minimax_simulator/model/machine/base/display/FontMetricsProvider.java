package de.uni_hannover.sra.minimax_simulator.model.machine.base.display;

import de.uni_hannover.sra.minimax_simulator.ui.gui.util.FontMetrics;
import javafx.scene.text.Font;

/**
 * The {@code FontMetricsProvider} provides the methods needed to
 * return information about the {@link Font} and {@link FontMetrics} used.
 *
 * @author Martin L&uuml;ck
 */
public interface FontMetricsProvider {

    /**
     * Gets the used {@code Font}.
     *
     * @return
     *          the used {@code Font}
     */
    public Font getFont();

    /**
     * Gets the {@code FontMetrics} of the used {@code Font}.
     *
     * @return
     *          the {@code FontMetrics} of the used {@code Font}
     */
    public FontMetrics getFontMetrics();
}