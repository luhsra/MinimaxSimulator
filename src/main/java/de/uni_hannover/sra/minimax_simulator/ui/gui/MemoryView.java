package de.uni_hannover.sra.minimax_simulator.ui.gui;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.MainGUI;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.Simulation;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.SimulationState;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs.FXDialog;
import de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs.SimulationRunningDialog;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.*;
import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <b>FXController of the MemoryView</b><br>
 * <br>
 * This controller handles every GUI interaction with the memory {@link Tab}.
 * The MemoryView is the UI of the {@link MachineMemory}.
 *
 * @author Philipp Rohde
 */
public class MemoryView {

    private static MachineMemory mMemory;

    private static FileChooser fc = new FileChooser();

    private final TextResource res;

    @FXML private Spinner spinnerStartAddress;
    @FXML private Spinner spinnerExportStartAddress;
    @FXML private Spinner spinnerExportEndAddress;

    @FXML private Label lblImportFile;
    @FXML private Label lblTargetAddress;
    @FXML private Label lblByteCount;
    @FXML private TitledPane paneImport;
    @FXML private TitledPane paneExport;
    @FXML private Label lblExportFile;
    @FXML private Label lblFromAddress;
    @FXML private Label lblToAddress;
    @FXML private TitledPane paneClear;

    @FXML private MemoryTable embeddedMemoryTableController;

    @FXML private Button btnClear;
    @FXML private TextField txtImport;
    private File currentImportFile;
    private File currentExportFile;

    @FXML private Spinner<Integer> spinnerSize;
    @FXML private CheckBox cbPartialImport;
    @FXML private Button btnImportMem;
    @FXML private TextField txtExport;
    @FXML private Button btnExportMem;

    /**
     * Initializes the final variables.
     */
    public MemoryView() {
        res = Main.getTextResource("project");
    }

    /**
     * This method is called during application start up and initializes the {@code MemoryView}
     * as much as possible without having any project data.
     */
    public void initialize() {
        setLocalizedTexts();
    }

    /**
     * Sets localized texts from resource for the GUI elements.
     */
    private void setLocalizedTexts() {
        final List<Labeled> controls = new ArrayList<>(Arrays.asList(btnImportMem, lblImportFile, lblTargetAddress, lblByteCount, cbPartialImport, paneImport, paneExport, btnExportMem, lblExportFile,
                lblFromAddress, lblToAddress, paneClear, btnClear));
        for (Labeled con : controls) {
            con.setText(res.get(con.getId().replace("_", ".")));
        }
    }

    /**
     * This method is called from the main controller if a new project was created or opened.
     * It initializes the memory {@link TableView} and the {@link Spinner}s because they need project data.
     */
    public void initMemoryView() {
        mMemory = Main.getWorkspace().getProject().getMachine().getMemory();

        txtExport.setText("");
        txtImport.setText("");
        currentExportFile = null;
        currentImportFile = null;
        cbPartialImport.setSelected(false);
        cbPartialImport.setDisable(true);
        spinnerSize.setDisable(true);
        btnExportMem.setDisable(true);
        btnImportMem.setDisable(true);

        initSpinner();
        embeddedMemoryTableController.initMemTable();
    }

    /**
     * Initializes the {@link Spinner}s.
     */
    private void initSpinner() {
        spinnerStartAddress.setValueFactory(new MemorySpinnerValueFactory(mMemory));
        spinnerStartAddress.getEditor().setTextFormatter(new AddressFormatter(mMemory));
        spinnerStartAddress.getValueFactory().setValue(mMemory.getMinAddress());
        InvalidationListener importStart = observable -> {
            String curText = spinnerStartAddress.getEditor().getCharacters().toString();
            int newValue = Integer.parseInt(curText, 16);
            spinnerStartAddress.getValueFactory().setValue(newValue);
        };
        spinnerStartAddress.focusedProperty().addListener(importStart);

        MemorySpinnerValueFactory expEndFactory = new MemorySpinnerValueFactory(mMemory);
        spinnerExportEndAddress.setValueFactory(expEndFactory);
        spinnerExportEndAddress.getEditor().setTextFormatter(new AddressFormatter(mMemory));
        spinnerExportEndAddress.getValueFactory().setValue(expEndFactory.getMax());
        InvalidationListener exportEnd = observable -> {
            String curText = spinnerExportEndAddress.getEditor().getCharacters().toString();
            int newValue = Integer.parseInt(curText, 16);
            spinnerExportEndAddress.getValueFactory().setValue(newValue);
        };
        spinnerExportEndAddress.focusedProperty().addListener(exportEnd);

        MemorySpinnerValueFactory startEndFactory = new MemorySpinnerValueFactory(mMemory);
        spinnerExportStartAddress.setValueFactory(startEndFactory);
        spinnerExportStartAddress.getEditor().setTextFormatter(new AddressFormatter(mMemory));
        spinnerExportStartAddress.getValueFactory().setValue(startEndFactory.getMin());
        InvalidationListener exportStart = observable -> {
            String curText = spinnerExportStartAddress.getEditor().getCharacters().toString();
            int newValue = Integer.parseInt(curText, 16);
            spinnerExportStartAddress.getValueFactory().setValue(newValue);
        };
        spinnerExportStartAddress.focusedProperty().addListener(exportStart);

        spinnerSize.getEditor().setTextFormatter(new NullAwareIntFormatter(NullAwareIntFormatter.Mode.DEC));
    }

