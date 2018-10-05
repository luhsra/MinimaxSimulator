package de.uni_hannover.sra.minimax_simulator.ui.gui.components.tableview;

import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;

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
    public DefaultColumn(String label, String id, int index, boolean centered) {
        super(label, id, index);

        setCellFactory(param -> {
            TableCell<ObservableList, String> cell = new TableCell<ObservableList, String>() {

                @Override
                public void updateItem(String item, boolean empty) {

                    if (item == null) {
                        setGraphic(null);
                    } else {
                        if (centered) {
                            setGraphic(new CenteredCellPane(item, true));
                        }
                        else {
                            setGraphic(new CellPane(item, true));
                        }
                    }
                }
            };
            return cell;
        });
    }
}
