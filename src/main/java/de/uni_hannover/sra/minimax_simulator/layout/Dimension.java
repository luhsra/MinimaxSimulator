package de.uni_hannover.sra.minimax_simulator.layout;

/**
 * Immutable representant for the dimension of a rectangular entity.<br>
 * It consists of an integer width and an integer height.
 * 
 * @author Martin
 *
 */
public class Dimension
{
	public final static Dimension ZERO = new Dimension(0, 0);

	public final int w;
	public final int h;

	/**
	 * Constructs a new Dimension instance.
	 * 
	 * @param width a non-negative integer
	 * @param height a non-negative integer
	 */
	public Dimension(int width, int height)
	{
		if (width < 0)
			throw new IllegalArgumentException("width < 0: " + width);
		if (height < 0)
			throw new IllegalArgumentException("height < 0: " + height);

		this.w = width;
		this.h = height;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + h;
		result = prime * result + w;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Dimension other = (Dimension) obj;
		if (h != other.h)
			return false;
		if (w != other.w)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "[" + w + "," + h + "]";
	}

	public java.awt.Dimension toAWT()
	{
		return new java.awt.Dimension(w, h);
	}

	public Dimension addInsets(Insets in)
	{
		return new Dimension(w + in.l + in.r, h + in.t + in.b);
	}
}