package de.uni_hannover.sra.minimax_simulator.ui.layout;

/**
 * Representation of a point in a two-dimensional space.
 *
 * @author Martin L&uuml;ck
 */
public class Point {

	/** x-coordinate */
	public final int x;
	/** y-coordinate */
	public final int y;

	/**
	 * Constructs a new {@code Point} with the specified coordinates.
	 *
	 * @param x
	 * 			the x-coordinate
	 * @param y
	 * 			the y-coordinate
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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

		Point other = (Point) obj;
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "[" + x + "," + y + "]";
	}
}