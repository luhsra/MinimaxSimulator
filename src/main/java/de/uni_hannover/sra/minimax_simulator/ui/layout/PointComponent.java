package de.uni_hannover.sra.minimax_simulator.ui.layout;

public class PointComponent implements Component
{
	private Bounds _bounds;

	public PointComponent()
	{
		_bounds = new Bounds();
	}

	public int getX()
	{
		return getBounds().x;
	}

	public int getY()
	{
		return getBounds().y;
	}

	public void set(int x, int y)
	{
		setBounds(new Bounds(x, y, 0, 0));
	}

	@Override
	public Bounds getBounds()
	{
		return _bounds;
	}

	@Override
	public void setBounds(Bounds bounds)
	{
		_bounds = bounds;
	}

	@Override
	public Dimension getDimension()
	{
		return Dimension.ZERO;
	}

	@Override
	public void setDimension(Dimension dimension)
	{
		// Ignored for point components
	}

	@Override
	public void setInsets(Insets insets)
	{
		// Ignored for point components
	}

	@Override
	public void updateSize()
	{
	}

	@Override
	public void doLayout()
	{
	}

	@Override
	public Insets getInsets()
	{
		return Insets.ZERO;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName();
	}
}