package de.uni_hannover.sra.minimax_simulator.gui;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MemoryState;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.Simulation;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.SimulationListener;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.SimulationState;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.Trackable;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.project.memory.components.MemoryUpdateDialog;
import de.uni_hannover.sra.minimax_simulator.util.Util;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


/**
 * <b>FXController of the DebuggerView</b><br>
 * <br>
 * This controller handles every GUI interaction with the debugger {@link Tab}.
 * The DebuggerView is the part of the UI for simulating the Machine.
 *
 * @author Philipp Rohde
 */
//TODO: change value of registers
//TODO: set breakpoints
public class DebuggerView implements SimulationListener {

    private String _addressFormatString;
    private static MachineMemory mMemory;

    private final int _pageSize = 16;
    private int _pageCount;
    private int	_page;
    private int	_cachedPageStart;

    private static FileChooser fc;

    private TextResource _resSignal;
    private TextResource _resMem;
    private TextResource _res;

    @FXML private TableView<MemoryTableModel> memTable;

    @FXML private TableColumn<MemoryTableModel, String> col_mem_adr;
    @FXML private TableColumn<MemoryTableModel, String> col_mem_dec;
    @FXML private TableColumn<MemoryTableModel, String> col_mem_hex;

    @FXML private TableView<RegisterTableModel> regTable;
    @FXML private TableColumn<RegisterTableModel, String> col_reg_name;
    @FXML private TableColumn<RegisterTableModel, String> col_reg_dec;
    @FXML private TableColumn<RegisterTableModel, String> col_reg_hex;

    @FXML private TableView<AluTableModel> aluTable;
    @FXML private TableColumn<AluTableModel, String> col_alu_dec;
    @FXML private TableColumn<AluTableModel, String> col_alu_hex;


    @FXML private TableView<SimulationTableModel> simTable;
    @FXML private TableColumn<SimulationTableModel, Boolean> col_sim_1;
    @FXML private TableColumn<SimulationTableModel, Boolean> col_sim_2;
    @FXML private TableColumn<SimulationTableModel, String> col_sim_label;
    @FXML private TableColumn<SimulationTableModel, String> col_sim_adr;
    @FXML private TableColumn<SimulationTableModel, String> col_sim_alu;
    @FXML private TableColumn<SimulationTableModel, String> col_sim_next;
    @FXML private TableColumn<SimulationTableModel, String> col_sim_desc;

    private Simulation _simulation;

    /**
     * This method is called during application start up and initializes the DebuggerView
     * as much as possible without having any project data.
     */
    public void initialize() {
        _resSignal = Main.getTextResource("signal");
        _resMem = Main.getTextResource("project");
        _res = Main.getTextResource("debugger");

        _page = _cachedPageStart = 0;

        txtAddressField.textProperty().addListener((observable, oldValue, newValue) -> {
            String text = newValue.trim();
            if (text.isEmpty()) {
                return;
            }
            if (text.startsWith("0x")) {
                text = text.substring(2);
            }
            try {
                int value = Integer.parseInt(text, 16);
                System.out.println("select address: " + value);
                selectAddress(value);
            } catch (NumberFormatException nfe) {
                // ignore malformed input
            }
        });

        fc = new FileChooser();

        setLocalizedTexts();
    }

    @FXML private TitledPane paneMemory;
    @FXML private TitledPane paneRegister;
    @FXML private TitledPane paneALU;
    @FXML private TitledPane paneSimulation;
    @FXML private Label lblCycles;

    /**
     * Sets localized texts from resource for the GUI elements.
     */
    private void setLocalizedTexts() {
        final List<TableColumn> tableColumnsMem = new ArrayList<>(Arrays.asList(col_mem_adr, col_mem_dec, col_mem_hex));
        for (TableColumn col : tableColumnsMem) {
            col.setText(_resMem.get(col.getId().replace("_", ".")));
        }
        paneMemory.setText(_resMem.get(paneMemory.getId().replace("_", ".")));

        final List<TableColumn> tableColumnsSignal = new ArrayList<>(Arrays.asList(col_sim_label, col_sim_adr, col_sim_alu, col_sim_next, col_sim_desc));
        for (TableColumn col : tableColumnsSignal) {
            col.setText(_resSignal.get(col.getId().replace("_", ".")));
        }

        final List<TableColumn> tableColumns = new ArrayList<>(Arrays.asList(col_reg_name, col_reg_dec, col_reg_hex, col_alu_dec, col_alu_hex));
        for (TableColumn col : tableColumns) {
            col.setText(_res.get(col.getId().replace("_", ".")));
        }

        final List<Labeled> controls = new ArrayList<>(Arrays.asList(paneRegister, paneALU, paneSimulation));
        for (Labeled con : controls) {
            con.setText(_res.get(con.getId().replace("_", ".")));
        }

        lblCycles.setText(_res.format(lblCycles.getId().replace("_", "."), "---"));
    }

