package de.uni_hannover.sra.minimax_simulator.ui.layout;

public class GridContainer extends AbstractListContainer
{
	private final int _cols;
	private final int _rows;

	private final int[] _rowHeights;
	private final int[] _colWidths;

	@Override
	protected void updateMySize()
	{
		zero(_rowHeights);
		zero(_colWidths);

		int x = 0;
		int y = 0;

		int rowHeight = 0;

		// calculate row heights and column widths
		for (Component comp : _children)
		{
			if (y == _rows)
			{
				throw new IllegalStateException("Too many elements");
			}

			comp.updateSize();

			Insets insets = comp.getInsets();
			Dimension dim = comp.getDimension();

			int itsWidth = dim.w + insets.r + insets.l;
			int itsHeight = dim.h + insets.b + insets.t;

			if (itsHeight > rowHeight)
				rowHeight = itsHeight;

			if (_colWidths[x] < itsWidth)
				_colWidths[x] = itsWidth;

			x++;

			if (x == _cols)
			{
				_rowHeights[y] = rowHeight;
				rowHeight = 0;

				x = 0;
				y++;
			}
		}

		int width = 0;
		int height = 0;
		for (int colW : _colWidths)
			width += colW;
		for (int rowH : _rowHeights)
			height += rowH;

		setDimension(new Dimension(width, height));
	}

	public GridContainer(int cols, int rows)
	{
		if (cols < 1 || rows < 1)
			throw new IllegalArgumentException("Require at least 1 col and 1 row");

		_cols = cols;
		_rows = rows;
		_rowHeights = new int[_rows];
		_colWidths = new int[_cols];
	}

	@Override
	protected void layoutChildren()
	{
		int x = getBounds().x;
		int y = getBounds().y;

		int col = 0;
		int row = 0;

		for (Component child : _children)
		{
			if (row == _rows)
			{
				throw new IllegalStateException("Too many elements");
			}

			Dimension dim = child.getDimension();
			Insets ins = child.getInsets();

			child.setBounds(new Bounds(x + ins.l, y + ins.t, dim.w, dim.h));
			x += _colWidths[col];
			col++;

			if (col == _cols)
			{
				x = getBounds().x;
				col = 0;

				y += _rowHeights[row];
				row++;
			}
		}
	}

	private void zero(int[] array)
	{
		for (int i = 0; i < array.length; i++)
			array[i] = 0;
	}
}