package de.uni_hannover.sra.minimax_simulator.ui.layout;

/**
 * The {@code GridContainer} is a {@link AbstractListContainer} that layouts
 * the child {@link Component}s in a grid.
 *
 * @author Martin L&uuml;ck
 */
public class GridContainer extends AbstractListContainer {

    private final int cols;
    private final int rows;

    private final int[] rowHeights;
    private final int[] colWidths;

    /**
     * Constructs a new {@code GridContainer} with the specified number of columns and rows.
     *
     * @param cols
     *          the number of columns
     * @param rows
     *          the number of rows
     * @throws IllegalArgumentException
     *          thrown if {@code cols} or {@code rows} is {@code < 1}
     */
    public GridContainer(int cols, int rows) {
        if (cols < 1 || rows < 1) {
            throw new IllegalArgumentException("Require at least 1 col and 1 row");
        }

        this.cols = cols;
        this.rows = rows;
        rowHeights = new int[this.rows];
        colWidths = new int[this.cols];
    }

    @Override
    protected void updateMySize() {
        zero(rowHeights);
        zero(colWidths);

        int x = 0;
        int y = 0;

        int rowHeight = 0;

        // calculate row heights and column widths
        for (Component comp : children) {
            if (y == rows) {
                throw new IllegalStateException("Too many elements");
            }

            comp.updateSize();

            Insets insets = comp.getInsets();
            Dimension dim = comp.getDimension();

            int itsWidth = dim.w + insets.r + insets.l;
            int itsHeight = dim.h + insets.b + insets.t;

            if (itsHeight > rowHeight) {
                rowHeight = itsHeight;
            }

            if (colWidths[x] < itsWidth) {
                colWidths[x] = itsWidth;
            }

            x++;

            if (x == cols) {
                rowHeights[y] = rowHeight;
                rowHeight = 0;

                x = 0;
                y++;
            }
        }

        int width = 0;
        int height = 0;
        for (int colW : colWidths) {
            width += colW;
        }
        for (int rowH : rowHeights) {
            height += rowH;
        }

        setDimension(new Dimension(width, height));
    }

    /**
     * Layouts the child {@link Component}s in a grid.
     */
    @Override
    protected void layoutChildren() {
        int x = getBounds().x;
        int y = getBounds().y;

        int col = 0;
        int row = 0;

        for (Component child : children) {
            if (row == rows) {
                throw new IllegalStateException("Too many elements");
            }

            Dimension dim = child.getDimension();
            Insets ins = child.getInsets();

            child.setBounds(new Bounds(x + ins.l, y + ins.t, dim.w, dim.h));
            x += colWidths[col];
            col++;

            if (col == cols) {
                x = getBounds().x;
                col = 0;

                y += rowHeights[row];
                row++;
            }
        }
    }

    /**
     * Zeros the specified integer array.
     *
     * @param array
     *          the array to zero
     */
    private void zero(int[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = 0;
        }
    }
}