    /**
     * This method is called from the main controller if a new project was created or a opened.
     * It initializes the {@link TableView}s because they need project data.
     */
    public void initDebuggerView() {
        mMemory = Main.getWorkspace().getProject().getMachine().getMemory();
        _simulation = Main.getWorkspace().getProject().getSimulation();
        _simulation.addSimulationListener(this);
        _addressFormatString = Util.createHexFormatString(mMemory.getAddressWidth(), false);

        _cyclesFormatHalted = _res.createFormat("cycles.label");
        _cyclesFormatRead = _res.createFormat("cycles.read.label");
        _cyclesFormatWrite = _res.createFormat("cycles.write.label");

        initMemTable();
        initRegTable();
        initAluTable();
        initSimulationTable();
    }

    /**
     * Calls the update method for each {@link TableView}.
     */
    private void updateAllTables() {
        updateMemTable();
        updateAluTable();
        updateRegTable();
        updateSimulationTable();
    }

    private void selectAddress(int address) {
        int page;
        int row;
        if (address < 0) {
            page = 0;
            row = 0;
        }
        else {
            if (address > mMemory.getMaxAddress()) {
                address = mMemory.getMaxAddress();
            }

            page = address / _pageSize;
            if (page >= _pageCount) {
                page = _pageCount - 1;
                row = _pageSize - 1;
            }
            else {
                row = address % _pageSize;
            }
        }

        setPage(page);

        if (row != memTable.getSelectionModel().getSelectedIndex()) {
            memTable.getSelectionModel().select(row);
        }
    }

    /**
     * Initializes the {@link TableView} for the registers.
     */
    private void initRegTable() {
        col_reg_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_reg_dec.setCellValueFactory(new PropertyValueFactory<>("decimal"));
        col_reg_hex.setCellValueFactory(new PropertyValueFactory<>("hex"));

        updateRegTable();
    }

    /**
     * Initializes the {@link TableView} for the ALU result.
     */
    private void initAluTable() {
        col_alu_dec.setCellValueFactory(new PropertyValueFactory<>("decimal"));
        col_alu_hex.setCellValueFactory(new PropertyValueFactory<>("hex"));

        updateAluTable();
    }

    /**
     * Updates the {@link TableView} for the registers.
     */
    private void updateRegTable() {
        ObservableList<RegisterTableModel> data = FXCollections.observableArrayList();
        List<RegisterExtension> registers = new ArrayList<>();
        registers.addAll(Main.getWorkspace().getProject().getMachineConfiguration().getBaseRegisters());
        registers.addAll(Main.getWorkspace().getProject().getMachineConfiguration().getRegisterExtensions());

        for (RegisterExtension register : registers) {
            String formatString = register.getSize().getHexFormat();

            String name = register.getName();
            String decimal;
            String hex;
            if (_simulation.getState().equals(SimulationState.OFF)) {
                decimal = "--";
                hex = "--";
            }
            else {
                Trackable<Integer> value = _simulation.getRegisterValue(name);
                decimal = Integer.toString(value.get().intValue());
                hex = String.format(formatString, value.get().intValue());
            }

            data.add(new RegisterTableModel(name, decimal, hex));

        }

        regTable.setItems(data);
    }

