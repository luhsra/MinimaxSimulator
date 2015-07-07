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
 * <b>FXController of the RegView</b><br>
 * <br>
 * This controller handles every GUI interaction with the Register {@link Tab}.
 *
 * @author Philipp Rohde
 */
public class RegView {

    private TextResource _res;
    private TextResource _resAlu;

    private MachineConfiguration _config;

    @FXML
    private TitledPane paneBaseReg;
    @FXML
    private TitledPane paneSelectedReg;
    @FXML
    private TitledPane paneExtendedReg;
    @FXML
    private Label lblName;
    @FXML
    private TextField txtName;
    @FXML
    private Label lblDescription;
    @FXML
    private TextArea txtDescription;
    @FXML
    private Label lblSize;
    @FXML
    private ComboBox cbSize;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnRemove;

    @FXML
    private TableView<AddedAluOpTableModel> tableBaseReg;
    @FXML
    private TableColumn<AddedAluOpTableModel, String> col_base_name;
    @FXML
    private TableColumn<AddedAluOpTableModel, String> col_base_size;

    @FXML
    private TableView<AddedAluOpTableModel> tableExtendedReg;
    @FXML
    private TableColumn<AddedAluOpTableModel, String> col_extended_name;
    @FXML
    private TableColumn<AddedAluOpTableModel, String> col_extended_reg;

    private final static String ALU_RESULT = "ALU.result \u2190 ";

    /**
     * This method is called during application start up and initializes the RegView
     * as much as possible without having any project data.
     */
    public void initialize() {
        _res = Main.getTextResource("machine").using("register");
        _resAlu = Main.getTextResource("alu");
        txtDescription.setWrapText(true);

        setLocalizedTexts();
    }

    /**
     * Sets localized texts from resource for the GUI elements.
     */
    private void setLocalizedTexts() {
        final List<Labeled> controls = new ArrayList<>(Arrays.asList(paneBaseReg, paneExtendedReg, paneSelectedReg, lblName, lblDescription, btnSave, btnRemove, btnAdd, lblSize));
        for (Labeled con : controls) {
            con.setText(_res.get(con.getId().replace("_", ".")));
        }
    }

    /**
     * This method is called from the main controller if a new project was created or a opened.
     * It initializes the two register {@link TableView}s because they need project data.
     */
    public void initRegView() {
        _config = Main.getWorkspace().getProject().getMachineConfiguration();
        initBaseTable();
        initExtendedTable();
    }

