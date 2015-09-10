package de.uni_hannover.sra.minimax_simulator.gui;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.gui.components.*;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.signal.*;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;

import java.util.*;

/**
 * <b>FXController of the SignalView</b><br>
 * <br>
 * This controller handles every GUI interaction with the signal table {@link Tab}.
 * The SignalView is the UI of the {@link SignalTable}.
 *
 * @author Philipp Rohde
 */
public class SignalView implements SignalTableListener {

    private final TextResource _res;

    private SignalTable _signal;

    @FXML private TableView signaltable;

    /**
     * The constructor initializes the final variables.
     */
    public SignalView() {
        _res = Main.getTextResource("signal");
    }

    /**
     * This method is called from the main controller if a new project was created or opened.
     * It initializes the signal {@link TableView} because it needs project data.
     */
    public void initSignalView() {
        _signal = Main.getWorkspace().getProject().getSignalTable();
        _signal.addSignalTableListener(this);

        signaltable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                btnMoveDown.setDisable(false);
                btnMoveUp.setDisable(false);
                if (signaltable.getSelectionModel().getSelectedIndex() == 0) {
                    btnMoveUp.setDisable(true);
                }
                else if (signaltable.getSelectionModel().getSelectedIndex() == signaltable.getItems().size()-1) {
                    btnMoveDown.setDisable(true);
                }
            }
        });

        updateSignalTable();
    }


    /**
     * Updates the signal table by complete recreation.
     */
    // TODO: is there a better way for this table?
    private void updateSignalTable() {
        signaltable.getItems().clear();
        signaltable.getColumns().clear();

        final List<String> columns = getAllColumnLabels();
        final List<String> columnIDs = getAllColumnIDs();

        for (int i = 0 ; i < columns.size(); i++) {
            TableColumn col;
            // set special update behavior
            if (i == 0) {
                col = new BreakpointColumn(columns.get(i), i);
            }
            else if (i == 1) {
                col = new LabelColumn(columns.get(i), i);
            }
            else if ( (i >= 3) && (i < columns.size()-3) ) {
                col = new SignalColumn(columns.get(i), columnIDs.get(i), i);
            }
            else if (i == columns.size()-3 || i == columns.size()-2) {
                col = new JumpTargetColumn(columns.get(i), columnIDs.get(i), i);
            }
            else {
                col = new DefaultColumn(columns.get(i), columnIDs.get(i), i);
            }

            signaltable.getColumns().add(col);
        }

        for (int i = 0; i < _signal.getRowCount(); i++) {
            signaltable.getItems().add(createTableRow(i, _signal.getRow(i)));
        }

    }

    /**
     * Creates an {@link ObservableList<String>} from a {@link SignalRow} to add to the signal table {@link TableView}.
     *
     * @param index
     *          the index of the row to create
     * @param signalRow
     *          the {@code SignalRow} to use for creating the table row data
     * @return
     *          the {@code SignalRow} as a list of {@code Strings}
     */
    private ObservableList<String> createTableRow(int index, SignalRow signalRow) {
        ObservableList<String> row = FXCollections.observableArrayList();

        row.add(String.valueOf(signalRow.isBreakpoint()));
        row.add(signalRow.getLabel());
        row.add(String.valueOf(index));

        Map<String, SignalValue> signalValues = signalRow.getSignalValues();
        ObservableList<TableColumn> cols = signaltable.getColumns();

        for (int j = 3; j < cols.size()-3; j++) {
            String lookUp = cols.get(j).getId();
            if (signalValues.containsKey(lookUp)) {
                row.add(String.valueOf(signalValues.get(lookUp).intValue()));
            }
            else {
                if ( (j >= 3 && j <= 5) || j == 7 || j == 8) {
                    row.add("-");
                }
                else {
                    row.add("0 ");
                }

            }
        }

        Jump j = signalRow.getJump();
        if (j.getTargetRow(index, 0) == j.getTargetRow(index, 1)) {
            row.add("-");
        }
        else {
            row.add("1\n0");
        }

        int target0 = j.getTargetRow(index, 0);
        int target1 = j.getTargetRow(index, 1);
        if (target0 == target1) {
            row.add(Integer.toString(target0));
        }
        else {
            row.add(target1 + "\n" + target0);
        }

        row.add(signalRow.getDescription());

        return row;
    }

    /**
     * Creates an unmodifiable list with all column labels.
     *
     * @return
     *          an unmodifiable list with all column labels
     */
    private List<String> getAllColumnLabels() {
        List<String> columns = new ArrayList<>();
        columns.add(_res.get("col.breakpoint"));
        columns.add(_res.get("col.label"));
        columns.add(_res.get("col.address"));
        columns.add(_res.get("col.aluselA"));
        columns.add(_res.get("col.aluselB"));
        columns.add(_res.get("col.mdrsel"));
        columns.add(_res.get("col.memcs"));
        columns.add(_res.get("col.memrw"));
        columns.add(_res.get("col.aluctrl"));

        for (RegisterExtension baseReg : Main.getWorkspace().getProject().getMachineConfiguration().getBaseRegisters()) {
            columns.add(baseReg.getName()+".W");
        }
        for (RegisterExtension reg : Main.getWorkspace().getProject().getMachineConfiguration().getRegisterExtensions()) {
            columns.add(reg.getName()+".W");
        }

        columns.add(_res.get("col.condition"));
        columns.add(_res.get("col.jumptarget"));
        columns.add(_res.get("col.description"));

        return Collections.unmodifiableList(columns);
    }

    /**
     * Creates an unmodifiable list with all column IDs.
     *
     * @return
     *          an unmodifiable list with all column IDs
     */
    private List<String> getAllColumnIDs() {
        List<String> columnIDs = new ArrayList<>();
        columnIDs.add("BREAKPOINT");
        columnIDs.add("LABEL");
        columnIDs.add("ADDRESS");
        columnIDs.add("ALU_SELECT_A");
        columnIDs.add("ALU_SELECT_B");
        columnIDs.add("MDR_SEL");
        columnIDs.add("MEM_CS");
        columnIDs.add("MEM_RW");
        columnIDs.add("ALU_CTRL");

        for (RegisterExtension baseReg : Main.getWorkspace().getProject().getMachineConfiguration().getBaseRegisters()) {
            columnIDs.add(baseReg.getName()+".W");
        }
        for (RegisterExtension reg : Main.getWorkspace().getProject().getMachineConfiguration().getRegisterExtensions()) {
            columnIDs.add(reg.getName()+".W");
        }

        columnIDs.add("CONDITION");
        columnIDs.add("JUMPTARGET");
        columnIDs.add("DESCRIPTION");

        return Collections.unmodifiableList(columnIDs);
    }

    /**
     * Adds a row to the {@link SignalTable}.<br>
     * If no row is selected the new row will be added to the end
     * otherwise it will be added after the selected row.
     */
    public void addRow() {
        int index = signaltable.getSelectionModel().getSelectedIndex();

        if (index == -1) {
            _signal.addSignalRow(new SignalRow());
        }
        else {
            _signal.addSignalRow(index+1, new SignalRow());
        }
    }

    /**
     * Deletes the selected row of the {@link TableView} from the {@link SignalTable}.
     */
    public void deleteRow() {
        int index = signaltable.getSelectionModel().getSelectedIndex();

        if (index != -1) {
            _signal.removeSignalRow(index);
        }
    }

    @FXML private Button btnMoveUp;
    @FXML private Button btnMoveDown;

    /**
     * Moves the currently selected signal row.<br>
     * It moves the row up if the caller is the moveUp {@link Button} or down if the caller is the moveDown {@link Button}.
     *
     * @param ae
     *          the {@link ActionEvent} calling the method
     */
    public void moveOperation(ActionEvent ae) {

        if (signaltable.getSelectionModel().getSelectedItems().isEmpty()) {
            return;
        }

        Object caller = ae.getSource();
        int difference = 0;
        if (caller.equals(btnMoveUp)) {
            difference = -1;
        }
        else if (caller.equals(btnMoveDown)) {
            difference = 1;
        }
        else {
            return;
        }

        int index1 = signaltable.getSelectionModel().getSelectedIndex();
        int index2 = index1 + difference;
        if (index2 < 0 || index2 >= signaltable.getItems().size()) {
            return;
        }

        // move rows in model and adapt selection
        _signal.exchangeSignalRows(index1, index2);
        signaltable.getSelectionModel().select(index2);

        Main.getWorkspace().setProjectUnsaved();
    }

    @Override
    public void onStructureChanged() {
        updateSignalTable();
    }

    @Override
    public void onRowAdded(int index, SignalRow row) {
        updateSignalTable();
    }

    @Override
    public void onRowRemoved(int index) {
        updateSignalTable();
    }

    @Override
    public void onRowsExchanged(int index1, int index2) {
        updateSignalTable();
    }

    @Override
    public void onRowReplaced(int index, SignalRow row) {
        updateSignalTable();
    }

    @Override
    public void onRowsUpdated(int fromIndex, int toIndex) {
        updateSignalTable();
    }

}
