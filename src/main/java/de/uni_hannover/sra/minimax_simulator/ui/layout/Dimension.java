package de.uni_hannover.sra.minimax_simulator.ui.layout;

/**
 * Immutable representative for the dimension of a rectangular entity.<br>
 * It consists of an integer width and an integer height.
 * 
 * @author Martin L&uuml;ck
 */
public class Dimension {

	/** Zero {@code Dimension} instance. */
	public static final Dimension ZERO = new Dimension(0, 0);

	/** The width. */
	public final int w;
	/** The height. */
	public final int h;

	/**
	 * Constructs a new {@code Dimension} instance.
	 * 
	 * @param width
	 * 			the width
	 * @param height
	 * 			the height
	 * @throws IllegalArgumentException
	 * 			thrown if {@code width} or {@code height} is {@code < 0}
	 */
	public Dimension(int width, int height) {
		if (width < 0) {
			throw new IllegalArgumentException("width < 0: " + width);
		}
		if (height < 0) {
			throw new IllegalArgumentException("height < 0: " + height);
		}

		this.w = width;
		this.h = height;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + h;
		result = prime * result + w;
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

		Dimension other = (Dimension) obj;
		if (h != other.h) {
			return false;
		}
		if (w != other.w) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "[" + w + "," + h + "]";
	}

	/**
	 * Gets the width of the {@code Dimension} instance.
	 *
	 * @return
	 * 			the width
	 */
	public int getWidth() {
		return w;
	}

	/**
	 * Gets the height of the {@code Dimension} instance.
	 *
	 * @return
	 * 			the height
	 */
	public int getHeight() {
		return h;
	}

	/**
	 * Adds the specified {@link Insets} to the {@code Dimension}.
	 *
	 * @param in
	 * 			the {@code Insets} to add
	 * @return
	 * 			a new {@code Dimension} instance with the added {@code Insets}
	 */
	public Dimension addInsets(Insets in) {
		return new Dimension(w + in.l + in.r, h + in.t + in.b);
	}
}