    /**
     * Initializes the {@link TableView} for the simulation overview.
     */
    private void initSimulationTable() {
        col_sim_1.setCellValueFactory(new PropertyValueFactory<>("active"));
        col_sim_2.setCellValueFactory(new PropertyValueFactory<>("breakpoint"));
        col_sim_label.setCellValueFactory(new PropertyValueFactory<>("label"));
        col_sim_adr.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_sim_alu.setCellValueFactory(new PropertyValueFactory<>("alu"));
        col_sim_next.setCellValueFactory(new PropertyValueFactory<>("next"));
        col_sim_desc.setCellValueFactory(new PropertyValueFactory<>("description"));

        col_sim_2.setCellFactory(new Callback<TableColumn<SimulationTableModel, Boolean>, TableCell<SimulationTableModel, Boolean>>() {
            @Override
            public TableCell<SimulationTableModel, Boolean> call(TableColumn<SimulationTableModel, Boolean> param) {
                TableCell<SimulationTableModel, Boolean> cell = new TableCell<SimulationTableModel, Boolean>() {
                    ImageView imageview = new ImageView();

                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        if (item != null && item == true) {
                            GridPane grid = new GridPane();
                            imageview.setImage(new Image("/images/fugue/control-record.png"));
                            grid.add(imageview, 0, 0);
                            ColumnConstraints columnConstraints = new ColumnConstraints();
                            columnConstraints.setFillWidth(true);
                            columnConstraints.setHgrow(Priority.ALWAYS);
                            grid.getColumnConstraints().add(columnConstraints);
                            grid.setHalignment(imageview, HPos.CENTER);
                            grid.setValignment(imageview, VPos.CENTER);
                            setGraphic(grid);
                        }
                        else {
                            setGraphic(null);
                        }
                    }
                };
                return cell;
            }

        });

