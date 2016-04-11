package de.uni_hannover.sra.minimax_simulator.ui.gui;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListener;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTableListener;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalValue;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.gui.components.tableview.*;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.UndoManager;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands.SignalRowAddedCommand;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands.SignalRowMovedCommand;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands.SignalRowRemovedCommand;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <b>FXController of the SignalView</b><br>
 * <br>
 * This controller handles every GUI interaction with the signal table {@link Tab}.
 * The SignalView is the UI of the {@link SignalTable}.
 *
 * @author Philipp Rohde
 */
// TODO: optimize event handling
public class SignalView implements SignalTableListener, MachineConfigListener {

    private final TextResource res;

    private SignalTable signal;

    @FXML private TableView signaltable;
    @FXML private Button btnMoveUp;
    @FXML private Button btnMoveDown;

    /**
     * Initializes the final variables.
     */
    public SignalView() {
        res = Main.getTextResource("signal");
    }

    /**
     * This method is called from the main controller if a new project was created or opened.
     * It initializes the signal {@link TableView} because it needs project data.
     */
    public void initSignalView() {
        signal = Main.getWorkspace().getProject().getSignalTable();
        signal.addSignalTableListener(this);

        btnMoveDown.setDisable(true);
        btnMoveUp.setDisable(true);

        Main.getWorkspace().getProject().getMachineConfiguration().addMachineConfigListener(this);

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

        for (int i = 0; i < signal.getRowCount(); i++) {
            signaltable.getItems().add(createTableRow(i, signal.getRow(i)));
        }

    }

    /**
     * Creates an {@link ObservableList} from a {@link SignalRow} to add to the signal table {@link TableView}.
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
        columns.add(res.get("col.breakpoint"));
        columns.add(res.get("col.label"));
        columns.add(res.get("col.address"));
        columns.add(res.get("col.aluselA"));
        columns.add(res.get("col.aluselB"));
        columns.add(res.get("col.mdrsel"));
        columns.add(res.get("col.memcs"));
        columns.add(res.get("col.memrw"));
        columns.add(res.get("col.aluctrl"));

        for (RegisterExtension baseReg : Main.getWorkspace().getProject().getMachineConfiguration().getBaseRegisters()) {
            columns.add(baseReg.getName()+".W");
        }
        for (RegisterExtension reg : Main.getWorkspace().getProject().getMachineConfiguration().getRegisterExtensions()) {
            columns.add(reg.getName()+".W");
        }

        columns.add(res.get("col.condition"));
        columns.add(res.get("col.jumptarget"));
        columns.add(res.get("col.description"));

        return Collections.unmodifiableList(columns);
    }

    /**
     * Creates an unmodifiable list with all column IDs.
     *
     * @return
     *          an unmodifiable list with all column IDs
     */
    private static List<String> getAllColumnIDs() {
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

        UndoManager.INSTANCE.addCommand(new SignalRowAddedCommand(index, signal));
    }

    /**
     * Deletes the selected row of the {@link TableView} from the {@link SignalTable}.
     */
    public void deleteRow() {
        int index = signaltable.getSelectionModel().getSelectedIndex();

        if (index != -1) {
            UndoManager.INSTANCE.addCommand(new SignalRowRemovedCommand(index, signal));
        }
    }

    /**
     * Moves the currently selected signal row.<br>
     * It moves the row up if the caller is the {@code moveUp} {@link Button} or down if the caller is the {@code moveDown Button}.
     *
     * @param ae
     *          the {@link ActionEvent} calling the method
     */
    // TODO: select multiple rows and move them
    public void moveOperation(ActionEvent ae) {

        if (signaltable.getSelectionModel().getSelectedItems().isEmpty()) {
            return;
        }

        Object caller = ae.getSource();
        int difference;
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

        UndoManager.INSTANCE.addCommand(new SignalRowMovedCommand(index1, difference, signal));

        signaltable.getSelectionModel().select(index1+difference);
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

    @Override
    public void processEvent(MachineConfigEvent event) {
        updateSignalTable();
    }

}
