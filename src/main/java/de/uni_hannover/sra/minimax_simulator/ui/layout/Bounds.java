package de.uni_hannover.sra.minimax_simulator.ui.layout;

public class Bounds
{
	public final int x;
	public final int y;
	public final int w;
	public final int h;

	public Bounds()
	{
		x = 0;
		y = 0;
		w = 0;
		h = 0;
	}

	public Bounds(int x, int y, int w, int h)
	{
		if (w < 0)
			throw new IllegalArgumentException("width < 0: " + w);
		if (h < 0)
			throw new IllegalArgumentException("height < 0: " + h);

		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public Bounds(Bounds b)
	{
		if (b.w < 0)
			throw new IllegalArgumentException("width < 0: " + b.w);
		if (b.h < 0)
			throw new IllegalArgumentException("height < 0: " + b.h);

		x = b.x;
		y = b.y;
		w = b.w;
		h = b.h;
	}

	@Override
	public String toString()
	{
		return "[" + x + "," + y + " / " + w + "," + h + "]";
	}
}