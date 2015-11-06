package de.uni_hannover.sra.minimax_simulator.ui.gui.components.tableview;

import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * An extended {@link TableColumn} for the UI representation of the {@link de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable}.<br>
 * A column of this class is not sortable, has a min width of 30 px and a column header rotated by -90 degrees as well as a custom {@code CellValueFactory}.
 *
 * @author Philipp Rohde
 */
public abstract class SignalTableColumn extends TableColumn {

    /**
     * Creates a {@link TableColumn} that is not sortable, has a min width of 30 px,
     * a rotated header and a custom {@code CellValueFactory}.
     *
     * @param label
     *          the text for the column header
     * @param id
     *          the column's ID
     * @param index
     *          the column index
     */
    protected SignalTableColumn(String label, String id, int index) {
        super();
        setSortable(false);
        UIUtil.rotateColumnHeader(this, label);
        setId(id);
        setMinWidth(30);

        setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                try {
                    return new SimpleStringProperty(param.getValue().get(index).toString());
                } catch (Exception e) {
                    return new SimpleStringProperty("");
                }
            }
        });
    }

    /**
     * Sets the min, max and pref width to the same value.
     *
     * @param width
     *          the width to set
     */
    protected void setWidth(double width) {
        setMinWidth(width);
        setMaxWidth(width);
        setPrefWidth(width);
    }

}
