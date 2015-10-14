package de.uni_hannover.sra.minimax_simulator.ui.layout;

public interface Component
{
	public Bounds getBounds();

	public void setBounds(Bounds bounds);

	public Dimension getDimension();

	public void setDimension(Dimension dimension);

	public void updateSize();

	public void doLayout();

	public Insets getInsets();

	public void setInsets(Insets insets);
}