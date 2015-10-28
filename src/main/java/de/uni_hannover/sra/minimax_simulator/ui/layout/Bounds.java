package de.uni_hannover.sra.minimax_simulator.ui.layout;

/**
 * This class represents the bounds of a {@link Component}.<br>
 * <br>
 * The bounds of two {@code Component}s cannot overlap.
 *
 * @author Martin L&uuml;ck
 */
public class Bounds {

	/** The x-coordinate. */
	public final int x;
	/** The y-coordinate. */
	public final int y;
	/** The width. */
	public final int w;
	/** The height. */
	public final int h;

	/**
	 * Constructs a new {@code Bounds} instance with all values set to zero.
	 */
	public Bounds() {
		x = 0;
		y = 0;
		w = 0;
		h = 0;
	}

	/**
	 * Constructs a new {@code Bounds} instance with the specified values.
	 *
	 * @param x
	 * 			the x-coordinate
	 * @param y
	 * 			the y-coordinate
	 * @param w
	 * 			the width
	 * @param h
	 * 			the height
	 * @throws IllegalArgumentException
	 * 			thrown if {@code w} or {@code h} is {@code < 0}
	 */
	public Bounds(int x, int y, int w, int h) {
		if (w < 0) {
			throw new IllegalArgumentException("width < 0: " + w);
		}
		if (h < 0) {
			throw new IllegalArgumentException("height < 0: " + h);
		}

		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	/**
	 * Constructs a new {@code Bounds} instance as copy of the specified instance.
	 *
	 * @param b
	 * 			the {@code Bounds} instance to copy
	 * @throws IllegalArgumentException
	 * 			thrown if the width or height is {@code < 0}
	 */
	public Bounds(Bounds b) {
		if (b.w < 0) {
			throw new IllegalArgumentException("width < 0: " + b.w);
		}
		if (b.h < 0) {
			throw new IllegalArgumentException("height < 0: " + b.h);
		}

		x = b.x;
		y = b.y;
		w = b.w;
		h = b.h;
	}

	@Override
	public String toString() {
		return "[" + x + "," + y + " / " + w + "," + h + "]";
	}
}