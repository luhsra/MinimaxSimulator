package de.uni_hannover.sra.minimax_simulator.gui;

import static com.google.common.base.Preconditions.*;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.gui.util.MemoryExportWorker;
import de.uni_hannover.sra.minimax_simulator.gui.util.MemorySpinnerValueFactory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MemoryState;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.gui.util.MemoryImportWorker;
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
import javafx.stage.StageStyle;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


/**
 * FXController of the MemoryView
 *
 * @author Philipp Rohde
 */
public class MemoryView{

    private String _addressFormatString;
    private static MachineMemory mMemory;

    private final int _pageSize = 16;
    private int _pageCount;
    private int	_page;
    private int	_cachedPageStart;

    private static FileChooser fc;

    private TextResource _res;

    @FXML
    private TableView<MemoryTableModel> memTable;

    @FXML
    private TableColumn<MemoryTableModel, String> col_adr;
    @FXML
    private TableColumn<MemoryTableModel, String> col_dec;
    @FXML
    private TableColumn<MemoryTableModel, String> col_hex;

    @FXML
    private Spinner spinnerStartAddress;

    public void initialize() {
        _res = Main.getTextResource("project");

        _page = _cachedPageStart = 0;

        memTable.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                double deltaY = scrollEvent.getDeltaY();
                if (deltaY > 0) {
                    prevPage();
                }
                else {
                    nextPage();
                }
            }
        });

        txtAddressField.textProperty().addListener((observable, oldValue, newValue) -> {
            String text = newValue.trim();
            if (text.isEmpty()) {
                return;
            }
            if (text.startsWith("0x")) {
                text = text.substring(2);
            }
            try
            {
                int value = Integer.parseInt(text, 16);
                System.out.println("select address: " + value);
                selectAddress(value);
            }
            catch (NumberFormatException nfe)
            {
                // Ignore malformed input
            }
        });

        fc = new FileChooser();

        setLocalizedTexts();
    }

    @FXML
    private TitledPane paneMemory;
    @FXML
    private Label lblImportFile;
    @FXML
    private Label lblTargetAddress;
    @FXML
    private Label lblByteCount;
    @FXML
    private TitledPane paneImport;
    @FXML
    private TitledPane paneExport;
    @FXML
    private Label lblExportFile;
    @FXML
    private Label lblFromAddress;
    @FXML
    private Label lblToAddress;
    @FXML
    private TitledPane paneClear;

    private void setLocalizedTexts() {
        final List<TableColumn> tableColumns = new ArrayList<>(Arrays.asList(col_adr, col_dec, col_hex));
        for (TableColumn col : tableColumns) {
            col.setText(_res.get(col.getId()));
        }

        final List<Labeled> controls = new ArrayList<>(Arrays.asList(paneMemory, btnImportMem, lblImportFile, lblTargetAddress, lblByteCount, cb_partialImport, paneImport, paneExport, btnExportMem, lblExportFile,
                lblFromAddress, lblToAddress, paneClear, btnClear));
        for (Labeled con : controls) {
            con.setText(_res.get(con.getId()));
        }
    }

    public void initMemoryView() {
        mMemory = Main.getWorkspace().getProject().getMachine().getMemory();
        _addressFormatString = Util.createHexFormatString(mMemory.getAddressWidth(), false);

        initSpinner();
        initMemTable();
    }

    @FXML
    Spinner spinnerExportStartAddress;
    @FXML
    Spinner spinnerExportEndAddress;

    // all spinners need to have their own value factory
    private void initSpinner() {
        spinnerStartAddress.setValueFactory(new MemorySpinnerValueFactory(mMemory));

        spinnerExportStartAddress.setValueFactory(new MemorySpinnerValueFactory(mMemory));

        MemorySpinnerValueFactory expEndFactory = new MemorySpinnerValueFactory(mMemory);
        spinnerExportEndAddress.setValueFactory(expEndFactory);
        spinnerExportEndAddress.getValueFactory().setValue(expEndFactory.getMax());
    }

    private void selectAddress(int address) {
        int page;
        int row;
        if (address < 0)
        {
            page = 0;
            row = 0;
        }
        else
        {
            if (address > mMemory.getMaxAddress())
            {
                address = mMemory.getMaxAddress();
            }

            page = address / _pageSize;
            if (page >= _pageCount)
            {
                page = _pageCount - 1;
                row = _pageSize - 1;
            }
            else
            {
                row = address % _pageSize;
            }
        }

        setPage(page);

        if (row != memTable.getSelectionModel().getSelectedIndex()) {
            memTable.getSelectionModel().select(row);
        }
    }

    @FXML
    private TextField txtAddressField;

    private void initMemTable() {
        System.out.println("initializing memory table");
        int addressRange = mMemory.getMaxAddress() - mMemory.getMinAddress();
        _pageCount = (addressRange - 1) / _pageSize + 1;

        col_adr.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_dec.setCellValueFactory(new PropertyValueFactory<>("decimal"));
        col_hex.setCellValueFactory(new PropertyValueFactory<>("hex"));

        updateMemTable();

        //TODO: BUGFIX for addresses containing chars
        memTable.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        System.out.println("Double clicked");
                        MemoryTableModel mtm = memTable.getSelectionModel().getSelectedItem();
                        checkNotNull(mtm);
                        int address = Integer.parseInt(mtm.getAddress());
                        checkNotNull(address);

                        // open edit dialog
                        //new MemoryUpdateDialog(address, mMemory).setVisible(true);
                        new MemoryUpdateDialog(address, mMemory).showAndWait();
                    }
                }
            }
        });
    }

    private void updateMemTable() {
        ObservableList<MemoryTableModel> data = FXCollections.observableArrayList(); /* = FXCollections.observableArrayList(
                new MemoryTableModel("0000", "0", "FF"),
                new MemoryTableModel("0001", "3", "AD")
        ); */
        MemoryState mState = mMemory.getMemoryState();
        for (int i = 0; i < _pageSize; i++) {
            int value = mState.getInt(_cachedPageStart + i);
            //System.out.println("loading data for address: " + _cachedPageStart + i);
            data.add(new MemoryTableModel(String.format(_addressFormatString, _cachedPageStart + i), String.valueOf(value), String.format("0x%08X", value)));
        }

        memTable.setItems(data);
    }

    @FXML
    private Button btnClear;

    public void clearMem() {
        Alert memoryClear = new Alert(AlertType.CONFIRMATION);
        memoryClear.setTitle(_res.get("memory.clear.confirm.title"));
        memoryClear.setHeaderText(null);
        memoryClear.setContentText(_res.get("memory.clear.confirm.message"));
        memoryClear.initStyle(StageStyle.UTILITY);
        // for setting the icon of the application to the dialog
        memoryClear.initOwner(Main.getPrimaryStage());

        Optional<ButtonType> result = memoryClear.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.out.println("clearing memory");
            //TODO: process dialog
            mMemory.getMemoryState().zero();
            updateMemTable();
        }
    }

    @FXML
    private Button btnNextPage;

    public void nextPage() {
        this.setPage(_page+1);
    }

    @FXML Button btnPrevPage;

    public void prevPage() {
        this.setPage(_page-1);
    }

    @FXML Button btnFirstPage;

    public void firstPage() {
        this.setPage(0);
    }

    @FXML Button btnLastPage;

    public void lastPage() {
        this.setPage(_pageCount-1);
    }

    private void setPage(int newPage) {
        if (newPage >= _pageCount || newPage < 0) {
            return;
        }
        _page = newPage;
        _cachedPageStart = _page * _pageSize + mMemory.getMinAddress();

        updateMemTable();
    }

    @FXML
    private TextField txtImport;
    private File _currentImportFile;
    private File _currentExportFile;

    @FXML
    private Spinner<Integer> spinnerSize;
    @FXML
    private CheckBox cb_partialImport;
    @FXML
    private Button btnImportMem;

    public void openImportDialog() {
        File selFile = fc.showOpenDialog(Main.getPrimaryStage());

        if (selFile == null) {
            return;
        }
        _currentImportFile = selFile;
        txtImport.setText(selFile.getAbsoluteFile().toString());

        int length = (int) Math.min(Integer.MAX_VALUE, selFile.length());
        //System.out.println("Größe der Datei: " + length);

        SpinnerValueFactory<Integer> sizeValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, length);
        sizeValueFactory.setWrapAround(true);
        sizeValueFactory.setValue(length);
        spinnerSize.setValueFactory(sizeValueFactory);

        cb_partialImport.setDisable(false);
        btnImportMem.setDisable(false);
    }

    public void cbPartialImportAction() {
        if (cb_partialImport.isSelected()) {
            spinnerSize.setDisable(false);
        }
        else {
            spinnerSize.setDisable(true);
            spinnerSize.getValueFactory().setValue((int)_currentImportFile.length());
        }
    }

    public void importMemory() {
        Alert memoryOverride = new Alert(AlertType.CONFIRMATION);
        memoryOverride.setTitle(_res.get("memory.import.confirm.title"));
        memoryOverride.setHeaderText(null);
        memoryOverride.setContentText(_res.get("memory.import.confirm.message"));
        memoryOverride.initStyle(StageStyle.UTILITY);
        // for setting the icon of the application to the dialog
        memoryOverride.initOwner(Main.getPrimaryStage());

        Optional<ButtonType> result = memoryOverride.showAndWait();
        if (result.get() == ButtonType.OK) {
            int address = Integer.parseInt(spinnerStartAddress.getValue().toString());
            int size = spinnerSize.getValue();
            UIUtil.executeWorker(new MemoryImportWorker(mMemory, address, size,
                    _currentImportFile, _res), _res.get("memory.import.wait.title"), _res.get("memory.import.wait.message"));
            updateMemTable();
        }
    }

    @FXML
    private TextField txtExport;

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

    @FXML
    private Button btnExportMem;

    public void exportMemory() {
        Integer fromAddress = (Integer) spinnerExportStartAddress.getValue();
        Integer toAddress = (Integer) spinnerExportEndAddress.getValue();

        if (fromAddress == null || toAddress == null) {
            return;
        }

        UIUtil.executeWorker(new MemoryExportWorker(mMemory, fromAddress, toAddress,
                _currentExportFile, _res), _res.get("memory.export.wait.title"), _res.get("memory.export.wait.message"));

    }

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


}
