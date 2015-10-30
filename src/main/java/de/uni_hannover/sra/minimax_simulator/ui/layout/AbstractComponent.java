package de.uni_hannover.sra.minimax_simulator.ui.layout;

/**
 * Basic implementation of {@link Component}.
 *
 * @author Martin L&uuml;ck
 */
// TODO: why not abstract?
public class AbstractComponent implements Component {

	private Dimension dimension = new Dimension(0, 0);
	private Insets insets = new Insets(0, 0, 0, 0);
	private Bounds bounds = new Bounds(0, 0, 0, 0);
	private ComponentShape shape = null;
	private String name = "";

	@Override
	public void updateSize() {
		if (shape != null) {
			shape.updateShape(this);
		}
	}

	@Override
	public void doLayout() {
		if (shape != null) {
			shape.layout(this);
		}
	}

	@Override
	public Dimension getDimension() {
		return dimension;
	}

	@Override
	public void setDimension(Dimension dimension) {
		if (dimension == null) {
			throw new IllegalArgumentException("Cannot set null dimension");
		}
		this.dimension = dimension;
	}

	@Override
	public Insets getInsets() {
		return insets;
	}

	@Override
	public void setInsets(Insets insets) {
		if (insets == null) {
			throw new IllegalArgumentException("Cannot set null insets");
		}
		this.insets = insets;
	}

	@Override
	public Bounds getBounds() {
		return bounds;
	}

	@Override
	public void setBounds(Bounds bounds) {
		if (bounds == null) {
			throw new IllegalArgumentException("Cannot set null bounds");
		}
		this.bounds = bounds;
	}

	/**
	 * Gets the {@link ComponentShape} of the {@code AbstractComponent} instance.
	 *
	 * @return
	 * 			the shape
	 */
	public ComponentShape getShape() {
		return shape;
	}

	/**
	 * Sets the {@link ComponentShape} of the {@code AbstractComponent} instance.
	 *
	 * @param shape
	 * 			the new shape
	 */
	public void setShape(ComponentShape shape) {
		this.shape = shape;
	}

	/**
	 * Gets the name of the {@code AbstractComponent}.
	 *
	 * @return
	 * 			the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the {@code AbstractComponent}.
	 *
	 * @param name
	 * 			the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + (name.isEmpty() ? "" : "[" + name + "]");
	}
}