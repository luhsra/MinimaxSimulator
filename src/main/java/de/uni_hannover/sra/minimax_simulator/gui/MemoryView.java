package de.uni_hannover.sra.minimax_simulator.gui;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.gui.util.MemoryExportWorker;
import de.uni_hannover.sra.minimax_simulator.gui.util.MemorySpinnerValueFactory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MemoryState;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.gui.util.MemoryImportWorker;
import de.uni_hannover.sra.minimax_simulator.ui.common.dialogs.FXDialog;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.project.memory.components.MemoryUpdateDialog;
import de.uni_hannover.sra.minimax_simulator.util.Util;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


/**
 * <b>FXController of the MemoryView</b><br>
 * <br>
 * This controller handles every GUI interaction with the memory {@link Tab}.
 * The MemoryView is the UI of the {@link MachineMemory}.
 *
 * @author Philipp Rohde
 */
//TODO: spinners editable
public class MemoryView{

    private static MachineMemory mMemory;

    private static FileChooser fc;

    private TextResource _res;

    @FXML private Spinner spinnerStartAddress;

    /**
     * This method is called during application start up and initializes the MemoryView
     * as much as possible without having any project data.
     */
    public void initialize() {
        _res = Main.getTextResource("project");

        fc = new FileChooser();

        setLocalizedTexts();
    }

    @FXML private Label lblImportFile;
    @FXML private Label lblTargetAddress;
    @FXML private Label lblByteCount;
    @FXML private TitledPane paneImport;
    @FXML private TitledPane paneExport;
    @FXML private Label lblExportFile;
    @FXML private Label lblFromAddress;
    @FXML private Label lblToAddress;
    @FXML private TitledPane paneClear;

    @FXML MemoryTable embeddedMemoryTableController;

    /**
     * Sets localized texts from resource for the GUI elements.
     */
    private void setLocalizedTexts() {
        final List<Labeled> controls = new ArrayList<>(Arrays.asList(btnImportMem, lblImportFile, lblTargetAddress, lblByteCount, cb_partialImport, paneImport, paneExport, btnExportMem, lblExportFile,
                lblFromAddress, lblToAddress, paneClear, btnClear));
        for (Labeled con : controls) {
            con.setText(_res.get(con.getId().replace("_", ".")));
        }
    }

    /**
     * This method is called from the main controller if a new project was created or a opened.
     * It initializes the memory {@link TableView} and the {@link Spinner}s because they need project data.
     */
    public void initMemoryView() {
        mMemory = Main.getWorkspace().getProject().getMachine().getMemory();

        initSpinner();
        embeddedMemoryTableController.initMemTable();
    }

    @FXML Spinner spinnerExportStartAddress;
    @FXML Spinner spinnerExportEndAddress;

    /**
     * Initializes the {@link Spinner}s. Independent {@link Spinner}s have to have their own value factory.
     */
    private void initSpinner() {
        spinnerStartAddress.setValueFactory(new MemorySpinnerValueFactory(mMemory));

        MemorySpinnerValueFactory expEndFactory = new MemorySpinnerValueFactory(mMemory);
        spinnerExportEndAddress.setValueFactory(expEndFactory);
        spinnerExportEndAddress.getValueFactory().setValue(expEndFactory.getMax());
    }

    @FXML private Button btnClear;

    /**
     * Zeros the complete memory after confirmation.
     */
    public void clearMem() {
        FXDialog memoryClear = new FXDialog(AlertType.CONFIRMATION, _res.get("memory.clear.confirm.title"), _res.get("memory.clear.confirm.message"));
        if (memoryClear.getChoice() == ButtonType.OK) {
            mMemory.getMemoryState().zero();
            embeddedMemoryTableController.updateMemTable();
        }
    }

    @FXML private TextField txtImport;
    private File _currentImportFile;
    private File _currentExportFile;

