package de.uni_hannover.sra.minimax_simulator.ui.gui;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.MachineConfigMuxEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListener;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.*;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs.FXDialog;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.HexSpinnerValueFactory;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.HexStringConverter;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.NullAwareIntFormatter;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.UndoManager;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands.MuxInputAddedCommand;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands.MuxInputModifiedCommand;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands.MuxInputMovedCommand;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands.MuxInputRemovedCommand;
import de.uni_hannover.sra.minimax_simulator.util.Util;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <b>FXController of the MuxView</b><br>
 * <br>
 * This controller handles every GUI interaction with the multiplexer {@link Tab}.
 *
 * @author Philipp Rohde
 */
public class MuxView implements MachineConfigListener {

    private final TextResource res;
    private MachineConfiguration config;

    @FXML private TitledPane paneMuxA;
    @FXML private TitledPane paneMuxB;
    @FXML private TitledPane paneSelectedConnection;

    @FXML private TableView<MuxTableModel> tableMuxA;
    @FXML private TableColumn<MuxTableModel, String> colMuxAcode;
    @FXML private TableColumn<MuxTableModel, String> colMuxAsource;
    @FXML private TableColumn<MuxTableModel, String> colMuxAextended;
    @FXML private Button btnMoveUpMuxA;
    @FXML private Button btnMoveDownMuxA;
    @FXML private Button btnNewMuxA;
    @FXML private Button btnRemoveMuxA;

    @FXML private TableView<MuxTableModel> tableMuxB;
    @FXML private TableColumn<MuxTableModel, String> colMuxBcode;
    @FXML private TableColumn<MuxTableModel, String> colMuxBsource;
    @FXML private TableColumn<MuxTableModel, String> colMuxBextended;
    @FXML private Button btnMoveUpMuxB;
    @FXML private Button btnMoveDownMuxB;
    @FXML private Button btnNewMuxB;
    @FXML private Button btnRemoveMuxB;

    @FXML private RadioButton radioRegister;
    @FXML private ComboBox<MuxInput> cbRegister;
    @FXML private RadioButton radioConstant;
    @FXML private Label lblDec;
    @FXML private Label lblHex;
    @FXML private Spinner<Integer> spinnerDec;
    @FXML private Spinner<Integer> spinnerHex;
    @FXML private Button btnSave;

    private ToggleGroup tgroup = new ToggleGroup();

    /**
     * Initializes the final variables.
     */
    public MuxView() {
        res = Main.getTextResource("machine").using("mux");
    }

    /**
     * This method is called during application start up and initializes the {@code MuxView}
     * as much as possible without having any project data.
     */
    public void initialize() {
        setLocalizedTexts();

        radioRegister.setToggleGroup(tgroup);
        radioConstant.setToggleGroup(tgroup);
    }

    /**
     * Sets localized texts from resource for the GUI elements.
     */
    private void setLocalizedTexts() {
        paneMuxA.setText(res.format("table.title", "A"));
        paneMuxB.setText(res.format("table.title", "B"));
        final List<Labeled> controls = new ArrayList<>(Arrays.asList(paneSelectedConnection, radioRegister, radioConstant, lblDec, lblHex, btnSave, btnNewMuxA, btnRemoveMuxA, btnNewMuxB, btnRemoveMuxB));
        for (Labeled con : controls) {
            con.setText(res.get(con.getId().replace("_", ".")));
        }
    }

    /**
     * This method is called from the main controller if a new project was created or opened.
     * It initializes the two multiplexer {@link TableView}s as well as the register {@link ComboBox} because they need project data.
     */
    public void initMuxView() {
        config = Main.getWorkspace().getProject().getMachineConfiguration();
        config.addMachineConfigListener(this);

        radioConstant.setSelected(false);
        radioConstant.setDisable(true);
        radioRegister.setSelected(false);
        radioRegister.setDisable(true);
        cbRegister.getSelectionModel().clearSelection();
        cbRegister.setDisable(true);
        lblDec.setDisable(true);
        lblHex.setDisable(true);
        spinnerHex.setDisable(true);
        spinnerDec.setDisable(true);
        updateSaveButton();

        initTableMuxA();
        initTableMuxB();

        // set a new string converter for the combo box, otherwise the toString method of MuxInput would be used
        cbRegister.setConverter(new StringConverter<MuxInput>() {
            @Override
            public String toString(MuxInput muxInput) {
                if (muxInput == null) {
                    return null;
                } else {
                    return muxInput.getName();
                }
            }

            @Override
            public MuxInput fromString(String muxInputString) {
                return null;
            }
        });

        cbRegister.valueProperty().addListener((obs, oldValue, newValue) ->
                updateSaveButton());

        updateRegisterComboBox();

        tgroup.selectedToggleProperty().addListener((ov, t, t1) -> {
            if (t1 == null) {
                return;
            }

            RadioButton chk = (RadioButton) t1.getToggleGroup().getSelectedToggle();
            boolean disable;
            if (chk.equals(radioRegister)) {
                disable = true;
            } else if (chk.equals(radioConstant)) {
                disable = false;
            } else {
                return;
            }
            cbRegister.setDisable(!disable);
            lblDec.setDisable(disable);
            lblHex.setDisable(disable);
            spinnerHex.setDisable(disable);
            spinnerDec.setDisable(disable);

            updateSaveButton();
        });

        initSpinners();
    }

