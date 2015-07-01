package de.uni_hannover.sra.minimax_simulator.gui;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.util.Util;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * FXController of the MuxView
 *
 * @author Philipp Rohde
 */
public class MuxView {

    private TextResource _res;
    //private TextResource _resAlu;

    private MachineConfiguration _config;

    @FXML
    private TitledPane paneMuxA;
    @FXML
    private TitledPane paneMuxB;
    @FXML
    private TitledPane paneSelectedConnection;

    @FXML
    private TableView<AddedAluOpTableModel> tableMuxA;
    @FXML
    private TableColumn<AddedAluOpTableModel, String> col_mux_a_code;
    @FXML
    private TableColumn<AddedAluOpTableModel, String> col_mux_a_source;
    @FXML
    private TableColumn<AddedAluOpTableModel, String> col_mux_a_extended;

    @FXML
    private TableView<AddedAluOpTableModel> tableMuxB;
    @FXML
    private TableColumn<AddedAluOpTableModel, String> col_mux_b_code;
    @FXML
    private TableColumn<AddedAluOpTableModel, String> col_mux_b_source;
    @FXML
    private TableColumn<AddedAluOpTableModel, String> col_mux_b_extended;

    @FXML
    private RadioButton radioRegister;
    @FXML
    private ChoiceBox cbRegister;
    @FXML
    private RadioButton radioConstant;
    @FXML
    private Label lblDec;
    @FXML
    private Label lblHex;
    @FXML
    private Spinner spinnerDec;
    @FXML
    private Spinner spinnerHex;

    public void initialize() {
        _res = Main.getTextResource("machine").using("mux");
        setLocalizedTexts();
    }

    private void setLocalizedTexts() {
        paneMuxA.setText(_res.format("table.title", "A"));
        paneMuxB.setText(_res.format("table.title", "B"));
        final List<Labeled> controls = new ArrayList<>(Arrays.asList(paneSelectedConnection, radioRegister, radioConstant, lblDec, lblHex));
        for (Labeled con : controls) {
            con.setText(_res.get(con.getId().replace("_", ".")));
        }
    }

    public void initMuxView() {
        _config = Main.getWorkspace().getProject().getMachineConfiguration();
        initTableMuxA();
        initTableMuxB();
    }

    private void initTableMuxA() {
        // TODO

        removeTableHeader(tableMuxA);
        updateTableMuxA();
    }

    private void updateTableMuxA() {
        ObservableList<AddedAluOpTableModel> data = FXCollections.observableArrayList();

        for (AluOperation op : _config.getAluOperations()) {
            data.add(new AddedAluOpTableModel(op, _config));
        }

        final int size = _config.getAluOperations().size()-1;

        data.forEach((model) -> {
            int pos = _config.getAluOperations().indexOf(model.getAluOP());
            model.setOpcode(Util.toBinaryAddress(pos, size));
        });

        //tableMuxA.setItems(data);
    }

    private void initTableMuxB() {
/*        col_available_op.setCellValueFactory(new PropertyValueFactory<>("op"));

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
        });     */

        removeTableHeader(tableMuxB);
        updateTableMuxB();
    }

    private void updateTableMuxB() {
        ObservableList<AvailableAluOpTableModel> data = FXCollections.observableArrayList();

        for (AluOperation op : AluOperation.values()) {
            if (!_config.getAluOperations().contains(op)) {
                data.add(new AvailableAluOpTableModel(op));
            }
        }

        //tableMuxB.setItems(data);
    }

    private void removeTableHeader(TableView table) {
        Pane header = (Pane) table.lookup("TableHeaderRow");
        header.setMaxHeight(0);
        header.setMinHeight(0);
        header.setPrefHeight(0);
        header.setVisible(false);
    }

    @FXML
    private Button btnMoveUpMuxA;
    @FXML
    private Button btnMoveDownMuxA;
    @FXML
    private Button btnMoveUpMuxB;
    @FXML
    private Button btnMoveDownMuxB;
    @FXML
    private Button btnNewMuxA;
    @FXML
    private Button btnRemoveMuxA;
    @FXML
    private Button btnNewMuxB;
    @FXML
    private Button btnRemoveMuxB;

    public void moveOperation(ActionEvent ae) {
/*
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

        Main.getWorkspace().setProjectUnsaved();    */
    }

    //TODO: change to mux
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