    @FXML private Spinner<Integer> spinnerSize;
    @FXML private CheckBox cb_partialImport;
    @FXML private Button btnImportMem;

    /**
     * Opens a {@link FileChooser} and updates the GUI components of the import {@link TitledPane} if the selected file was not null.
     */
    public void openImportDialog() {
        File selFile = fc.showOpenDialog(Main.getPrimaryStage());

        if (selFile == null) {
            return;
        }
        _currentImportFile = selFile;
        txtImport.setText(selFile.getAbsoluteFile().toString());

        int length = (int) Math.min(Integer.MAX_VALUE, selFile.length());

        SpinnerValueFactory<Integer> sizeValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, length);
        sizeValueFactory.setWrapAround(true);
        sizeValueFactory.setValue(length);
        spinnerSize.setValueFactory(sizeValueFactory);

        cb_partialImport.setDisable(false);
        btnImportMem.setDisable(false);
    }

    /**
     * Enables/disables the {@link Spinner} for the partial import according to the state of the {@link CheckBox}.
     */
    public void cbPartialImportAction() {
        if (cb_partialImport.isSelected()) {
            spinnerSize.setDisable(false);
        }
        else {
            spinnerSize.setDisable(true);
            spinnerSize.getValueFactory().setValue((int)_currentImportFile.length());
        }
    }

    /**
     * Imports the data from the selected input file with all import options after confirmation.
     */
    public void importMemory() {
        FXDialog memoryOverride = new FXDialog(AlertType.CONFIRMATION, _res.get("memory.import.confirm.title"), _res.get("memory.import.confirm.message"));
        if (memoryOverride.getChoice() == ButtonType.OK) {
            int address = Integer.parseInt(spinnerStartAddress.getValue().toString());
            int size = spinnerSize.getValue();
            UIUtil.executeWorker(new MemoryImportWorker(mMemory, address, size,
                    _currentImportFile, _res), _res.get("memory.import.wait.title"), _res.get("memory.import.wait.message"));
            embeddedMemoryTableController.updateMemTable();
        }
    }

    @FXML private TextField txtExport;

    /**
     * Opens a {@link FileChooser} and sets the export file to the selected file if it was not null.
     */
    public void openExportDialog() {
        File selFile = fc.showSaveDialog(Main.getPrimaryStage());

        if (selFile == null) {
            return;
        }

/*          this is not needed because the file chooser itself checks if the file already exists and asks for confirmation to override

            else if (selFile.exists()) {
            Alert fileOverride = new Alert(AlertType.CONFIRMATION);
            fileOverride.setTitle("Überschreiben bestätigen");
            fileOverride.setHeaderText(null);
            fileOverride.setContentText("Die Datei "+selFile.getPath()+" ist schon vorhanden und wird beim Exportieren überschrieben. Trotzdem auswählen?");
            fileOverride.initStyle(StageStyle.UTILITY);
            // for setting the icon of the application to the dialog
            fileOverride.initOwner(Main.getPrimaryStage());

            // FIXME: delete if issue with long texts in linux is resolved
            fileOverride.setResizable(true);

            Optional<ButtonType> override = fileOverride.showAndWait();
            if (override.get() != ButtonType.OK) {
                return;
            }
        }           */
        _currentExportFile = selFile;
        txtExport.setText(selFile.getAbsoluteFile().toString());
        btnExportMem.setDisable(false);
    }

    @FXML private Button btnExportMem;

    /**
     * Exports the memory to the current export file with all export options.
     */
    public void exportMemory() {
        Integer fromAddress = (Integer) spinnerExportStartAddress.getValue();
        Integer toAddress = (Integer) spinnerExportEndAddress.getValue();

        if (fromAddress == null || toAddress == null) {
            return;
        }

        UIUtil.executeWorker(new MemoryExportWorker(mMemory, fromAddress, toAddress,
                _currentExportFile, _res), _res.get("memory.export.wait.title"), _res.get("memory.export.wait.message"));

    }

}
