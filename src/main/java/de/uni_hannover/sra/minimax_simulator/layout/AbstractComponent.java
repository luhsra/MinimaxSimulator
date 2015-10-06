package de.uni_hannover.sra.minimax_simulator.layout;


public class AbstractComponent implements Component {

	private Dimension _dimension = new Dimension(0, 0);
	private Insets _insets = new Insets(0, 0, 0, 0);
	private Bounds _bounds = new Bounds(0, 0, 0, 0);
	private ComponentShape _shape = null;
	private String _name = "";

	@Override
	public void updateSize() {
		if (_shape != null)
			_shape.updateShape(this);
	}

	@Override
	public void doLayout() {
		if (_shape != null)
			_shape.layout(this);
	}

	@Override
	public Dimension getDimension() {
		return _dimension;
	}

	@Override
	public void setDimension(Dimension dimension) {
		if (dimension == null)
			throw new IllegalArgumentException("Cannot set null dimension");
		_dimension = dimension;
	}

	@Override
	public Insets getInsets() {
		return _insets;
	}

	@Override
	public void setInsets(Insets insets) {
		if (insets == null)
			throw new IllegalArgumentException("Cannot set null insets");
		_insets = insets;
	}

	@Override
	public Bounds getBounds() {
		return _bounds;
	}

	@Override
	public void setBounds(Bounds bounds) {
		if (bounds == null)
			throw new IllegalArgumentException("Cannot set null bounds");
		_bounds = bounds;
	}

	public ComponentShape getShape() {
		return _shape;
	}

	public void setShape(ComponentShape shape) {
		_shape = shape;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + (_name.isEmpty() ? "" : "[" + _name + "]");
	}
}