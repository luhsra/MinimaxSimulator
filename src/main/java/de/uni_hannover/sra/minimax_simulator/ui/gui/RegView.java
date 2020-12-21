package de.uni_hannover.sra.minimax_simulator.ui.gui;

import de.uni_hannover.sra.minimax_simulator.config.Config;
import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.MachineConfigRegisterEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListener;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterSize;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs.FXDialog;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.UndoManager;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands.RegisterAddedCommand;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands.RegisterRemovedCommand;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands.RegisterModifiedCommand;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands.RegisterMovedCommand;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
public class RegView implements MachineConfigListener {

    private final TextResource res;

    private MachineConfiguration config;

    @FXML private TitledPane paneBaseReg;
    @FXML private TitledPane paneSelectedReg;
    @FXML private TitledPane paneExtendedReg;
    @FXML private Label lblName;
    @FXML private TextField txtName;
    @FXML private Label lblDescription;
    @FXML private TextArea txtDescription;
    @FXML private Label lblSize;
    @FXML private ComboBox<RegisterSize> cbSize;
    @FXML private Button btnSave;
    @FXML private Button btnAdd;
    @FXML private Button btnRemove;
    @FXML private Button btnMoveUp;
    @FXML private Button btnMoveDown;

    @FXML private TableView<RegisterTableModel> tableBaseReg;
    @FXML private TableColumn<RegisterTableModel, String> colBaseName;
    @FXML private TableColumn<RegisterTableModel, String> colBaseSize;

    @FXML private TableView<RegisterTableModel> tableExtendedReg;
    @FXML private TableColumn<RegisterTableModel, String> colExtendedName;
    @FXML private TableColumn<RegisterTableModel, String> colExtendedSize;

    /**
     * Initializes the final variables.
     */
    public RegView() {
        res = Main.getTextResource("machine").using("register");
    }

    /**
     * This method is called during application start up and initializes the {@code RegView}
     * as much as possible without having any project data.
     */
    public void initialize() {
        txtDescription.setWrapText(true);

        cbSize.getItems().addAll(RegisterSize.BITS_32, RegisterSize.BITS_24);

        // ComboBox and TextFields check if the save button will be enabled
        cbSize.valueProperty().addListener((observable, oldValue, newValue) -> updateButton());

        txtName.textProperty().addListener((observable, oldValue, newValue) -> updateButton());

        txtName.lengthProperty().addListener((observable, oldValue, newValue) -> {
            int limit = Config.getEditorMaxRegisterLength();
            if (txtName.getText().length() > limit) {
                txtName.setText(txtName.getText(0, limit));
            }
        });

        txtDescription.textProperty().addListener((observable, oldValue, newValue) -> updateButton());

        setLocalizedTexts();
    }

    /**
     * Sets localized texts from resource for the GUI elements.
     */
    private void setLocalizedTexts() {
        final List<Labeled> controls = new ArrayList<>(Arrays.asList(paneBaseReg, paneExtendedReg, paneSelectedReg, lblName, lblDescription, btnSave, btnRemove, btnAdd, lblSize));
        for (Labeled con : controls) {
            con.setText(res.get(con.getId().replace("_", ".")));
        }
    }

    /**
     * This method is called from the main controller if a new project was created or opened.
     * It initializes the two register {@link TableView}s because they need project data.
     */
    public void initRegView() {
        config = Main.getWorkspace().getProject().getMachineConfiguration();
        config.addMachineConfigListener(this);

        txtDescription.setText("");
        txtName.setText("");
        cbSize.getSelectionModel().clearSelection();

        initBaseTable();
        initExtendedTable();
    }