    /**
     * Initializes the {@link TableView} for the base registers.
     */
    private void initBaseTable() {
        col_base_size.setCellValueFactory(new PropertyValueFactory<>("op"));
        col_base_name.setCellValueFactory(new PropertyValueFactory<>("opcode"));

        // remove operation with double click
        tableBaseReg.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        if (!tableBaseReg.getSelectionModel().getSelectedItems().isEmpty()) {
                            removeOperation();
                        }
                    }
                }
            }
        });

        // set selected operation and clear selection of other table
        tableBaseReg.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tableExtendedReg.getSelectionModel().clearSelection();
                AluOperation op = newSelection.getAluOP();
                txtName.setText(ALU_RESULT + op.getRtNotation(_resAlu));
                txtDescription.setText(op.getDescription(_resAlu));

                btnSave.setDisable(true);
                btnRemove.setDisable(false);
                btnMoveDown.setDisable(false);
                btnMoveUp.setDisable(false);
                if (tableBaseReg.getSelectionModel().getSelectedIndex() == 0) {
                    btnMoveUp.setDisable(true);
                }
                else if (tableBaseReg.getSelectionModel().getSelectedIndex() == tableBaseReg.getItems().size()-1) {
                    btnMoveDown.setDisable(true);
                }
            }
        });

        removeTableHeader(tableBaseReg);
        updateAddedTable();
    }

    /**
     * Updates the {@link TableView} for the base registers.
     */
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

        tableBaseReg.setItems(data);
    }

    /**
     * Initializes the {@link TableView} for the extended registers.
     */
    private void initExtendedTable() {
        col_extended_name.setCellValueFactory(new PropertyValueFactory<>("op"));

        // add operation with double click
        tableExtendedReg.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        if (!tableExtendedReg.getSelectionModel().getSelectedItems().isEmpty()) {
                            addOperation();
                        }
                    }
                }
            }
        });

        // set selected operation and clear selection of other table
        tableExtendedReg.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tableBaseReg.getSelectionModel().clearSelection();
                AluOperation op = newSelection.getAluOP();
                txtName.setText(ALU_RESULT + op.getRtNotation(_resAlu));
                txtDescription.setText(op.getDescription(_resAlu));

                btnSave.setDisable(false);
                btnRemove.setDisable(true);
                btnMoveDown.setDisable(true);
                btnMoveUp.setDisable(true);
            }
        });

        removeTableHeader(tableExtendedReg);
        updateAvailableTable();
    }

    /**
     * Updates the {@link TableView} for the extended registers.
     */
    private void updateAvailableTable() {
        ObservableList<AddedAluOpTableModel> data = FXCollections.observableArrayList();

        for (AluOperation op : AluOperation.values()) {
            if (!_config.getAluOperations().contains(op)) {
                data.add(new AddedAluOpTableModel(op, null));
            }
        }

        tableExtendedReg.setItems(data);
    }

    /**
     * Removes the table header of a {@link TableView} by looking up the TableHeaderRow and making it invisible.
     *
     * @param table
     *          the {@link TableView} for that the header should be removed
     */
    //TODO: move to something like tableUtils
    private void removeTableHeader(TableView table) {
        Pane header = (Pane) table.lookup("TableHeaderRow");
        header.setMaxHeight(0);
        header.setMinHeight(0);
        header.setPrefHeight(0);
        header.setVisible(false);
    }

    /**
     * Adds the currently selected available {@link AluOperation} to the list of added {@link AluOperation}s.
     */
    public void addOperation() {
        AluOperation op = tableExtendedReg.getSelectionModel().getSelectedItem().getAluOP();
        if (op != null) {
            _config.addAluOperation(op);
            updateAvailableTable();
            updateAddedTable();
            int lastItem = tableBaseReg.getItems().size()-1;
            tableBaseReg.getSelectionModel().select(lastItem);
            tableBaseReg.getSelectionModel().focus(lastItem);

            Main.getWorkspace().setProjectUnsaved();
        }
    }

    /**
     * Removes the currently selected added {@link AluOperation} and adds it to the list of available {@link AluOperation}s.
     */
    public void removeOperation() {
/*        AluOperation op = tableBaseReg.getSelectionModel().getSelectedItem().getAluOP();
        if (op != null) {
            _config.removeAluOperation(op);
            updateAddedTable();
            updateAvailableTable();

            int index = -1;
            // lookup the index of the removed operation at tableExtendedReg
            List<A> list = tableExtendedReg.getItems();
            for (AvailableAluOpTableModel model : list) {
                AluOperation operation = model.getAluOP();
                if (op.equals(operation)) {
                    index = list.indexOf(model);
                }
            }

            tableExtendedReg.getSelectionModel().select(index);
            tableExtendedReg.getSelectionModel().focus(index);

            Main.getWorkspace().setProjectUnsaved();
        }           */
    }

    @FXML
    private Button btnMoveUp;
    @FXML
    private Button btnMoveDown;

    /**
     * Moves the currently selected operation.
     * It moves the source up if the caller is the moveUp {@link Button} or down if the caller is the moveDown {@link Button}.
     *
     * @param ae
     *          the {@link ActionEvent} calling the method
     */
    public void moveOperation(ActionEvent ae) {

        if (tableBaseReg.getSelectionModel().getSelectedItems().isEmpty()) {
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

        int index1 = tableBaseReg.getSelectionModel().getSelectedIndex();
        int index2 = index1 + difference;
        if (index2 < 0 || index2 >= tableBaseReg.getItems().size()) {
            return;
        }

        // Move operations in model and adapt selection
        _config.exchangeAluOperations(index1, index2);
        updateAddedTable();
        tableBaseReg.getSelectionModel().select(index2);
        //_addedTable.getSelectionModel().setSelectionInterval(index2, index2);

        Main.getWorkspace().setProjectUnsaved();
    }

    /**
     * This class represents the table model for the register {@link TableView}s.<br>
     * <br>
     * It stores the {@link AluOperation} as well as the binary operation code and the operation name as {@link SimpleStringProperty}.
     *
     * @author Philipp Rohde
     */
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

}
