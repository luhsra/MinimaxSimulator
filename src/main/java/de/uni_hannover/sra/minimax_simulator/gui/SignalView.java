package de.uni_hannover.sra.minimax_simulator.gui;

import com.google.common.collect.ImmutableList;
import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.signal.*;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.util.Util;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.StringConverter;

import javax.swing.*;
import javax.swing.table.*;
import java.io.File;
import java.util.*;
import java.util.function.BooleanSupplier;

/**
 * <b>FXController of the MemoryView</b><br>
 * <br>
 * This controller handles every GUI interaction with the memory {@link Tab}.
 * The MemoryView is the UI of the {@link MachineMemory}.
 *
 * @author Philipp Rohde
 */
public class SignalView {

    private final TextResource _res;

    private SignalTable _signal;

    @FXML TableView signaltable;


    private ImmutableList<TableColumn> cols;

    /**
     * The constructor initializes the final variables.
     */
    public SignalView() {
        _res = Main.getTextResource("signal");
    }

    /**
     * This method is called during application start up and initializes the MemoryView
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

    private void rotateColumnHeader(TableColumn col, String text) {
        col.setText("");
        Label lbl = new Label(text);
        lbl.setRotate(-90);
        Group grp = new Group(lbl);
        col.setGraphic(grp);
    }

    /**
     * This method is called from the main controller if a new project was created or a opened.
     * It initializes the memory {@link TableView} and the {@link Spinner}s because they need project data.
     */
    public void initSignalView() {
        _signal = Main.getWorkspace().getProject().getSignalTable();
        signaltable.getColumns().clear();
        updateSignalTable();
    }


    private void updateSignalTable() {
        signaltable.getItems().clear();
        signaltable.getColumns().clear();

        List<String> columns = new ArrayList<String>();
        columns.add(_res.get("col.breakpoint"));
        columns.add("label");
        columns.add("address");
        columns.add("ALUSel.A");
        columns.add("ALUSel.B");
        columns.add("MDR.Sel");
        columns.add("HS CS");
        columns.add("HW RW");
        columns.add("ALU Ctrl");

        // now all registers
        for (RegisterExtension baseReg : Main.getWorkspace().getProject().getMachineConfiguration().getBaseRegisters()) {
            columns.add(baseReg.getName()+".W");
        }
        for (RegisterExtension reg : Main.getWorkspace().getProject().getMachineConfiguration().getRegisterExtensions()) {
            columns.add(reg.getName()+".W");
        }

        // now the rest
        columns.add("ALU == 0?");
        columns.add("Folgebefehl");
        columns.add("Beschreibung");

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
            rotateColumnHeader(col, columns.get(i));
            col.setId(columnIDs.get(i));
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

                                    cbRegister.valueProperty().addListener((obs, oldValue, newValue) -> {
                                        if (newValue.startsWith("-")) {
                                            signalRow.setSignal(cell.getTableColumn().getId(), null);
                                        }
                                        else {
                                            String binary = newValue.substring(0, newValue.indexOf(" "));
                                            int code = Integer.parseInt(binary, 2);
                                            signalRow.setSignalValue(cell.getTableColumn().getId(), code);
                                        }

                                        DescriptionFactory dFac = signalTable.getDescriptionFactory();
                                        signalRow.setDescription(dFac.createDescription(index, signalRow));
                                        signalTable.setSignalRow(index, signalRow);             // exchange row with itself for notification of SignalTableListeners
                                        updateSignalTable();
                                    });

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

                                            for (int i = 0; i < ops.size(); i++) {
                                                data.add(Util.toBinaryAddress(i, ops.size()-1) + " " + ops.get(i).getOperationName());
                                            }
                                            break;

                                        default:
                                            data.addAll("0 ", "1 write");
                                    }

                                    cbRegister.setItems(data);

                                    cell.setGraphic(cbRegister);
                                    cbRegister.show();

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
            SignalRow signalRow = _signal.getRow(i);
            ObservableList<String> row = FXCollections.observableArrayList();
            row.add(String.valueOf(signalRow.isBreakpoint()));
            row.add(signalRow.getLabel());
            row.add(String.valueOf(i));

