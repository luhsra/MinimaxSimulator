package de.uni_hannover.sra.minimax_simulator.gui;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.gui.util.MemoryExportWorker;
import de.uni_hannover.sra.minimax_simulator.gui.util.MemoryImportWorker;
import de.uni_hannover.sra.minimax_simulator.gui.util.MemorySpinnerValueFactory;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MemoryState;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.common.dialogs.FXDialog;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.project.memory.components.MemoryUpdateDialog;
import de.uni_hannover.sra.minimax_simulator.util.Util;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO: selection of OPs and showing information

/**
 * FXController of the AluView
 *
 * @author Philipp Rohde
 */
public class AluView {

    private TextResource _res;
    private TextResource _resAlu;

    private MachineConfiguration _config;

    @FXML
    private TitledPane paneAddedOP;
    @FXML
    private TitledPane paneSelectedOP;
    @FXML
    private TitledPane paneAvailableOP;
    @FXML
    private Label lblRT;
    @FXML
    private TextField txtRT;
    @FXML
    private Label lblDescription;
    @FXML
    private TextArea txtDescription;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnRemove;

    @FXML
    private TableView<AddedAluOpTableModel> tableAdded;
    @FXML
    private TableColumn<AddedAluOpTableModel, String> col_added_opcode;
    @FXML
    private TableColumn<AddedAluOpTableModel, String> col_added_op;

    @FXML
    private TableView<AvailableAluOpTableModel> tableAvailable;
    @FXML
    private TableColumn<AvailableAluOpTableModel, String> col_available_op;

    public void initialize() {
        _res = Main.getTextResource("machine");
        _resAlu = Main.getTextResource("alu");

        setLocalizedTexts();
    }

    private void setLocalizedTexts() {
        final List<Labeled> controls = new ArrayList<>(Arrays.asList(paneAddedOP, paneAvailableOP, paneSelectedOP, lblRT, lblDescription, btnAdd, btnRemove));
        for (Labeled con : controls) {
            con.setText(_res.get(con.getId()));
        }
    }

    public void initAluView() {
        _config = Main.getWorkspace().getProject().getMachineConfiguration();
        initAddedTable();
        initAvailableTable();
    }

    private void initAddedTable() {
        col_added_op.setCellValueFactory(new PropertyValueFactory<>("op"));
        col_added_opcode.setCellValueFactory(new PropertyValueFactory<>("opcode"));

        removeTableHeader(tableAdded);
        updateAddedTable();
    }

    private void updateAddedTable() {
        ObservableList<AddedAluOpTableModel> data = FXCollections.observableArrayList();

        for (AluOperation op : _config.getAluOperations()) {
            //TODO: correct OPCode
            data.add(new AddedAluOpTableModel("0", op.getOperationName()));
        }

        tableAdded.setItems(data);
    }

    private void initAvailableTable() {
        col_available_op.setCellValueFactory(new PropertyValueFactory<>("op"));

        removeTableHeader(tableAvailable);
        updateAvailableTable();
    }

    private void updateAvailableTable() {
        ObservableList<AvailableAluOpTableModel> data = FXCollections.observableArrayList();

        for (AluOperation op : AluOperation.values()) {
            if (!_config.getAluOperations().contains(op)) {
                data.add(new AvailableAluOpTableModel(op.getOperationName()));
            }
        }

        tableAvailable.setItems(data);
    }

    private void removeTableHeader(TableView table) {
        Pane header = (Pane) table.lookup("TableHeaderRow");
        header.setMaxHeight(0);
        header.setMinHeight(0);
        header.setPrefHeight(0);
        header.setVisible(false);
    }

    public static class AddedAluOpTableModel {
        private final SimpleStringProperty opcode;
        private final SimpleStringProperty op;

        private AddedAluOpTableModel(String opcode, String op) {
            this.opcode = new SimpleStringProperty(opcode);
            this.op = new SimpleStringProperty(op);
        }

        public String getOpcode() {
            return opcode.get();
        }

        public SimpleStringProperty opcodeProperty() {
            return opcode;
        }

        public void setOpcode(String opcode) {
            this.opcode.set(opcode);
        }

        public String getOp() {
            return op.get();
        }

        public SimpleStringProperty opProperty() {
            return op;
        }

        public void setOp(String op) {
            this.op.set(op);
        }
    }

    public static class AvailableAluOpTableModel {
        private final SimpleStringProperty op;

        private AvailableAluOpTableModel(String op) {
            this.op = new SimpleStringProperty(op);
        }

        public String getOp() {
            return op.get();
        }

        public SimpleStringProperty opProperty() {
            return op;
        }

        public void setOp(String op) {
            this.op.set(op);
        }
    }

}
