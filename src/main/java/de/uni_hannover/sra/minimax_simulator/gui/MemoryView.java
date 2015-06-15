package de.uni_hannover.sra.minimax_simulator.gui;

import static com.google.common.base.Preconditions.*;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MemoryState;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.gui.util.MemoryImportWorker;
import de.uni_hannover.sra.minimax_simulator.util.Util;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ScrollEvent;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.text.ParseException;


/**
 * FXController of the MemoryView
 *
 * @author Philipp Rohde
 */
public class MemoryView{
/*
    @FXML
    private Button memBtn;

    public void memBtnClicked() {
        System.out.println("The MemBtn was clicked!");
    }
*/
    private String _addressFormatString;
    private static MachineMemory mMemory;

    private final int _pageSize = 16;
    private int _pageCount;
    private int	_page;
    private int	_cachedPageStart;

    private static FileChooser fc;

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
    }

    public void initMemoryView() {
        mMemory = Main.getWorkspace().getProject().getMachine().getMemory();
        _addressFormatString = Util.createHexFormatString(mMemory.getAddressWidth(), false);

        initSpinner();
        initMemTable();
    }

    private void initSpinner() {
        int spinnerMin = mMemory.getMinAddress();
        int spinnerMax = mMemory.getMaxAddress();
        SpinnerValueFactory.IntegerSpinnerValueFactory spinnerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(spinnerMin, spinnerMax);
        spinnerFactory.setWrapAround(true);
        spinnerFactory.setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                return (value == null) ? "" : String.format(_addressFormatString, value);
            }

            @Override
            public Integer fromString(String text) {
                if (text == null || text.isEmpty())
                    return null;

                try
                {
                    Long l = Long.valueOf(text, 16);
                    int value = l.intValue();
                    if (value < mMemory.getMinAddress())
                        value = mMemory.getMinAddress();
                    else if (value > mMemory.getMaxAddress())
                        value = mMemory.getMaxAddress();
                    return value;
                }
                catch (NumberFormatException nfe)
                {
                    nfe.printStackTrace();
                }
                return null;
            }
        });
        spinnerStartAddress.setValueFactory(spinnerFactory);
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
        // TODO: warning dialog
        System.out.println("clearing memory");
        mMemory.getMemoryState().zero();
        //updateMemTable();
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
    private File _currentFile;

    @FXML
    private Spinner<Integer> spinnerSize;

    public void openImportDialog() {
        File selFile = fc.showOpenDialog(Main.getPrimaryStage());

        if (selFile == null) {
            txtImport.setText("");
            return;
        }
        _currentFile = selFile;
        txtImport.setText(selFile.getAbsoluteFile().toString());

        int length = (int) Math.min(Integer.MAX_VALUE, selFile.length());
        System.out.println("Größe der Datei: " + length);

        SpinnerValueFactory<Integer> sizeValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, length);
        sizeValueFactory.setWrapAround(true);
        sizeValueFactory.setValue(length);
        spinnerSize.setValueFactory(sizeValueFactory);
    }

    public void importMemory() {
        // TODO: Sicherheitsabfrage usw.
        int address = Integer.parseInt(spinnerStartAddress.getValue().toString());
        int size = (int) _currentFile.length();
        UIUtil.executeWorker(new MemoryImportWorker(mMemory, address, size,
                _currentFile), "Bitte warten", "Importiere...");
        updateMemTable();
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
