package de.uni_hannover.sra.minimax_simulator.model.machine.base.display;

import com.sun.javafx.tk.FontMetrics;
import javafx.scene.text.Font;

public interface FontMetricsProvider {

	public Font getFont();

	public FontMetrics getFontMetrics();
}