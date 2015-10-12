package de.uni_hannover.sra.minimax_simulator.gui;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <b>FXController of the AluView</b><br>
 * <br>
 * This controller handles every GUI interaction with the ALU {@link Tab}.
 *
 * @author Philipp Rohde
 */
public class AluView {

    private final TextResource _res;
    private final TextResource _resAlu;

    private MachineConfiguration _config;

    @FXML private TitledPane paneAddedOP;
    @FXML private TitledPane paneSelectedOP;
    @FXML private TitledPane paneAvailableOP;
    @FXML private Label lblRT;
    @FXML private TextField txtRT;
    @FXML private Label lblDescription;
    @FXML private TextArea txtDescription;
    @FXML private Button btnAdd;
    @FXML private Button btnRemove;

    @FXML private TableView<AluOpTableModel> tableAdded;
    @FXML private TableColumn<AluOpTableModel, String> col_added_opcode;
    @FXML private TableColumn<AluOpTableModel, String> col_added_op;

    @FXML private TableView<AluOpTableModel> tableAvailable;
    @FXML private TableColumn<AluOpTableModel, String> col_available_op;

    private final static String ALU_RESULT = "ALU.result \u2190 ";

    /**
     * The constructor initializes the final variables.
     */
    public AluView() {
        _res = Main.getTextResource("machine");
        _resAlu = Main.getTextResource("alu");
    }

    /**
     * This method is called during application start up and initializes the AluView
     * as much as possible without having any project data.
     */
    public void initialize() {
        txtDescription.setWrapText(true);

        setLocalizedTexts();
    }

    /**
     * Sets localized texts from resource for the GUI elements.
     */
    private void setLocalizedTexts() {
        final List<Labeled> controls = new ArrayList<>(Arrays.asList(paneAddedOP, paneAvailableOP, paneSelectedOP, lblRT, lblDescription, btnAdd, btnRemove));
        for (Labeled con : controls) {
            con.setText(_res.get(con.getId().replace("_", ".")));
        }
    }

    /**
     * This method is called from the main controller if a new project was created or opened.
     * It initializes the two ALU operation {@link TableView}s because they need project data.
     */
    public void initAluView() {
        _config = Main.getWorkspace().getProject().getMachineConfiguration();
        initAddedTable();
        initAvailableTable();
    }

    /**
     * Initializes the {@link TableView} for the added {@link AluOperation}s.
     */
    private void initAddedTable() {
        col_added_op.setCellValueFactory(new PropertyValueFactory<>("op"));
        col_added_opcode.setCellValueFactory(new PropertyValueFactory<>("opcode"));

        // remove operation with double click
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

        // set selected operation and clear selection of other table
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

        UIUtil.removeTableHeader(tableAdded);
        updateAddedTable();
    }

    /**
     * Updates the {@link TableView} for the added {@link AluOperation}s.
     */
    private void updateAddedTable() {
        ObservableList<AluOpTableModel> data = FXCollections.observableArrayList();

        for (AluOperation op : _config.getAluOperations()) {
            data.add(new AluOpTableModel(op, _config));
        }

        final int size = _config.getAluOperations().size()-1;

        data.forEach((model) -> {
            int pos = _config.getAluOperations().indexOf(model.getAluOP());
            model.setOpcode(Util.toBinaryAddress(pos, size));
        });

        tableAdded.setItems(data);
    }

    /**
     * Initializes the {@link TableView} for the available {@link AluOperation}s.
     */
    private void initAvailableTable() {
        col_available_op.setCellValueFactory(new PropertyValueFactory<>("op"));

        // add operation with double click
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

        // set selected operation and clear selection of other table
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

        UIUtil.removeTableHeader(tableAvailable);
        updateAvailableTable();
    }

    /**
     * Updates the {@link TableView} for the available {@link AluOperation}s.
     */
    private void updateAvailableTable() {
        ObservableList<AluOpTableModel> data = FXCollections.observableArrayList();

        for (AluOperation op : AluOperation.values()) {
            if (!_config.getAluOperations().contains(op)) {
                data.add(new AluOpTableModel(op, _config));
            }
        }

        tableAvailable.setItems(data);
    }

    /**
     * Adds the currently selected available {@link AluOperation} to the list of added {@link AluOperation}s.
     */
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

    /**
     * Removes the currently selected added {@link AluOperation} and adds it to the list of available {@link AluOperation}s.
     */
    public void removeOperation() {
        AluOperation op = tableAdded.getSelectionModel().getSelectedItem().getAluOP();
        if (op != null) {
            _config.removeAluOperation(op);
            updateAddedTable();
            updateAvailableTable();

            int index = -1;
            // lookup the index of the removed operation at tableAvailable
            List<AluOpTableModel> list = tableAvailable.getItems();
            for (AluOpTableModel model : list) {
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

    /**
     * Moves the currently selected operation.
     * It moves the source up if the caller is the moveUp {@link Button} or down if the caller is the moveDown {@link Button}.
     *
     * @param ae
     *          the {@link ActionEvent} calling the method
     */
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

        Main.getWorkspace().setProjectUnsaved();
    }

    /**
     * This class represents the table model for the {@link AluOperation} {@link TableView}s.<br>
     * <br>
     * It stores the {@link AluOperation} as well as the binary operation code and the operation name as {@link SimpleStringProperty}.
     *
     * @author Philipp Rohde
     */
    public static class AluOpTableModel {
        private final SimpleStringProperty opcode;
        private final SimpleStringProperty op;
        private final AluOperation aluOP;

        private AluOpTableModel(AluOperation aluOP, MachineConfiguration config) {
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
