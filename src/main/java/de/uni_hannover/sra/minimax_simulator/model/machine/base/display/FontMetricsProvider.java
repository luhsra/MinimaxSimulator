package de.uni_hannover.sra.minimax_simulator.model.machine.base.display;

import java.awt.Font;
import java.awt.FontMetrics;

public interface FontMetricsProvider
{
	public Font getFont();

	public FontMetrics getFontMetrics();
}