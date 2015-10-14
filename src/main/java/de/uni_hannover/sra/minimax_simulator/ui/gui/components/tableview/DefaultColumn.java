package de.uni_hannover.sra.minimax_simulator.ui.gui.components.tableview;

/**
 * The default implementation of a {@link SignalTableColumn}.
 *
 * @author Philipp Rohde
 */
public class DefaultColumn extends SignalTableColumn {

    /**
     * Creates a default {@link SignalTableColumn}.
     *
     * @param label
     *          the text for the column header
     * @param id
     *          the column's ID
     * @param index
     *          the column index
     */
    public DefaultColumn(String label, String id, int index) {
        super(label, id, index);
    }

}
