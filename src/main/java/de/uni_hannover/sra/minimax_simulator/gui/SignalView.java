package de.uni_hannover.sra.minimax_simulator.gui;

import com.google.common.collect.ImmutableList;
import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.signal.*;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.common.dialogs.FXJumpTargetDialog;
import de.uni_hannover.sra.minimax_simulator.util.Util;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.util.Callback;

import java.util.*;
import java.util.regex.Pattern;

/**
 * <b>FXController of the SignalView</b><br>
 * <br>
 * This controller handles every GUI interaction with the signal table {@link Tab}.
 * The SignalView is the UI of the {@link SignalTable}.
 *
 * @author Philipp Rohde
 */
// TODO: disable / enable the buttons
public class SignalView implements SignalTableListener {

    private final TextResource _res;

    private SignalTable _signal;

    @FXML TableView signaltable;

    /**
     * The constructor initializes the final variables.
     */
    public SignalView() {
        _res = Main.getTextResource("signal");
    }

    /**
     * This method is called during application start up and initializes the SignalView
     * as much as possible without having any project data.
     */
    public void initialize() {
        setLocalizedTexts();
    }

    /**
     * Sets localized texts from resource for the GUI elements.
     */
    private void setLocalizedTexts() {
/*        cols = new ImmutableList.Builder<TableColumn>().addAll(Arrays.asList(col_breakpoint, col_label, col_address, col_alusel_a, col_alusel_b, col_mdrsel, col_memcs, col_memrw, col_aluctrl, col_condition, col_jumptarget, col_description)).build();
        for (TableColumn col : cols) {
            Label lbl = new Label(_res.get(col.getId().replace("_", ".")));
            lbl.setRotate(-90);
            Group grp = new Group(lbl);
            col.setGraphic(grp);
        }           */
/*
        ObservableList<TableColumn<SignalTableModel, ?>> test = signaltable.getColumns();
        for (TableColumn col : test) {
            if (!cols.contains(col)) {
                Label lbl = new Label(col.getText());
                lbl.setRotate(-90);
                Group grp = new Group(lbl);
                col.setText("");
                col.setGraphic(grp);
            }
        }       */
    }

    /**
     * This method is called from the main controller if a new project was created or a opened.
     * It initializes the signal {@link TableView} because it needs project data.
     */
    public void initSignalView() {
        _signal = Main.getWorkspace().getProject().getSignalTable();
        _signal.addSignalTableListener(this);
        signaltable.getColumns().clear();
        updateSignalTable();
    }


