package de.uni_hannover.sra.minimax_simulator.ui.layout;

/**
 * A {@code Component} is a class that can be rendered.
 *
 * @author Martin L&uuml;ck
 */
public interface Component {

	/**
	 * Gets the {@link Bounds} of the instance.
	 *
	 * @return
	 * 			the bounds
	 */
	public Bounds getBounds();

	/**
	 * Sets the {@link Bounds} of the instance.
	 *
	 * @param bounds
	 * 			the new bounds
	 */
	public void setBounds(Bounds bounds);

	/**
	 * Gets the {@link Dimension} of the instance.
	 *
	 * @return
	 * 			the dimension
	 */
	public Dimension getDimension();

	/**
	 * Sets the {@link Dimension} of the instance.
	 *
	 * @param dimension
	 * 			the new dimension
	 */
	public void setDimension(Dimension dimension);

	/**
	 * Updates the size of the instance.
	 */
	public void updateSize();

	/**
	 * Does the layout of the instance.
	 */
	public void doLayout();

	/**
	 * Gets the {@link Insets} of the instance.
	 *
	 * @return
	 * 			the insets
	 */
	public Insets getInsets();

	/**
	 * Sets the {@link Insets} of the instance.
	 *
	 * @param insets
	 * 			the new insets
	 */
	public void setInsets(Insets insets);
}