        col_sim_1.setCellFactory(new Callback<TableColumn<SimulationTableModel, Boolean>, TableCell<SimulationTableModel, Boolean>>() {
            @Override
            public TableCell<SimulationTableModel, Boolean> call(TableColumn<SimulationTableModel, Boolean> param) {
                TableCell<SimulationTableModel, Boolean> cell = new TableCell<SimulationTableModel, Boolean>() {
                    ImageView imageview = new ImageView();

                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        if (item != null && item == true) {
                            GridPane grid = new GridPane();
                            imageview.setImage(new Image("/images/fugue/arrow-curve-000-left.png"));
                            grid.add(imageview, 0, 0);
                            ColumnConstraints columnConstraints = new ColumnConstraints();
                            columnConstraints.setFillWidth(true);
                            columnConstraints.setHgrow(Priority.ALWAYS);
                            grid.getColumnConstraints().add(columnConstraints);
                            grid.setHalignment(imageview, HPos.CENTER);
                            grid.setValignment(imageview, VPos.CENTER);
                            setGraphic(grid);
                        }
                        else {
                            setGraphic(null);
                        }
                    }
                };
                return cell;
            }

        });

        updateSimulationTable();
    }

    private final static int	NO_ROW_MARKED	= -1;
    private static int			_lastExecutedRow = -1;

    /**
     * Gets the index of the last executed row of the simulation.
     *
     * @return
     *          the index of the last executed row
     */
    public static int getLastExecutedRow() {
        return _lastExecutedRow;
    }

    /**
     * Updates the {@link TableView} for the simulation overview.
     */
    private void updateSimulationTable() {
        ObservableList<SimulationTableModel> data = FXCollections.observableArrayList();

        SignalTable signalTable = Main.getWorkspace().getProject().getSignalTable();
        for (int i = 0; i < signalTable.getRowCount(); i++) {
            data.add(new SimulationTableModel(signalTable.getRow(i), i));
        }

        simTable.setItems(data);
        System.out.println("Height of SimTable: " + simTable.getHeight());
    }

    /**
     * Updates the {@link TableView} for the ALU result.
     */
    private void updateAluTable() {
        ObservableList<AluTableModel> data = FXCollections.observableArrayList();

        if (_simulation.getState().equals(SimulationState.OFF)) {
           data.add(new AluTableModel(null));
        }
        else {
            data.add(new AluTableModel(_simulation.getAluResult().get()));
        }

        aluTable.setItems(data);
    }

    @FXML private TextField txtAddressField;

    /**
     * Initializes the {@link TableView} for the memory.
     */
    private void initMemTable() {
        System.out.println("initializing memory table");
        int addressRange = mMemory.getMaxAddress() - mMemory.getMinAddress();
        _pageCount = (addressRange - 1) / _pageSize + 1;

        col_mem_adr.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_mem_dec.setCellValueFactory(new PropertyValueFactory<>("decimal"));
        col_mem_hex.setCellValueFactory(new PropertyValueFactory<>("hex"));

        updateMemTable();

        // open edit dialog at double click
        memTable.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        System.out.println("Double clicked");
                        int address = memTable.getSelectionModel().getSelectedIndex() + _cachedPageStart;
                        // open edit dialog
                        Optional<ButtonType> result = new MemoryUpdateDialog(address, mMemory).showAndWait();
                        if (result.get() == ButtonType.OK) {
                            updateMemTable();
                        }
                    }
                }
            }
        });

        // set next/previous page at scroll
        memTable.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                double deltaY = scrollEvent.getDeltaY();
                if (deltaY > 0) {
                    prevPage();
                } else {
                    nextPage();
                }
            }
        });
    }

    /**
     * Updates the {@link TableView} for the memory.
     */
    private void updateMemTable() {
        ObservableList<MemoryTableModel> data = FXCollections.observableArrayList();

        MemoryState mState = mMemory.getMemoryState();
        for (int i = 0; i < _pageSize; i++) {
            int value = mState.getInt(_cachedPageStart + i);
            data.add(new MemoryTableModel(String.format(_addressFormatString, _cachedPageStart + i), String.valueOf(value), String.format("0x%08X", value)));
        }

        memTable.setItems(data);
    }

    @FXML private Button btnNextPage;

    /**
     * Sets the next memory page to the {@link TableView}.
     */
    public void nextPage() {
        this.setPage(_page+1);
    }

    @FXML Button btnPrevPage;

    /**
     * Sets the previous memory page to the {@link TableView}.
     */
    public void prevPage() {
        this.setPage(_page - 1);
    }

    @FXML Button btnFirstPage;

    /**
     * Sets the first memory page to the {@link TableView}.
     */
    public void firstPage() {
        this.setPage(0);
    }

    @FXML Button btnLastPage;

    /**
     * Sets the last memory page to the {@link TableView}.
     */
    public void lastPage() {
        this.setPage(_pageCount - 1);
    }

    /**
     * Sets the memory page with the given page number to the {@link TableView}.
     *
     * @param newPage
     *          the number of the new page
     */
    private void setPage(int newPage) {
        if (newPage >= _pageCount || newPage < 0) {
            return;
        }
        _page = newPage;
        _cachedPageStart = _page * _pageSize + mMemory.getMinAddress();

        updateMemTable();
    }

    @Override
    public void stateChanged(SimulationState state) {
        System.out.println("DEBUG: state changed");
        if (state == SimulationState.IDLE) {
            System.out.println("DEBUG: state now is IDLE");
            // simulation has done a step
            _lastExecutedRow = _simulation.getCurrentSignalRow();
        }
        else if (state == SimulationState.HALTED) {
            System.out.println("DEBUG: state now is HALTED");
            // simulation ended
            if (_lastExecutedRow != NO_ROW_MARKED) {
                _lastExecutedRow = NO_ROW_MARKED;
            }
            btnSimCycle.setDisable(true);
            btnSimRun.setDisable(true);
        }
        else if (state == SimulationState.OFF) {
            System.out.println("DEBUG: state now is OFF");
            // simulation canceled
            if (_lastExecutedRow != NO_ROW_MARKED) {
                _lastExecutedRow = NO_ROW_MARKED;
            }
        }
        System.out.println("DEBUG: lastExecutedRow: " + _lastExecutedRow);
    }

    @FXML private Button btnSimInit;
    @FXML private Button btnSimQuit;
    @FXML private Button btnSimCycle;
    @FXML private Button btnSimRun;

    /**
     * Initializes the simulation of the machine.
     */
    public void initSimulation() {
        if (_simulation.getState().equals(SimulationState.OFF)) {
            _simulation.init();
            btnSimQuit.setDisable(false);
            btnSimCycle.setDisable(false);
            btnSimRun.setDisable(false);
            btnSimInit.setGraphic(new ImageView("/images/fugue/arrow-circle-225-red.png"));
        }
        else {
            _simulation.reset();
            btnSimCycle.setDisable(false);
            btnSimRun.setDisable(false);
        }

        updateAllTables();
        updateCyclesText();
    }

    /**
     * Stops the simulation of the machine and resets it.
     */
    public void quitSimulation() {
        _simulation.stop();
        btnSimQuit.setDisable(true);
        btnSimCycle.setDisable(true);
        btnSimRun.setDisable(true);
        btnSimInit.setGraphic(new ImageView("/images/fugue/control-green.png"));

        updateAllTables();
        updateCyclesText();
    }

    /**
     * Simulates the next cycle of the machine.
     */
    public void nextCycle() {
        _simulation.step();
        updateAllTables();
        updateCyclesText();
    }

    /**
     * Simulates the machine until it reaches a breakpoint or the end of the program.
     */
    public void runSimulation() {
        UIUtil.executeWorker(new Runnable() {
                                 // background task
                                 @Override
                                 public void run() {
                                     _simulation.run();
                                 }
                             }, _res.get("simulation.wait.title"), _res.get("simulation.wait.message"),
                new Runnable() {
                    @Override
                    public void run() {
                        // on cancel
                        _simulation.pause();
                    }
                });
        updateAllTables();
        updateCyclesText();
    }

    private MessageFormat _cyclesFormatHalted;
    private MessageFormat	_cyclesFormatRead;
    private MessageFormat	_cyclesFormatWrite;
    private Object[]		_cyclesFormatParam	= new Object[1];

    /**
     * Updates the cycle {@link Label} with the current cycle.
     */
    private void updateCyclesText() {
        String text;
        if (_simulation.getState() == SimulationState.OFF)
        {
            _cyclesFormatParam[0] = "---";
            text = _cyclesFormatHalted.format(_cyclesFormatParam);
        }
        else
        {
            MessageFormat format = _simulation.isResolved() ? _cyclesFormatWrite : _cyclesFormatRead;
            _cyclesFormatParam[0] = Integer.valueOf(_simulation.getCyclesCount());
            text = format.format(_cyclesFormatParam);
        }
        lblCycles.setText(text);
    }

    /**
     * This class represents the table model for the memory {@link TableView}.<br>
     * <br>
     * It stores the address as well as the decimal and hexadecimal value as {@link SimpleStringProperty}.
     *
     * @author Philipp Rohde
     */
    public static class MemoryTableModel {
        private final SimpleStringProperty address;
        private final SimpleStringProperty decimal;
        private final SimpleStringProperty hex;

        private MemoryTableModel(String address, String decimal, String hex) {
            this.address = new SimpleStringProperty(address);
            this.decimal = new SimpleStringProperty(decimal);
            this.hex = new SimpleStringProperty(hex);
        }

        public String getAddress() {
            return address.get();
        }

        public SimpleStringProperty addressProperty() {
            return address;
        }

        public void setAddress(String address) {
            this.address.set(address);
        }

        public String getDecimal() {
            return decimal.get();
        }

        public SimpleStringProperty decimalProperty() {
            return decimal;
        }

        public void setDecimal(String decimal) {
            this.decimal.set(decimal);
        }

        public String getHex() {
            return hex.get();
        }

        public SimpleStringProperty hexProperty() {
            return hex;
        }

        public void setHex(String hex) {
            this.hex.set(hex);
        }
    }

    /**
     * This class represents the table model for the register {@link TableView}.<br>
     * <br>
     * It stores the name as well as the decimal and hexadecimal value as {@link SimpleStringProperty}.
     *
     * @author Philipp Rohde
     */
    public static class RegisterTableModel {
        private final SimpleStringProperty name;
        private final SimpleStringProperty decimal;
        private final SimpleStringProperty hex;

        private RegisterTableModel(String name, String decimal, String hex) {
            this.name = new SimpleStringProperty(name);
            this.decimal = new SimpleStringProperty(decimal);
            this.hex = new SimpleStringProperty(hex);
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getDecimal() {
            return decimal.get();
        }

        public SimpleStringProperty decimalProperty() {
            return decimal;
        }

        public void setDecimal(String decimal) {
            this.decimal.set(decimal);
        }

        public String getHex() {
            return hex.get();
        }

        public SimpleStringProperty hexProperty() {
            return hex;
        }

        public void setHex(String hex) {
            this.hex.set(hex);
        }

    }

    /**
     * This class represents the table model for the ALU result {@link TableView}.<br>
     * <br>
     * It stores the decimal and hexadecimal value as {@link SimpleStringProperty}.
     *
     * @author Philipp Rohde
     */
    public static class AluTableModel {
        private final SimpleStringProperty decimal;
        private final SimpleStringProperty hex;

        private AluTableModel (Integer value) {
            if (value == null) {
                this.decimal = new SimpleStringProperty("--");
                this.hex = new SimpleStringProperty("--");
            }
            else {
                this.decimal = new SimpleStringProperty(String.valueOf(value));
                this.hex = new SimpleStringProperty(String.format("0x%08X", value));
            }
        }

        public String getDecimal() {
            return decimal.get();
        }

        public SimpleStringProperty decimalProperty() {
            return decimal;
        }

        public void setDecimal(String decimal) {
            this.decimal.set(decimal);
        }

        public String getHex() {
            return hex.get();
        }

        public SimpleStringProperty hexProperty() {
            return hex;
        }

        public void setHex(String hex) {
            this.hex.set(hex);
        }
    }

    /**
     * This class represents the table model for the simulation {@link TableView}.<br>
     * <br>
     * It stores the label as well as the address, ALU condition, jump target and description as {@link SimpleStringProperty}.
     * If the row is active and/or a breakpoint is stored as {@link SimpleBooleanProperty}.
     *
     * @author Philipp Rohde
     */
    public static class SimulationTableModel {
        private final SimpleStringProperty label;
        private final SimpleStringProperty address;
        private final SimpleStringProperty alu;
        private final SimpleStringProperty next;
        private final SimpleStringProperty description;
        private final SimpleBooleanProperty breakpoint;
        private final SimpleBooleanProperty active;

        private SimulationTableModel(SignalRow row, int rowIndex) {
            if (row.isBreakpoint()) {
                breakpoint = new SimpleBooleanProperty(true);
            }
            else {
                breakpoint = new SimpleBooleanProperty(false);
            }

            if (rowIndex == DebuggerView.getLastExecutedRow()) {
                active = new SimpleBooleanProperty(true);
            }
            else {
                active = new SimpleBooleanProperty(false);
            }

            this.label = new SimpleStringProperty(row.getLabel());
            this.address = new SimpleStringProperty(String.valueOf(rowIndex));

            Jump j = row.getJump();
            if (j.getTargetRow(rowIndex, 0) == j.getTargetRow(rowIndex, 1)) {
                this.alu = new SimpleStringProperty("-");
            }
            else {
                this.alu = new SimpleStringProperty("1\n0");
            }

            int target0 = j.getTargetRow(rowIndex, 0);
            int target1 = j.getTargetRow(rowIndex, 1);
            if (target0 == target1) {
                this.next = new SimpleStringProperty(Integer.toString(target0));
            }
            else {
                this.next = new SimpleStringProperty(target1 + "\n" + target0);
            }

            this.description = new SimpleStringProperty(row.getDescription());
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

        public String getAddress() {
            return address.get();
        }

        public SimpleStringProperty addressProperty() {
            return address;
        }

        public void setAddress(String address) {
            this.address.set(address);
        }

        public String getAlu() {
            return alu.get();
        }

        public SimpleStringProperty aluProperty() {
            return alu;
        }

        public void setAlu(String alu) {
            this.alu.set(alu);
        }

        public String getNext() {
            return next.get();
        }

        public SimpleStringProperty nextProperty() {
            return next;
        }

        public void setNext(String next) {
            this.next.set(next);
        }

        public String getDescription() {
            return description.get();
        }

        public SimpleStringProperty descriptionProperty() {
            return description;
        }

        public void setDescription(String description) {
            this.description.set(description);
        }

        public Boolean getBreakpoint() {
            return breakpoint.get();
        }

        public SimpleBooleanProperty breakpointProperty() {
            return breakpoint;
        }

        public void setBreakpoint(Boolean breakpoint) {
            this.breakpoint.set(breakpoint);
        }

        public Boolean getActive() {
            return active.get();
        }

        public SimpleBooleanProperty activeProperty() {
            return active;
        }

        public void setActive(Boolean active) {
            this.active.set(active);
        }
    }

}
