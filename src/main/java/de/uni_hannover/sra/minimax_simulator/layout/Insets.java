package de.uni_hannover.sra.minimax_simulator.layout;

public class Insets
{
	public final static Insets ZERO = new Insets(0, 0, 0, 0);

	public final int t;
	public final int b;
	public final int l;
	public final int r;

	public Insets(int t, int b, int l, int r)
	{
		this.t = t;
		this.b = b;
		this.l = l;
		this.r = r;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + b;
		result = prime * result + l;
		result = prime * result + r;
		result = prime * result + t;
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

		Insets other = (Insets) obj;
		if (b != other.b)
			return false;
		if (l != other.l)
			return false;
		if (r != other.r)
			return false;
		if (t != other.t)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "[" + t + "," + b + "," + l + "," + r + "]";
	}
}