package de.uni_hannover.sra.minimax_simulator.ui.layout;

/**
 * The insets of a {@link Component}.
 *
 * @author Martin L&uuml;ck
 */
public class Insets {

	/** Zero {@code Insets} instance. */
	public final static Insets ZERO = new Insets(0, 0, 0, 0);

	/** top */
	public final int t;
	/** bottom */
	public final int b;
	/** left */
	public final int l;
	/** right */
	public final int r;

	/**
	 * Constructs a new {@code Insets} instance with the specified values.
	 *
	 * @param t
	 * 			inset top
	 * @param b
	 * 			inset bottom
	 * @param l
	 * 			inset left
	 * @param r
	 * 			inset right
	 */
	public Insets(int t, int b, int l, int r) {
		this.t = t;
		this.b = b;
		this.l = l;
		this.r = r;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + b;
		result = prime * result + l;
		result = prime * result + r;
		result = prime * result + t;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		else if (obj == null) {
			return false;
		}
		else if (getClass() != obj.getClass()) {
			return false;
		}

		Insets other = (Insets) obj;
		if (b != other.b) {
			return false;
		}
		if (l != other.l) {
			return false;
		}
		if (r != other.r) {
			return false;
		}
		if (t != other.t) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "[" + t + "," + b + "," + l + "," + r + "]";
	}
}