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
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

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

    private final static String ALU_RESULT = "ALU.result \u2190 ";

    public void initialize() {
        _res = Main.getTextResource("machine");
        _resAlu = Main.getTextResource("alu");
        //txtRT.setFont(new Font("Monospaced", 14));
        txtDescription.setWrapText(true);

        setLocalizedTexts();
    }

    private void setLocalizedTexts() {
        final List<Labeled> controls = new ArrayList<>(Arrays.asList(paneAddedOP, paneAvailableOP, paneSelectedOP, lblRT, lblDescription, btnAdd, btnRemove));
        for (Labeled con : controls) {
            con.setText(_res.get(con.getId().replace("_", ".")));
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

        tableAdded.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        if (!tableAdded.getSelectionModel().getSelectedItems().isEmpty()) {
                            removeOperation();
                        }
                    }
                }
            }
        });

        tableAdded.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tableAvailable.getSelectionModel().clearSelection();
                AluOperation op = newSelection.getAluOP();
                txtRT.setText(ALU_RESULT + op.getRtNotation(_resAlu));
                txtDescription.setText(op.getDescription(_resAlu));

                btnAdd.setDisable(true);
                btnRemove.setDisable(false);
                btnMoveDown.setDisable(false);
                btnMoveUp.setDisable(false);
                if (tableAdded.getSelectionModel().getSelectedIndex() == 0) {
                    btnMoveUp.setDisable(true);
                }
                else if (tableAdded.getSelectionModel().getSelectedIndex() == tableAdded.getItems().size()-1) {
                    btnMoveDown.setDisable(true);
                }
            }
        });

        removeTableHeader(tableAdded);
        updateAddedTable();
    }

    private void updateAddedTable() {
        ObservableList<AddedAluOpTableModel> data = FXCollections.observableArrayList();

        for (AluOperation op : _config.getAluOperations()) {
            data.add(new AddedAluOpTableModel(op, _config));
        }

        final int size = _config.getAluOperations().size()-1;

        data.forEach((model) -> {
            int pos = _config.getAluOperations().indexOf(model.getAluOP());
            model.setOpcode(Util.toBinaryAddress(pos, size));
        });

        tableAdded.setItems(data);
    }

    private void initAvailableTable() {
        col_available_op.setCellValueFactory(new PropertyValueFactory<>("op"));

        tableAvailable.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        if (!tableAvailable.getSelectionModel().getSelectedItems().isEmpty()) {
                            addOperation();
                        }
                    }
                }
            }
        });

        tableAvailable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tableAdded.getSelectionModel().clearSelection();
                AluOperation op = newSelection.getAluOP();
                txtRT.setText(ALU_RESULT + op.getRtNotation(_resAlu));
                txtDescription.setText(op.getDescription(_resAlu));

                btnAdd.setDisable(false);
                btnRemove.setDisable(true);
                btnMoveDown.setDisable(true);
                btnMoveUp.setDisable(true);
            }
        });

        removeTableHeader(tableAvailable);
        updateAvailableTable();
    }

    private void updateAvailableTable() {
        ObservableList<AvailableAluOpTableModel> data = FXCollections.observableArrayList();

        for (AluOperation op : AluOperation.values()) {
            if (!_config.getAluOperations().contains(op)) {
                data.add(new AvailableAluOpTableModel(op));
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

    public void addOperation() {
        AluOperation op = tableAvailable.getSelectionModel().getSelectedItem().getAluOP();
        if (op != null) {
            _config.addAluOperation(op);
            updateAvailableTable();
            updateAddedTable();
            int lastItem = tableAdded.getItems().size()-1;
            tableAdded.getSelectionModel().select(lastItem);
            tableAdded.getSelectionModel().focus(lastItem);

            Main.getWorkspace().setProjectUnsaved();
        }
    }

    public void removeOperation() {
        AluOperation op = tableAdded.getSelectionModel().getSelectedItem().getAluOP();
        if (op != null) {
            _config.removeAluOperation(op);
            updateAddedTable();
            updateAvailableTable();

            int index = -1;
            // lookup the index of the removed operation at tableAvailable
            List<AvailableAluOpTableModel> list = tableAvailable.getItems();
            for (AvailableAluOpTableModel model : list) {
                AluOperation operation = model.getAluOP();
                if (op.equals(operation)) {
                    index = list.indexOf(model);
                }
            }

            tableAvailable.getSelectionModel().select(index);
            tableAvailable.getSelectionModel().focus(index);

            Main.getWorkspace().setProjectUnsaved();
        }
    }

    @FXML
    private Button btnMoveUp;
    @FXML
    private Button btnMoveDown;

    public void moveOperation(ActionEvent ae) {

        if (tableAdded.getSelectionModel().getSelectedItems().isEmpty()) {
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

        int index1 = tableAdded.getSelectionModel().getSelectedIndex();
        int index2 = index1 + difference;
        if (index2 < 0 || index2 >= tableAdded.getItems().size()) {
            return;
        }

        // Move operations in model and adapt selection
        _config.exchangeAluOperations(index1, index2);
        updateAddedTable();
        tableAdded.getSelectionModel().select(index2);
        //_addedTable.getSelectionModel().setSelectionInterval(index2, index2);

        Main.getWorkspace().setProjectUnsaved();
    }

    public static class AddedAluOpTableModel {
        private final SimpleStringProperty opcode;
        private final SimpleStringProperty op;
        private final AluOperation aluOP;

        private AddedAluOpTableModel(AluOperation aluOP, MachineConfiguration config) {
            this.aluOP = aluOP;

            int size = config.getAluOperations().size()-1;
            int pos = config.getAluOperations().indexOf(aluOP);
            this.opcode = new SimpleStringProperty(Util.toBinaryAddress(pos, size));

            this.op = new SimpleStringProperty(aluOP.getOperationName());
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

        public AluOperation getAluOP() {
            return aluOP;
        }
    }

    public static class AvailableAluOpTableModel {
        private final SimpleStringProperty op;
        private final AluOperation aluOP;

        private AvailableAluOpTableModel(AluOperation aluOP) {
            this.aluOP = aluOP;
            this.op = new SimpleStringProperty(aluOP.getOperationName());
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

        public AluOperation getAluOP() {
            return aluOP;
        }
    }

}
