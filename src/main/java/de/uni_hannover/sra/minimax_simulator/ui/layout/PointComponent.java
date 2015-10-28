package de.uni_hannover.sra.minimax_simulator.ui.layout;

/**
 * A {@link Component} that occupies only a single point.
 *
 * @author Martin L&uuml;ck
 */
public class PointComponent implements Component {

	private Bounds _bounds;

	/**
	 * Constructs a new {@code PointComponent} at {@code (0,0)}.
	 */
	public PointComponent() {
		_bounds = new Bounds();
	}

	/**
	 * Gets the x-coordinate of the {@code PointComponent}.
	 *
	 * @return
	 * 			the x-coordinate
	 */
	public int getX() {
		return getBounds().x;
	}

	/**
	 * Gets the y-coordinate of the {@code PointComponent}.
	 *
	 * @return
	 * 			the y-coordinate
	 */
	public int getY() {
		return getBounds().y;
	}

	/**
	 * Sets the x- and y-coordinate to the specified values.
	 *
	 * @param x
	 * 			the new x-coordinate
	 * @param y
	 * 			the new y-coordinate
	 */
	public void set(int x, int y) {
		setBounds(new Bounds(x, y, 0, 0));
	}

	@Override
	public Bounds getBounds() {
		return _bounds;
	}

	@Override
	public void setBounds(Bounds bounds) {
		_bounds = bounds;
	}

	/**
	 * Gets the {@link Dimension#ZERO} instance because a {@code PointComponent} has no dimension.
	 *
	 * @return
	 * 			zero dimension instance
	 */
	@Override
	public Dimension getDimension() {
		return Dimension.ZERO;
	}

	@Override
	public void setDimension(Dimension dimension) {
		// ignored for point components
	}

	@Override
	public void setInsets(Insets insets) {
		// ignored for point components
	}

	@Override
	public void updateSize() {
		// ignored for point components
	}

	@Override
	public void doLayout() {
		// ignored for point components
	}

	/**
	 * Gets the {@link Insets#ZERO} instance because a {@code PointComponent} has no insets.
	 *
	 * @return
	 * 			zero insets instance
	 */
	@Override
	public Insets getInsets() {
		return Insets.ZERO;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}