            Map<String, SignalValue> signalValues = signalRow.getSignalValues();
            ObservableList<TableColumn> cols = signaltable.getColumns();

            for (int j = 3; j < cols.size()-3; j++) {
                String lookUp = cols.get(j).getId();
                if (signalValues.containsKey(lookUp)) {
                    row.add(String.valueOf(signalValues.get(lookUp).intValue()));
                }
                else {
                    row.add("-");
                }
            }

            Jump j = signalRow.getJump();
            if (j.getTargetRow(i, 0) == j.getTargetRow(i, 1)) {
                row.add("-");
            }
            else {
                row.add("1\n0");
            }

            int target0 = j.getTargetRow(i, 0);
            int target1 = j.getTargetRow(i, 1);
            if (target0 == target1) {
                row.add(Integer.toString(target0));
            }
            else {
                row.add(target1 + "\n" + target0);
            }

            row.add(signalRow.getDescription());
            signaltable.getItems().add(row);
        }


        ObservableList<String> row = FXCollections.observableArrayList();
        ObservableList<String> row1 = FXCollections.observableArrayList();
        row.addAll("d1");
        row.addAll("d11");
        //row.addAll("d111");
        row1.addAll("d2");
        row1.addAll("d22");
        //row1.addAll("d222");
//        signaltable.getItems().add(allRows);
//        signaltable.getItems().add(row1);
    }

    public void doSomething() {
        updateSignalTable();
    }

    /**
     * This class represents the table model for the memory {@link TableView}.<br>
     * <br>
     * It stores the address as well as the decimal and hexadecimal value as {@link SimpleStringProperty}.
     *
     * @author Philipp Rohde
     */
    public static class SignalTableModel {
        private final SimpleBooleanProperty breakpoint;
        private final SimpleStringProperty label;
        private final SimpleIntegerProperty address;
        private final SimpleStringProperty description;
        private final SimpleObjectProperty<Map<String, SignalValue>> signalValues;
        private final SimpleStringProperty condition;
        private final SimpleStringProperty jumptarget;

        private static final String _hexFormatString = "0x%08X";

        private SignalTableModel(SignalRow row, int address) {
            this.breakpoint = new SimpleBooleanProperty(row.isBreakpoint());
            this.label = new SimpleStringProperty(row.getLabel());
            this.address = new SimpleIntegerProperty(address);
            this.description = new SimpleStringProperty(row.getDescription());
            this.signalValues = new SimpleObjectProperty<>(row.getSignalValues());

            Jump j = row.getJump();
            if (j.getTargetRow(address, 0) == j.getTargetRow(address, 1)) {
                this.condition = new SimpleStringProperty("-");
            }
            else {
                this.condition = new SimpleStringProperty("1\n0");
            }

            int target0 = j.getTargetRow(address, 0);
            int target1 = j.getTargetRow(address, 1);
            if (target0 == target1) {
                this.jumptarget = new SimpleStringProperty(Integer.toString(target0));
            }
            else {
                this.jumptarget = new SimpleStringProperty(target1 + "\n" + target0);
            }

        }

        public String getLabel() {
            return label.get();
        }

        public SimpleStringProperty labelProperty() {
            return label;
        }

        public void setLabel(String label) {
            this.label.set(label);
        }

        public int getAddress() {
            return address.get();
        }

        public SimpleIntegerProperty addressProperty() {
            return address;
        }

        public boolean getBreakpoint() {
            return breakpoint.get();
        }

        public SimpleBooleanProperty breakpointProperty() {
            return breakpoint;
        }

        public String getDescription() {
            return description.get();
        }

        public SimpleStringProperty descriptionProperty() {
            return description;
        }

        public Map<String, SignalValue> getSignalValues() {
            return signalValues.get();
        }

        public SimpleObjectProperty<Map<String, SignalValue>> signalValuesProperty() {
            return signalValues;
        }

        public String getCondition() {
            return condition.get();
        }

        public SimpleStringProperty conditionProperty() {
            return condition;
        }

        public String getJumptarget() {
            return  jumptarget.get();
        }

        public SimpleStringProperty jumptargetProperty() {
            return jumptarget;
        }
    }

}