    /**
     * Initializes the {@link TableView} for the base registers.
     */
    private void initBaseTable() {
        colBaseSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colBaseName.setCellValueFactory(new PropertyValueFactory<>("name"));

        // set selected register and clear selection of other table
        tableBaseReg.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tableExtendedReg.getSelectionModel().clearSelection();
                RegisterExtension reg = newSelection.getRegister();
                txtName.setText(reg.getName());
                txtName.setEditable(false);
                txtDescription.setText(reg.getDescription());
                txtDescription.setEditable(false);
                cbSize.getSelectionModel().select(reg.getSize());

                btnSave.setDisable(true);
                btnRemove.setDisable(true);
                btnMoveDown.setDisable(true);
                btnMoveUp.setDisable(true);
            }
        });

        UIUtil.removeTableHeader(tableBaseReg);
        updateBaseTable();
    }

    /**
     * Updates the {@link TableView} for the base registers.
     */
    private void updateBaseTable() {
        ObservableList<RegisterTableModel> data = FXCollections.observableArrayList();

        for (RegisterExtension reg : config.getBaseRegisters()) {
            data.add(new RegisterTableModel(reg));
        }

        tableBaseReg.setItems(data);
    }

    /**
     * Initializes the {@link TableView} for the extended registers.
     */
    private void initExtendedTable() {
        colExtendedName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colExtendedSize.setCellValueFactory(new PropertyValueFactory<>("size"));

        // set selected register and clear selection of other table
        tableExtendedReg.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tableBaseReg.getSelectionModel().clearSelection();
                RegisterExtension reg = newSelection.getRegister();
                txtName.setText(reg.getName());
                txtName.setEditable(true);
                txtDescription.setText(reg.getDescription());
                txtDescription.setEditable(true);
                cbSize.getSelectionModel().select(reg.getSize());

                btnSave.setDisable(true);
                btnRemove.setDisable(false);
                btnMoveDown.setDisable(false);
                btnMoveUp.setDisable(false);
                if (tableExtendedReg.getSelectionModel().getSelectedIndex() == 0) {
                    btnMoveUp.setDisable(true);
                }
                if (tableExtendedReg.getSelectionModel().getSelectedIndex() == tableExtendedReg.getItems().size()-1) {
                    btnMoveDown.setDisable(true);
                }
            }
        });

        UIUtil.removeTableHeader(tableExtendedReg);
        updateExtendedTable();
    }

    /**
     * Updates the {@link TableView} for the extended registers.
     */
    private void updateExtendedTable() {
        ObservableList<RegisterTableModel> data = FXCollections.observableArrayList();

        for (RegisterExtension reg : config.getRegisterExtensions()) {
            data.add(new RegisterTableModel(reg));
        }

        tableExtendedReg.setItems(data);
    }

    /**
     * Adds a new register to the extended registers list.
     */
    public void addRegister() {
        RegisterExtension reg = createNewRegister();
        UndoManager.INSTANCE.addCommand(new RegisterAddedCommand(reg, config));
    }

    /**
     * Creates a default {@link RegisterExtension}.
     *
     * @return
     *          the created {@link RegisterExtension}
     */
    private RegisterExtension createNewRegister() {
        String newName;
        int number = 0;

        NameSearch:
        while (true) {
            number++;

            newName = res.format("new-name", "(" + number + ")");

            // check if this is a new name
            for (RegisterExtension register : config.getRegisterExtensions()) {
                if (register.getName().equalsIgnoreCase(newName)) {
                    continue NameSearch;
                }
            }

            // new name
            break;
        }

        return new RegisterExtension(newName, RegisterSize.BITS_32, "", true);
    }

    /**
     * Removes the currently selected register from the list of the extended registers.
     */
    public void removeRegister() {
        if (tableExtendedReg.getSelectionModel().getSelectedItems().isEmpty()) {
            return;
        }
        RegisterExtension reg = tableExtendedReg.getSelectionModel().getSelectedItem().getRegister();

        if (new FXDialog(Alert.AlertType.CONFIRMATION, res.format("dialog.delete.message", reg.getName()), res.get("dialog.delete.title")).getChoice() == ButtonType.OK) {
            UndoManager.INSTANCE.addCommand(new RegisterRemovedCommand(reg, config));
        }
    }

    /**
     * Moves the currently selected register.
     * It moves the register up if the caller is the {@code moveUp} {@link Button} or down if the caller is the {@code moveDown Button}.
     *
     * @param ae
     *          the {@link ActionEvent} calling the method
     */
    public void moveOperation(ActionEvent ae) {

        if (tableExtendedReg.getSelectionModel().getSelectedItems().isEmpty()) {
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

        int index1 = tableExtendedReg.getSelectionModel().getSelectedIndex();
        int index2 = index1 + difference;
        if (index2 < 0 || index2 >= tableExtendedReg.getItems().size()) {
            return;
        }

        // move registers in model
        UndoManager.INSTANCE.addCommand(new RegisterMovedCommand(index1, index2, config));
    }

    /**
     * Checks if the save {@link Button} should be enabled and updates the disableProperty.
     */
    private void updateButton() {
        boolean isValid = isInputValid();
        boolean isUnsaved = isUnsaved();

        boolean saveEnabled = isValid && isUnsaved;
        if (btnSave.isDisable() == saveEnabled)
            btnSave.setDisable(!saveEnabled);
    }

    /**
     * Checks if the input of the currently selected source type is valid. The source types are register and constant.
     *
     * @return
     *          whether the input of either the register or constant is valid
     */
    private boolean isInputValid() {
        if (tableExtendedReg.getSelectionModel().getSelectedItems().isEmpty()) {
            return false;
        }
        RegisterExtension reg = tableExtendedReg.getSelectionModel().getSelectedItem().getRegister();
        String newName = txtName.getText();

        if (newName.isEmpty() || cbSize.getSelectionModel().getSelectedItem() == null) {
            return false;
        }

        for (RegisterExtension otherReg : config.getRegisterExtensions()) {
            if (!reg.getName().equals(otherReg.getName())
                    && newName.equalsIgnoreCase(otherReg.getName())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the selected register from one of the {@link TableView}s was modified.
     *
     * @return
     *          whether the selected source is modified
     */
    private boolean isUnsaved() {
        if (tableExtendedReg.getSelectionModel().getSelectedItems().isEmpty()) {
            return false;
        }
        RegisterExtension reg = tableExtendedReg.getSelectionModel().getSelectedItem().getRegister();

        if (reg == null) {
            return false;
        }

        if (!reg.getName().equals(txtName.getText())) {
            return true;
        }

        if (reg.getSize() != cbSize.getSelectionModel().getSelectedItem()) {
            return true;
        }

        if (!reg.getDescription().equals(txtDescription.getText())) {
            return true;
        }

        return false;
    }

    /**
     * Saves the changes made to the extended register.
     */
    public void saveChanges() {
        if (tableExtendedReg.getSelectionModel().getSelectedItems().isEmpty()) {
            return;
        }
        RegisterExtension regOld = tableExtendedReg.getSelectionModel().getSelectedItem().getRegister();
        int index = tableExtendedReg.getSelectionModel().getSelectedIndex();

        RegisterExtension regNew = new RegisterExtension(txtName.getText().trim(), cbSize.getValue(), txtDescription.getText(), regOld.isExtended());

        UndoManager.INSTANCE.addCommand(new RegisterModifiedCommand(index, regOld, regNew, config));
    }

    @Override
    public void processEvent(MachineConfigEvent event) {
        if (event instanceof MachineConfigRegisterEvent) {
            MachineConfigRegisterEvent e = (MachineConfigRegisterEvent) event;
            updateExtendedTable();

            if (e.type == MachineConfigListEvent.EventType.ELEMENT_ADDED) {
                tableExtendedReg.getSelectionModel().select(e.index);
            }
            else if (e.type == MachineConfigListEvent.EventType.ELEMENT_REMOVED) {
                if (!tableExtendedReg.getItems().isEmpty()) {
                    int maxIndex = tableExtendedReg.getItems().size() - 1;
                    int index = (e.index > maxIndex) ? maxIndex : e.index;
                    tableExtendedReg.getSelectionModel().select(index);
                }
                else {
                    tableExtendedReg.getSelectionModel().clearSelection();
                    btnRemove.setDisable(true);
                }
            }
            else if (e.type == MachineConfigListEvent.EventType.ELEMENTS_EXCHANGED) {
                tableExtendedReg.getSelectionModel().select(e.index2);
            }
            else if (e.type == MachineConfigListEvent.EventType.ELEMENT_REPLACED) {
                updateButton();
                tableExtendedReg.getSelectionModel().select(e.index);
            }
        }
    }


    /**
     * This class represents the table model for the register {@link TableView}s.<br>
     * <br>
     * It stores the {@link RegisterExtension} as well as the name and size as {@link SimpleStringProperty}.
     *
     * @author Philipp Rohde
     */
    public static class RegisterTableModel {

        private final SimpleStringProperty name;
        private final SimpleStringProperty size;
        private final RegisterExtension register;

        /**
         * Constructs a new {@code RegisterTableModel} and sets the properties.
         *
         * @param register
         *          the {@link RegisterExtension} to represent
         */
        private RegisterTableModel(RegisterExtension register) {
            this.name = new SimpleStringProperty(register.getName());
            this.size = new SimpleStringProperty(register.getSize().toString());
            this.register = register;
        }

        /**
         * Gets the register's name.
         *
         * @return
         *          the register's name
         */
        public String getName() {
            return name.get();
        }

        /**
         * Sets the name of the register to the specified value.
         *
         * @param name
         *          the new name
         */
        public void setName(String name) {
            this.name.set(name);
        }

        /**
         * Gets the bit width of the register.
         *
         * @return
         *          the register's bit width
         */
        public String getSize() {
            return size.get();
        }

        /**
         * Sets the bit width of the register to the specified value.
         *
         * @param size
         *          the new bit width
         */
        public void setSize(String size) {
            this.size.set(size);
        }

        /**
         * Gets the register.
         *
         * @return
         *          the register
         */
        public RegisterExtension getRegister() {
            return register;
        }
    }

}
