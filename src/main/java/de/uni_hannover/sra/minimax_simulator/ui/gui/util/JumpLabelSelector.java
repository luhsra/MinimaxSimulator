package de.uni_hannover.sra.minimax_simulator.ui.gui.util;

import com.google.common.collect.ImmutableList;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.List;

/**
 * The {@code JumpLabelSelector} is basically a {@link ComboBox} that contains all {@link SignalRow}s with a label.<br>
 * But the data list is created by the constructor and the text of an associated {@link TextField} will be updated on change.
 *
 * @author Philipp Rohde
 * @author Martin L&uuml;ck
 */
public class JumpLabelSelector extends ComboBox {

    /**
     * Creates a {@code JumpLabelSelector} and sets the data.
     *
     * @param rowField
     *          the associated {@code TextField}
     * @param signalTable
     *          the {@code SignalTable} of the simulator
     * @param currentTargetRow
     *          the index of the row to be executed next
     */
    public JumpLabelSelector(TextField rowField, SignalTable signalTable, int currentTargetRow) {

        Target selected = null;

        ImmutableList.Builder<Target> b = ImmutableList.builder();
        for (int i = 0, n = signalTable.getRowCount(); i < n; i++) {
            SignalRow row = signalTable.getRow(i);
            if (row.getLabel() != null) {
                Target t = new Target(i, row.getLabel());
                b.add(t);
                if (i == currentTargetRow) {
                    selected = t;
                }

            }
        }
        List<Target> targets = b.build();

        this.setItems(FXCollections.observableList(targets));

        if (selected != null) {
            this.getSelectionModel().select(1);
        }

        this.valueProperty().addListener((obs, oldValue, newValue) -> {
            Target target = (Target) newValue;
            rowField.setText(String.valueOf(target.row));
        });
    }

    /**
     * A {@code Target} represents a {@link SignalRow} by its row index and label.
     *
     * @author Martin L&uuml;ck
     */
    private static class Target {
        protected int      row;
        protected String   label;

        public Target(int row, String label) {
            this.row = row;
            this.label = label;
        }

        @Override
        public String toString()
        {
            return label + " (" + row + ")";
        }
    }

}