    /**
     * Updates the {@link ComboBox} containing the registers.
     */
    private void updateRegisterComboBox() {
        cbRegister.setItems(FXCollections.observableArrayList(config.getAvailableSources()));
    }

    /**
     * Sets up two synchronous {@link Spinner}s with different value representation.
     */
    private void initSpinners() {
        HexStringConverter hexStringConverter = new HexStringConverter(Integer.MIN_VALUE, Integer.MAX_VALUE);

        SpinnerValueFactory.IntegerSpinnerValueFactory vFacDec = new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE);
        vFacDec.setWrapAround(true);
        spinnerDec.setValueFactory(vFacDec);
        spinnerDec.getEditor().setTextFormatter(new NullAwareIntFormatter(NullAwareIntFormatter.Mode.DEC));
        spinnerDec.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.equals("-")) {
                return;
            }
            spinnerHex.getValueFactory().setValue(Integer.valueOf(newValue));
            updateSaveButton();
        });

        HexSpinnerValueFactory vFacHex = new HexSpinnerValueFactory();
        vFacHex.setWrapAround(true);
        spinnerHex.setValueFactory(vFacHex);
        spinnerHex.getEditor().setTextFormatter(new NullAwareIntFormatter(NullAwareIntFormatter.Mode.HEX));
        spinnerHex.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            spinnerDec.getValueFactory().setValue(hexStringConverter.fromString(newValue));
            updateSaveButton();
        });

        vFacDec.setValue(0);
        vFacHex.setValue(0);
    }

    /**
     * Initializes the {@link TableView} for multiplexer A.
     */
    private void initTableMuxA() {
        colMuxAcode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colMuxAsource.setCellValueFactory(new PropertyValueFactory<>("source"));
        colMuxAextended.setCellValueFactory(new PropertyValueFactory<>("extended"));

        tableMuxA.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                disableMuxControls(true);
                setSelectedConnection(newSelection.getMuxInput());
                if (tableMuxA.getSelectionModel().getSelectedIndex() == 0) {
                    btnMoveUpMuxA.setDisable(true);
                }
                else if (tableMuxA.getSelectionModel().getSelectedIndex() == tableMuxA.getItems().size()-1) {
                    btnMoveDownMuxA.setDisable(true);
                }
                updateSaveButton();
            }
        });

        UIUtil.removeTableHeader(tableMuxA);
        updateTableMuxA();
    }

    /**
     * Updates the {@link TableView} for multiplexer A.
     */
    private void updateTableMuxA() {
        ObservableList<MuxTableModel> data = FXCollections.observableArrayList();

        final List<MuxInput> muxInputList = config.getMuxSources(MuxType.A);
        int size = muxInputList.size();

        for (int i = 0; i < size; i++) {
            MuxInput muxInput = muxInputList.get(i);
            data.add(new MuxTableModel(muxInput, i, size));
        }

        tableMuxA.setItems(data);
    }

    /**
     * Initializes the {@link TableView} for multiplexer B.
     */
    private void initTableMuxB() {
        colMuxBcode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colMuxBsource.setCellValueFactory(new PropertyValueFactory<>("source"));
        colMuxBextended.setCellValueFactory(new PropertyValueFactory<>("extended"));

        tableMuxB.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                disableMuxControls(false);
                setSelectedConnection(newSelection.getMuxInput());
                if (tableMuxB.getSelectionModel().getSelectedIndex() == 0) {
                    btnMoveUpMuxB.setDisable(true);
                }
                else if (tableMuxB.getSelectionModel().getSelectedIndex() == tableMuxB.getItems().size()-1) {
                    btnMoveDownMuxB.setDisable(true);
                }
                updateSaveButton();
            }
        });

        UIUtil.removeTableHeader(tableMuxB);
        updateTableMuxB();
    }

    /**
     * Updates the {@link TableView} for multiplexer B.
     */
    private void updateTableMuxB() {
        ObservableList<MuxTableModel> data = FXCollections.observableArrayList();

        final List<MuxInput> muxInputList = config.getMuxSources(MuxType.B);
        int size = muxInputList.size();

        for (int i = 0; i < size; i++) {
            MuxInput muxInput = muxInputList.get(i);
            data.add(new MuxTableModel(muxInput, i, size));
        }

        tableMuxB.setItems(data);
    }

    /**
     * Sets the information of the given {@link MuxInput} to the GUI components of the {@link TitledPane} for the selected connection.
     *
     * @param muxInput
     *          the {@link MuxInput} for which the data should be displayed
     */
    private void setSelectedConnection(MuxInput muxInput) {
        if (muxInput instanceof RegisterMuxInput) {
            radioRegister.setDisable(false);
            radioConstant.setDisable(false);
            radioRegister.setSelected(true);
            cbRegister.getSelectionModel().select(muxInput);
        } else if (muxInput instanceof ConstantMuxInput) {
            radioRegister.setDisable(false);
            radioConstant.setDisable(false);
            radioConstant.setSelected(true);
            spinnerDec.getValueFactory().setValue(((ConstantMuxInput) muxInput).getConstant());
        }

    }

    /**
     * This method is called if the selection of one of the two multiplexer {@link TableView}s is changed.
     * It clears the selection of the other table and disables/enables the move up/down buttons for each table.
     *
     * @param isMuxA
     *          whether the selected multiplexer is multiplexer A or not
     */
    private void disableMuxControls(boolean isMuxA) {
        if (isMuxA) {
            tableMuxB.getSelectionModel().clearSelection();
        }
        else {
            tableMuxA.getSelectionModel().clearSelection();
        }

        btnSave.setDisable(false);
        btnMoveDownMuxA.setDisable(!isMuxA);
        btnMoveUpMuxA.setDisable(!isMuxA);
        btnMoveDownMuxB.setDisable(isMuxA);
        btnMoveUpMuxB.setDisable(isMuxA);
        btnRemoveMuxA.setDisable(!isMuxA);
        btnRemoveMuxB.setDisable(isMuxA);
    }

    /**
     * Moves the currently selected source of the currently selected multiplexer.
     * It moves the source up if the caller is the {@code moveUp} {@link Button} or down if the caller is the {@code moveDown Button}.
     * The selected multiplexer is identified by the caller object.
     *
     * @param ae
     *          the {@link ActionEvent} calling the method
     */
    public void moveSource(ActionEvent ae) {

        if (tableMuxA.getSelectionModel().getSelectedItems().isEmpty() && tableMuxB.getSelectionModel().getSelectedItems().isEmpty()) {
            return;
        }

        Object caller = ae.getSource();
        int difference = 0;

        if (caller.equals(btnMoveUpMuxA) || caller.equals(btnMoveUpMuxB)) {
            difference = -1;
        }
        else if (caller.equals(btnMoveDownMuxA) || caller.equals(btnMoveDownMuxB)) {
            difference = 1;
        }

        MuxType mux = null;
        if (caller.equals(btnMoveUpMuxA) || caller.equals(btnMoveDownMuxA)) {
            mux = MuxType.A;
        }
        else if (caller.equals(btnMoveUpMuxB) || caller.equals(btnMoveDownMuxB)) {
            mux = MuxType.B;
        }
        if (mux == null) {
            return;
        }

        int index1;
        int size;
        if (mux == MuxType.A) {
            index1 = tableMuxA.getSelectionModel().getSelectedIndex();
            size = tableMuxA.getItems().size();
        }
        else {
            // there are only MuxType.A and MuxType.B therefore it is MuxType.B here
            index1 = tableMuxB.getSelectionModel().getSelectedIndex();
            size = tableMuxB.getItems().size();
        }

        int index2 = index1 + difference;
        if (index2 < 0 || index2 >= size) {
            return;
        }

        // move one up
        UndoManager.INSTANCE.addCommand(new MuxInputMovedCommand(mux, index1, index2, config));
    }

    /**
     * Deletes the currently selected source from multiplexer A.
     */
    public void deleteSourceFromA() {
        deleteSource(MuxType.A);
    }

    /**
     * Deletes the currently selected source from multiplexer B.
     */
    public void deleteSourceFromB() {
        deleteSource(MuxType.B);
    }

    /**
     * Deletes the currently selected source from the given multiplexer.
     *
     * @param mux
     *          the multiplexer for which the source should be deleted
     */
    private void deleteSource(MuxType mux) {
        int index = -1;

        if (mux == MuxType.A) {
            index = tableMuxA.getSelectionModel().getSelectedIndex();
        }
        else {
            // there are only MuxType.A and MuxType.B therefore it is MuxType.B here
            index = tableMuxB.getSelectionModel().getSelectedIndex();
        }

        FXDialog delete = new FXDialog(Alert.AlertType.CONFIRMATION, res.get("delete-dialog.title"), res.format("delete-dialog.message", mux.name()));
        Optional<ButtonType> result = delete.showAndWait();
        if (result.get() != ButtonType.OK) {
            return;
        }

        UndoManager.INSTANCE.addCommand(new MuxInputRemovedCommand(mux, index, config));
    }

    /**
     * Adds a new default source to multiplexer A.
     */
    public void addSourceToA() {
        addSource(MuxType.A);
    }

    /**
     * Adds a new default source to multiplexer B.
     */
    public void addSourceToB() {
        addSource(MuxType.B);
    }

    /**
     * Adds a new default source to the given multiplexer.
     *
     * @param mux
     *          the multiplexer for which the source should be added
     */
    private void addSource(MuxType mux) {
        UndoManager.INSTANCE.addCommand(new MuxInputAddedCommand(mux, config));
    }

    /**
     * This method is called from the save {@link Button} and updates the selected multiplexer source at the {@link MachineConfiguration}.<br>
     * <br>
     * The selected source and multiplexer are identified via the {@link SelectionModel} of the two multiplexer {@link TableView}s.
     */
    public void updateMuxInput() {
        int index = -1;
        MuxType mux = null;

        if (!tableMuxA.getSelectionModel().getSelectedItems().isEmpty()) {
            mux = MuxType.A;
            index = tableMuxA.getSelectionModel().getSelectedIndex();
        }
        else if (!tableMuxB.getSelectionModel().getSelectedItems().isEmpty()) {
            mux = MuxType.B;
            index = tableMuxB.getSelectionModel().getSelectedIndex();
        }

        if (index == -1 || mux == null) {
            return;
        }

        MuxInput input;
        if (radioConstant.isSelected()) {
            Integer i = spinnerDec.getValue();
            if (i != null) {
                input = new ConstantMuxInput(spinnerDec.getValue());
            }
            else {
                return;
            }
        }
        else if (radioRegister.isSelected()) {
            MuxInput register = cbRegister.getSelectionModel().getSelectedItem();
            if (register != null) {
                input = register;
            }
            else {
                return;
            }
        }
        else {
            return;
        }

        UndoManager.INSTANCE.addCommand(new MuxInputModifiedCommand(mux, index, input, config));
    }

    /**
     * Checks if the save {@link Button} should be enabled and updates the disableProperty.
     */
    protected void updateSaveButton() {
        boolean isValid = isInputValid();
        boolean isUnsaved = isUnsaved();

        boolean saveEnabled = isValid && isUnsaved;
        if (btnSave.isDisable() == saveEnabled) {
            btnSave.setDisable(!saveEnabled);
        }
    }

    /**
     * Checks if the input of the currently selected source type is valid. The source types are register and constant.
     *
     * @return
     *          whether the input of either the register or constant is valid
     */
    private boolean isInputValid() {
        if (radioConstant.isSelected()) {
            if (spinnerDec.getValue() == null) {
                return false;
            }
            return true;
        }
        else if (radioRegister.isSelected()) {
            if (cbRegister.getSelectionModel().getSelectedItem() == null) {
                return false;
            }
            return true;
        }

        return false;
    }

    /**
     * Checks if the selected source from one of the {@link TableView}s was modified.
     *
     * @return
     *          whether the selected source is modified
     */
    private boolean isUnsaved() {
        MuxInput source = null;
        if (!tableMuxA.getSelectionModel().getSelectedItems().isEmpty()) {
            source = tableMuxA.getSelectionModel().getSelectedItem().getMuxInput();
        }
        else if (!tableMuxB.getSelectionModel().getSelectedItems().isEmpty()) {
            source = tableMuxB.getSelectionModel().getSelectedItem().getMuxInput();
        }

        if (source == null) {
            return false;
        }
        else if (source instanceof ConstantMuxInput && !radioConstant.isSelected()) {
            return true;
        }
        else if (source instanceof RegisterMuxInput && !radioRegister.isSelected()) {
            return true;
        }
        else if (source instanceof NullMuxInput && (radioConstant.isSelected() || radioRegister.isSelected())) {
            return true;
        }
        else if (source instanceof ConstantMuxInput) {
            Integer value = ((ConstantMuxInput) source).getConstant();
            if (!value.equals(spinnerDec.getValue())) {
                return true;
            }
        }
        else if (source instanceof RegisterMuxInput) {
            // comparison by object always is false so I used the String comparison
            String srcName = source.getName();
            String selectedName = "";
            try {
                selectedName = cbRegister.getSelectionModel().getSelectedItem().getName();
            } catch (NullPointerException e) {
                Logger.getLogger("de.uni_hannover.sra.minimax_simulator").log(Level.FINEST, "no mux input was selected", e);
                return false;
            }
            if (!srcName.equals(selectedName)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void processEvent(MachineConfigEvent event) {
        if (event instanceof MachineConfigMuxEvent) {
            MachineConfigMuxEvent e = (MachineConfigMuxEvent) event;
            updateRegisterComboBox();

            TableView<MuxTableModel> table;
            if (e.mux == MuxType.A) {
                updateTableMuxA();
                table = tableMuxA;
            }
            else {
                // there are only the multiplexers A and B so it must be B here
                updateTableMuxB();
                table = tableMuxB;
            }

            switch (e.type) {
                case ELEMENT_ADDED:
                    table.getSelectionModel().select(table.getItems().size()-1);
                    break;
                case ELEMENT_REMOVED:
                    int maxIndex = table.getItems().size() - 1;
                    int index = (e.index > maxIndex) ? maxIndex : e.index;
                    table.getSelectionModel().select(index);
                    break;
                case ELEMENTS_EXCHANGED:
                    table.getSelectionModel().select(e.index2);
                    break;
                case ELEMENT_REPLACED:
                    table.getSelectionModel().select(e.index);
                    break;
            }
        }
    }

    /**
     * This class represents the table model for the multiplexer {@link TableView}s.<br>
     * <br>
     * It stores the multiplexer source as {@link MuxInput} as well as the binary code, source info and extended source info as {@link SimpleStringProperty}.
     *
     * @author Philipp Rohde
     */
    public static class MuxTableModel {

        private final SimpleStringProperty code;
        private final SimpleStringProperty source;
        private final SimpleStringProperty extended;
        private final MuxInput muxInput;

        /**
         * Constructs a new {@code MuxTableModel} and sets the properties.
         *
         * @param muxInput
         *          the {@link MuxInput} to represent
         * @param index
         *          the index of the {@code MuxInput}
         * @param size
         *          numbers of {@code MuxInput}
         */
        private MuxTableModel(MuxInput muxInput, int index, int size) {
            this.muxInput = muxInput;
            this.code = new SimpleStringProperty(Util.toBinaryAddress(index, size-1));
            this.source = new SimpleStringProperty(muxInput.getName());
            this.extended = new SimpleStringProperty(getExtendedSourceInfo());
        }

        /**
         * Creates the extended source information of the {@code MuxInput}.<br>
         * The extended source information is the hexadecimal value of the constant.
         *
         * @return
         *         the extended source information of the {@code MuxInput}
         */
        private String getExtendedSourceInfo() {
            if (this.muxInput instanceof ConstantMuxInput) {
                int value = ((ConstantMuxInput) this.muxInput).getConstant();
                return String.format("0x%08X", value);
            }
            return "";
        }

        /**
         * Gets the selection code of the {@code MuxInput}.
         *
         * @return
         *        the selection code of the {@code MuxInput}
         */
        public String getCode() {
            return code.get();
        }

        /**
         * Sets the selection code of the {@code MuxInput} to the specified value.
         *
         * @param code
         *          the new value
         */
        public void setCode(String code) {
            this.code.set(code);
        }

        /**
         * Gets the source of the {@code MuxInput}.
         *
         * @return
         *          the source of the {@code MuxInput}
         */
        public String getSource() {
            return source.get();
        }

        /**
         * Sets the source of the {@code MuxInput} to the specified value.
         *
         * @param source
         *          the new value
         */
        public void setSource(String source) {
            this.source.set(source);
        }

        /**
         * Gets the extended source information of the {@code MuxInput}.
         *
         * @return
         *          the extended source information of the {@code MuxInput}
         */
        public String getExtended() {
            return extended.get();
        }

        /**
         * Sets the extended source information of the {@code MuxInput} to the specified value.
         *
         * @param extended
         *          the new value
         */
        public void setExtended(String extended) {
            this.extended.set(extended);
        }

        /**
         * Gets the {@code MuxInput}.
         *
         * @return
         *          the {@code MuxInput}
         */
        public MuxInput getMuxInput() {
            return muxInput;
        }
    }

}