    /**
     * Updates the signal table by complete recreation.
     */
    // TODO: create different TableColumn classes
    private void updateSignalTable() {
        signaltable.getItems().clear();
        signaltable.getColumns().clear();

        List<String> columns = new ArrayList<String>();
        columns.add(_res.get("col.breakpoint"));
        columns.add(_res.get("col.label"));
        columns.add(_res.get("col.address"));
        columns.add(_res.get("col.aluselA"));
        columns.add(_res.get("col.aluselB"));
        columns.add(_res.get("col.mdrsel"));
        columns.add(_res.get("col.memcs"));
        columns.add(_res.get("col.memrw"));
        columns.add(_res.get("col.aluctrl"));

        // now all registers
        for (RegisterExtension baseReg : Main.getWorkspace().getProject().getMachineConfiguration().getBaseRegisters()) {
            columns.add(baseReg.getName()+".W");
        }
        for (RegisterExtension reg : Main.getWorkspace().getProject().getMachineConfiguration().getRegisterExtensions()) {
            columns.add(reg.getName()+".W");
        }

        // now the rest
        columns.add(_res.get("col.condition"));
        columns.add(_res.get("col.jumptarget"));
        columns.add(_res.get("col.description"));

        List<String> columnIDs = new ArrayList<String>();
        columnIDs.add("BREAKPOINT");
        columnIDs.add("LABEL");
        columnIDs.add("ADDRESS");
        columnIDs.add("ALU_SELECT_A");
        columnIDs.add("ALU_SELECT_B");
        columnIDs.add("MDR_SEL");
        columnIDs.add("MEM_CS");
        columnIDs.add("MEM_RW");
        columnIDs.add("ALU_CTRL");

        // now all registers
        for (RegisterExtension baseReg : Main.getWorkspace().getProject().getMachineConfiguration().getBaseRegisters()) {
            columnIDs.add(baseReg.getName()+".W");
        }
        for (RegisterExtension reg : Main.getWorkspace().getProject().getMachineConfiguration().getRegisterExtensions()) {
            columnIDs.add(reg.getName()+".W");
        }

        // now the rest
        columnIDs.add("CONDITION");
        columnIDs.add("JUMPTARGET");
        columnIDs.add("DESCRIPTION");
//        TableColumn[] tableColumns = new TableColumn[columns.size()];

        for (int i = 0 ; i < columns.size(); i++) {
            final int j = i;
            TableColumn col = new TableColumn(columns.get(i));
            UIUtil.rotateColumnHeader(col, columns.get(i));
            col.setId(columnIDs.get(i));
            col.setSortable(false);
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    try {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    } catch (Exception e) {
                        return new SimpleStringProperty("");
                    }
                }
            });

            // set special update behavior
            if (i == 0) {
                col.setCellFactory(new Callback<TableColumn<ObservableList, String>, TableCell<ObservableList, String>>() {
                    @Override
                    public TableCell<ObservableList, String> call(TableColumn<ObservableList, String> param) {
                        TableCell<ObservableList, String> cell = new TableCell<ObservableList, String>() {
                            ImageView imageview = new ImageView();

                            @Override
                            public void updateItem(String item, boolean empty) {
                                if (item != null && item.equals("true")) {
                                    GridPane grid = new GridPane();
                                    imageview.setImage(new Image("/images/fugue/control-record.png"));
                                    grid.add(imageview, 0, 0);
                                    ColumnConstraints columnConstraints = new ColumnConstraints();
                                    columnConstraints.setFillWidth(true);
                                    columnConstraints.setHgrow(Priority.ALWAYS);
                                    grid.getColumnConstraints().add(columnConstraints);
                                    RowConstraints rowConstraints = new RowConstraints();
                                    rowConstraints.setFillHeight(true);
                                    rowConstraints.setVgrow(Priority.ALWAYS);
                                    grid.getRowConstraints().add(rowConstraints);
                                    grid.setHalignment(imageview, HPos.CENTER);
                                    grid.setValignment(imageview, VPos.CENTER);
                                    setGraphic(grid);
                                } else {
                                    setGraphic(null);
                                }
                            }
                        };

                        cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if (event.getClickCount() == 2) {
                                    System.out.println("double clicked!");
                                    int index = cell.getTableView().getSelectionModel().getSelectedIndex();
                                    SignalTable signalTable = Main.getWorkspace().getProject().getSignalTable();
                                    SignalRow signalRow = signalTable.getRow(index);

                                    signalRow.setBreakpoint(!signalRow.isBreakpoint());
                                    signalTable.setSignalRow(index, signalRow);

                                    updateSignalTable();
                                }
                            }
                        });

                        return cell;
                    }

                });
            }
            else if (i == 1) {
                col.setCellFactory(new Callback<TableColumn<ObservableList, String>, TableCell<ObservableList, String>>() {
                    @Override
                    public TableCell<ObservableList, String> call(TableColumn<ObservableList, String> param) {
                        TableCell<ObservableList, String> cell = new TableCell<ObservableList, String>(){

                            @Override
                            public void updateItem(String item, boolean empty) {

                                if (item == null) {
                                    setGraphic(null);
                                }
                                else {
                                    GridPane grid = new GridPane();
                                    Label lbl = new Label(item);

                                    grid.add(lbl, 0, 0);
                                    ColumnConstraints columnConstraints = new ColumnConstraints();
                                    columnConstraints.setFillWidth(true);
                                    columnConstraints.setHgrow(Priority.ALWAYS);
                                    grid.getColumnConstraints().add(columnConstraints);
                                    RowConstraints rowConstraints = new RowConstraints();
                                    rowConstraints.setFillHeight(true);
                                    rowConstraints.setVgrow(Priority.ALWAYS);
                                    grid.getRowConstraints().add(rowConstraints);
                                    grid.setHalignment(lbl, HPos.CENTER);
                                    grid.setValignment(lbl, VPos.CENTER);
                                    setGraphic(grid);
                                }
                            }
                        };

                        cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if (event.getClickCount() == 2) {
                                    System.out.println("double clicked!");
                                    int index = cell.getTableView().getSelectionModel().getSelectedIndex();
                                    SignalTable signalTable = Main.getWorkspace().getProject().getSignalTable();
                                    SignalRow signalRow = signalTable.getRow(index);

                                    TextField txtLabel = new TextField(signalRow.getLabel());
/*
                                    txtLabel.textProperty().addListener((observable, oldValue, newValue) -> {
                                        signalRow.setLabel(newValue);
                                        signalTable.setSignalRow(index, signalRow);
                                    });
*/
                                    txtLabel.setOnKeyReleased(new EventHandler<KeyEvent>() {
                                        @Override
                                        public void handle(KeyEvent evt) {

                                            if (KeyCode.ENTER == evt.getCode()) {
                                                signalRow.setLabel(txtLabel.getText());
                                                txtLabel.cancelEdit();
                                                signalTable.setSignalRow(index, signalRow);
                                                //updateSignalTable();

                                            }
                                            else if (KeyCode.ESCAPE == evt.getCode()) {
                                                txtLabel.cancelEdit();
                                                cell.setGraphic(new Label(signalRow.getLabel()));
                                            }
                                        }
                                    });

                                    txtLabel.focusedProperty().addListener(new ChangeListener<Boolean>() {
                                        @Override public void changed(ObservableValue<? extends Boolean> val, Boolean oldVal, Boolean newVal) {

                                            if (!newVal) {
                                                txtLabel.cancelEdit();
                                                cell.setGraphic(new Label(signalRow.getLabel()));
                                            }
                                        }
                                    });


                                    cell.setGraphic(txtLabel);
                                    txtLabel.requestFocus();
                                }
                            }
                        });

                        return cell;
                    }

                });
            }
            else if ( (i >= 3) && (i < columns.size()-3) ) {
                col.setCellFactory(new Callback<TableColumn<ObservableList, String>, TableCell<ObservableList, String>>() {
                    @Override
                    public TableCell<ObservableList, String> call(TableColumn<ObservableList, String> param) {
                        TableCell<ObservableList, String> cell = new TableCell<ObservableList, String>() {

                            @Override
                            public void updateItem(String item, boolean empty) {

                                if (item == null) {
                                    setGraphic(null);
                                }
                                else if (!item.equals("-")) {
                                    GridPane grid = new GridPane();
                                    Label lbl = new Label();

                                    // set text of label according to the current column
                                    MachineConfiguration mConfig = Main.getWorkspace().getProject().getMachineConfiguration();
                                    String newText;
                                    if (col.getId().equals("ALU_SELECT_A")) {
                                        newText = Util.toBinaryAddress(Integer.parseInt(item), mConfig.getMuxSources(MuxType.A).size()-1);
                                    }
                                    else if (col.getId().equals("ALU_SELECT_B")) {
                                        newText = Util.toBinaryAddress(Integer.parseInt(item), mConfig.getMuxSources(MuxType.B).size()-1);
                                    }
                                    else if (col.getId().equals("ALU_CTRL")) {
                                        newText = Util.toBinaryAddress(Integer.parseInt(item), mConfig.getAluOperations().size()-1);
                                    }
                                    else {
                                        newText = item;
                                    }
                                    lbl.setText(newText);

                                    grid.add(lbl, 0, 0);
                                    ColumnConstraints columnConstraints = new ColumnConstraints();
                                    columnConstraints.setFillWidth(true);
                                    columnConstraints.setHgrow(Priority.ALWAYS);
                                    grid.getColumnConstraints().add(columnConstraints);
                                    RowConstraints rowConstraints = new RowConstraints();
                                    rowConstraints.setFillHeight(true);
                                    rowConstraints.setVgrow(Priority.ALWAYS);
                                    grid.getRowConstraints().add(rowConstraints);
                                    grid.setHalignment(lbl, HPos.CENTER);
                                    grid.setValignment(lbl, VPos.CENTER);
                                    setGraphic(grid);
                                } else {
                                    setGraphic(new Label("-"));
                                }
                            }

                        };

                        cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if (event.getClickCount() == 2) {
                                    System.out.println("double clicked!");
                                    int index = cell.getTableView().getSelectionModel().getSelectedIndex();
                                    SignalTable signalTable = Main.getWorkspace().getProject().getSignalTable();
                                    SignalRow signalRow = signalTable.getRow(index);
                                    ComboBox<String> cbRegister = new ComboBox<String>();

                                    ObservableList<String> data = FXCollections.observableArrayList();

                                    // get choices according to table column
                                    String id = cell.getTableColumn().getId();

                                    switch (id) {
                                        case "ALU_SELECT_A":
                                            List<MuxInput> sourcesA = Main.getWorkspace().getProject().getMachineConfiguration().getMuxSources(MuxType.A);

                                            data.add("-");
                                            for (int i = 0; i < sourcesA.size(); i++) {
                                                data.add(Util.toBinaryAddress(i, sourcesA.size()-1) + " " + sourcesA.get(i).getName());
                                            }
                                            break;

                                        case "ALU_SELECT_B":
                                            List<MuxInput> sourcesB = Main.getWorkspace().getProject().getMachineConfiguration().getMuxSources(MuxType.B);

                                            data.add("-");
                                            for (int i = 0; i < sourcesB.size(); i++) {
                                                data.add(Util.toBinaryAddress(i, sourcesB.size()-1) + " " + sourcesB.get(i).getName());
                                            }
                                            break;

                                        case "ALU_CTRL":
                                            List<AluOperation> ops = Main.getWorkspace().getProject().getMachineConfiguration().getAluOperations();

                                            data.add("-");
                                            for (int i = 0; i < ops.size(); i++) {
                                                data.add(Util.toBinaryAddress(i, ops.size()-1) + " " + ops.get(i).getOperationName());
                                            }
                                            break;

                                        case "MDR_SEL":
                                            data.addAll("-", "0 ALU.result", "1 MEM.DO");
                                            break;

                                        case "MEM_CS":
                                            data.addAll("0 ", "1 enable");
                                            break;

                                        case "MEM_RW":
                                            data.addAll("-", "0 write", "1 read");
                                            break;

                                        default:
                                            data.addAll("0 ", "1 write");
                                    }

                                    // get the index of the currently set signal value
                                    int indexCurrentValue;
                                    if (signalRow.getSignalValues().containsKey(id)) {
                                        indexCurrentValue = signalRow.getSignalValue(id) + 1;
                                        if (Pattern.matches(".+\\.W", id)) {
                                            indexCurrentValue -= 1;
                                        }
                                    }
                                    else {
                                        indexCurrentValue = 0;
                                    }


                                    cbRegister.setItems(data);
                                    cbRegister.getSelectionModel().select(indexCurrentValue);

                                    cbRegister.valueProperty().addListener((obs, oldValue, newValue) -> {
                                        if (newValue.startsWith("-")) {
                                            signalRow.setSignal(cell.getTableColumn().getId(), null);
                                        } else {
                                            String binary = newValue.substring(0, newValue.indexOf(" "));
                                            int code = Integer.parseInt(binary, 2);
                                            signalRow.setSignalValue(cell.getTableColumn().getId(), code);
                                        }

                                        DescriptionFactory dFac = signalTable.getDescriptionFactory();
                                        signalRow.setDescription(dFac.createDescription(index, signalRow));
                                        signalTable.setSignalRow(index, signalRow);             // exchange row with itself for notification of SignalTableListeners
                                    });

                                    cbRegister.focusedProperty().addListener((obs, oldValue, newValue) -> {
                                        if (!newValue) {
                                            if (signalRow.getSignalValues().containsKey(id)) {
                                                //cell.setGraphic(new Label(String.valueOf(signalRow.getSignalValue(id))));
                                                // TODO: better update of the graphic
                                                updateSignalTable();
                                            } else {
                                                cell.setGraphic(new Label("-"));
                                            }
                                        }
                                    });
                                    cbRegister.setOnKeyReleased(new EventHandler<KeyEvent>() {
                                        @Override
                                        public void handle(KeyEvent evt) {
                                            if (KeyCode.ESCAPE == evt.getCode()) {
                                                if (signalRow.getSignalValues().containsKey(id)) {
                                                    //cell.setGraphic(new Label(String.valueOf(signalRow.getSignalValue(id))));
                                                    //cell.setText(String.valueOf(signalRow.getSignalValue(id)));
                                                    // TODO: better update of the graphic
                                                    updateSignalTable();
                                                } else {
                                                    cell.setGraphic(new Label("-"));
                                                }
                                            }
                                        }
                                    });

                                    cell.setGraphic(cbRegister);
                                    cbRegister.requestFocus();
                                    cbRegister.show();

                                }
                            }
                        });

                        return cell;
                    }

                });
            }
            else if (i == columns.size()-3 || i == columns.size()-2) {
                col.setCellFactory(new Callback<TableColumn<ObservableList, String>, TableCell<ObservableList, String>>() {
                    @Override
                    public TableCell<ObservableList, String> call(TableColumn<ObservableList, String> param) {
                        TableCell<ObservableList, String> cell = new TableCell<ObservableList, String>() {

                            @Override
                            public void updateItem(String item, boolean empty) {

                                if (item == null) {
                                    setGraphic(null);
                                }
                                else {
                                    GridPane grid = new GridPane();
                                    Label lbl = new Label(item);

                                    grid.add(lbl, 0, 0);
                                    ColumnConstraints columnConstraints = new ColumnConstraints();
                                    columnConstraints.setFillWidth(true);
                                    columnConstraints.setHgrow(Priority.ALWAYS);
                                    grid.getColumnConstraints().add(columnConstraints);
                                    RowConstraints rowConstraints = new RowConstraints();
                                    rowConstraints.setFillHeight(true);
                                    rowConstraints.setVgrow(Priority.ALWAYS);
                                    grid.getRowConstraints().add(rowConstraints);
                                    grid.setHalignment(lbl, HPos.CENTER);
                                    grid.setValignment(lbl, VPos.CENTER);
                                    setGraphic(grid);
                                }
                            }

                        };

                        cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if (event.getClickCount() == 2) {
                                    System.out.println("double clicked!");
                                    int index = cell.getTableView().getSelectionModel().getSelectedIndex();
                                    SignalTable signalTable = Main.getWorkspace().getProject().getSignalTable();
                                    SignalRow signalRow = signalTable.getRow(index);
                                    // TODO: open JumpDialog
                                    new FXJumpTargetDialog(signalTable, signalRow, index).showAndApply();
                                }
                            }
                        });

                        return cell;
                    }

                });
            }

            signaltable.getColumns().addAll(col);
        }

        ObservableList<ObservableList> allRows = FXCollections.observableArrayList();

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
     * Adds a row to the {@link SignalTable}.<br>
     * If now row is selected the new created row will be added to the end
     * else if will be added after the selected row.
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
     * Moves the currently selected signal row.
     * It moves the source up if the caller is the moveUp {@link Button} or down if the caller is the moveDown {@link Button}.
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
