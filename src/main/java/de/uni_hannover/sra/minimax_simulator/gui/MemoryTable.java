package de.uni_hannover.sra.minimax_simulator.gui;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MemoryAccessListener;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MemoryState;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.project.memory.components.MemoryUpdateDialog;
import de.uni_hannover.sra.minimax_simulator.util.Util;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <b>FXController of the MemoryTable</b><br>
 * <br>
 * This controller handles every GUI interaction with the memory {@link TableView} and associated {@link Control}s.
 * The MemoryTable is the part of the DebuggerView and MemoryView.
 *
 * @author Philipp Rohde
 */
public class MemoryTable implements MemoryAccessListener {

    @FXML private TitledPane paneMemory;

    private String _addressFormatString;
    private static MachineMemory mMemory;

    private final int _pageSize = 16;
    private int _pageCount;
    private int	_page;
    private int	_cachedPageStart;

    private final TextResource _res;

    @FXML private TableView<MemoryTableModel> memTable;

    @FXML private TableColumn<MemoryTableModel, String> col_mem_adr;
    @FXML private TableColumn<MemoryTableModel, String> col_mem_dec;
    @FXML private TableColumn<MemoryTableModel, String> col_mem_hex;

    @FXML private TextField txtAddressField;
    @FXML private Label lblMemPage;

    /**
     * The constructor initializes the variables.
     */
    public MemoryTable() {
        _res = Main.getTextResource("project");
        _page = _cachedPageStart = 0;
    }

    /**
     * This method is called during application start up and initializes the DebuggerView
     * as much as possible without having any project data.
     */
    public void initialize() {
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

        setLocalizedTexts();
        setTooltips();
    }

    /**
     * Sets localized texts from resource for the GUI elements.
     */
    private void setLocalizedTexts() {
        final List<TableColumn> tableColumnsMem = new ArrayList<>(Arrays.asList(col_mem_adr, col_mem_dec, col_mem_hex));
        for (TableColumn col : tableColumnsMem) {
            col.setText(_res.get(col.getId().replace("_", ".")));
        }
        paneMemory.setText(_res.get(paneMemory.getId().replace("_", ".")));
        updateMemPageLabel();
    }

    /**
     * Sets the tooltips for the buttons.
     */
    private void setTooltips() {
        btnFirstPage.setTooltip(new Tooltip(_res.get("memtable.first.tip")));
        btnPrevPage.setTooltip(new Tooltip(_res.get("memtable.previous.tip")));
        btnNextPage.setTooltip(new Tooltip(_res.get("memtable.next.tip")));
        btnLastPage.setTooltip(new Tooltip(_res.get("memtable.last.tip")));

        txtAddressField.setTooltip(new Tooltip(_res.get("memtable.address.tip")));
    }

    private void updateMemPageLabel() {
        lblMemPage.setText(_res.format("memtable.page", _page + 1, _pageCount));
    }

    /**
     * Selects a specific address of the memory {@link TableView}.
     *
     * @param address
     *          the address to select
     */
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
     * This method is called from the controller of the views containing the MemoryTable
     * if a new project was created or a opened. It initializes the {@link TableView}
     * of the memory because it needs project data.
     */
    public void initMemTable() {
        mMemory = Main.getWorkspace().getProject().getMachine().getMemory();
        mMemory.addMemoryAccessListener(this);
        _addressFormatString = Util.createHexFormatString(mMemory.getAddressWidth(), false);

        int addressRange = mMemory.getMaxAddress() - mMemory.getMinAddress();
        _pageCount = (addressRange - 1) / _pageSize + 1;

        updateMemPageLabel();

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
                        int address = memTable.getSelectionModel().getSelectedIndex() + _cachedPageStart;
                        // open edit dialog
                        new MemoryUpdateDialog(address, mMemory).show();
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
    public void updateMemTable() {
        ObservableList<MemoryTableModel> data = FXCollections.observableArrayList();

        MemoryState mState = mMemory.getMemoryState();
        for (int i = 0; i < _pageSize; i++) {
            int value = mState.getInt(_cachedPageStart + i);
            data.add(new MemoryTableModel(String.format(_addressFormatString, _cachedPageStart + i), value));
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
        updateMemPageLabel();
    }

    @Override
    public void memoryReadAccess(int address, int value) {
        // there is nothing to do if the memory was read
    }

    @Override
    public void memoryWriteAccess(int address, int value) {
        // only update the affected table row
        MemoryTableModel entry = memTable.getItems().get(address % _pageSize);
        entry.setValue(value);
    }

    @Override
    public void memoryReset() {
        // update the whole table
        updateMemTable();
    }

    @Override
    public void memoryChanged() {
        //update the whole table
        updateMemTable();
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
        private final SimpleIntegerProperty decimal;
        private final SimpleStringProperty hex;

        private static final String _hexFormatString = "0x%08X";

        private MemoryTableModel(String address, int value) {
            this.address = new SimpleStringProperty(address);
            this.decimal = new SimpleIntegerProperty(value);
            this.hex = new SimpleStringProperty(String.format(_hexFormatString, value));
        }

        public String getAddress() {
            return address.get();
        }

        public SimpleStringProperty addressProperty() {
            return address;
        }

        public int getDecimal() {
            return decimal.get();
        }

        public SimpleIntegerProperty decimalProperty() {
            return decimal;
        }

        public String getHex() {
            return hex.get();
        }

        public SimpleStringProperty hexProperty() {
            return hex;
        }

        public void setValue(int value) {
            this.decimal.set(value);
            this.hex.set(String.format(_hexFormatString, value));
        }
    }

}