    /**
     * Zeros the complete memory after confirmation.
     */
    public void clearMem() {
        FXDialog memoryClear = new FXDialog(AlertType.CONFIRMATION, res.get("memory.clear.confirm.title"), res.get("memory.clear.confirm.message"));
        if (memoryClear.getChoice() == ButtonType.OK) {
            mMemory.getMemoryState().zero();
            embeddedMemoryTableController.updateMemTable();
        }
    }

    /**
     * Opens a {@link FileChooser} and updates the GUI components of the import {@link TitledPane} if the selected file was not null.
     */
    public void openImportDialog() {
        if (currentImportFile != null && currentImportFile.getParentFile().exists()) {
            fc.setInitialDirectory(currentImportFile.getParentFile());
        }
        else if (currentExportFile != null && currentExportFile.getParentFile().exists()) {
            fc.setInitialDirectory(currentExportFile.getParentFile());
        }

        File selFile = fc.showOpenDialog(MainGUI.getPrimaryStage());

        if (selFile == null) {
            return;
        }
        currentImportFile = selFile;
        txtImport.setText(selFile.getAbsoluteFile().toString());

        int length = (int) Math.min(Integer.MAX_VALUE, selFile.length());

        SpinnerValueFactory<Integer> sizeValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, length);
        sizeValueFactory.setWrapAround(true);
        sizeValueFactory.setValue(length);
        spinnerSize.setValueFactory(sizeValueFactory);

        cbPartialImport.setDisable(false);
        btnImportMem.setDisable(false);
    }

    /**
     * Enables/disables the {@link Spinner} for the partial import according to the state of the {@link CheckBox}.
     */
    public void cbPartialImportAction() {
        if (cbPartialImport.isSelected()) {
            spinnerSize.setDisable(false);
        }
        else {
            spinnerSize.setDisable(true);
            spinnerSize.getValueFactory().setValue((int) currentImportFile.length());
        }
    }

    /**
     * Imports the data from the selected input file with all import options after confirmation.
     */
    public void importMemory() {
        Simulation simulation = Main.getWorkspace().getProject().getSimulation();
        if (simulation.getState() != SimulationState.OFF) {
            SimulationRunningDialog srd = new SimulationRunningDialog();
            ButtonType result = srd.getChoice();
            if (result == ButtonType.YES) {
                simulation.stop();
            }
            else if (result == ButtonType.CANCEL) {
                return;
            }
        }

        FXDialog memoryOverride = new FXDialog(AlertType.CONFIRMATION, res.get("memory.import.confirm.title"), res.get("memory.import.confirm.message"));
        if (memoryOverride.getChoice() == ButtonType.OK) {
            int address = Integer.parseInt(spinnerStartAddress.getValue().toString());
            int size = spinnerSize.getValue();
            UIUtil.executeWorker(new MemoryImportWorker(mMemory, address, size,
                    currentImportFile, res), res.get("memory.import.wait.title"), res.get("memory.import.wait.message"));
            embeddedMemoryTableController.updateMemTable();
        }
    }

    /**
     * Opens a {@link FileChooser} and sets the export file to the selected file if it was not null.
     */
    public void openExportDialog() {
        if (currentExportFile != null && currentExportFile.getParentFile().exists()) {
            fc.setInitialDirectory(currentExportFile.getParentFile());
        }
        else if (currentImportFile != null && currentImportFile.getParentFile().exists()) {
            fc.setInitialDirectory(currentImportFile.getParentFile());
        }

        File selFile = fc.showSaveDialog(MainGUI.getPrimaryStage());

        if (selFile == null) {
            return;
        }

        //the file chooser itself checks if the file already exists and asks for confirmation to override

        currentExportFile = selFile;
        txtExport.setText(selFile.getAbsoluteFile().toString());
        btnExportMem.setDisable(false);
    }

    /**
     * Exports the memory to the current export file with all export options.
     */
    public void exportMemory() {
        Integer fromAddress = (Integer) spinnerExportStartAddress.getValue();
        Integer toAddress = (Integer) spinnerExportEndAddress.getValue();

        if (fromAddress == null || toAddress == null) {
            return;
        }

        UIUtil.executeWorker(new MemoryExportWorker(mMemory, fromAddress, toAddress, currentExportFile, res), res.get("memory.export.wait.title"), res.get("memory.export.wait.message"));
    }

}
