package de.uni_hannover.sra.minimax_simulator.ui.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public interface RenderEnvironment
{
	public Graphics2D createGraphics(Graphics2D g);

	public Color getBackgroundColor();

	public Color getForegroundColor();

	public Font getFont();

	public FontMetrics getFontMetrics();
}