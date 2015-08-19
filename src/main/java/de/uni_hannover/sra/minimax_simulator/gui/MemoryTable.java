package de.uni_hannover.sra.minimax_simulator.gui;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MemoryState;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.project.memory.components.MemoryUpdateDialog;
import de.uni_hannover.sra.minimax_simulator.util.Util;
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
import java.util.Optional;


/**
 * <b>FXController of the MemoryTable</b><br>
 * <br>
 * This controller handles every GUI interaction with the memory {@link TableView} and associated {@link Control}s.
 * The MemoryTable is the part of the DebuggerView and MemoryView.
 *
 * @author Philipp Rohde
 */
public class MemoryTable {

    private String _addressFormatString;
    private static MachineMemory mMemory;

    private final int _pageSize = 16;
    private int _pageCount;
    private int	_page;
    private int	_cachedPageStart;

    private TextResource _res;

    @FXML private TableView<MemoryTableModel> memTable;

    @FXML private TableColumn<MemoryTableModel, String> col_mem_adr;
    @FXML private TableColumn<MemoryTableModel, String> col_mem_dec;
    @FXML private TableColumn<MemoryTableModel, String> col_mem_hex;

    /**
     * This method is called during application start up and initializes the DebuggerView
     * as much as possible without having any project data.
     */
    public void initialize() {
        _res = Main.getTextResource("project");

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

        setLocalizedTexts();
    }

    @FXML private TitledPane paneMemory;

    /**
     * Sets localized texts from resource for the GUI elements.
     */
    private void setLocalizedTexts() {
        final List<TableColumn> tableColumnsMem = new ArrayList<>(Arrays.asList(col_mem_adr, col_mem_dec, col_mem_hex));
        for (TableColumn col : tableColumnsMem) {
            col.setText(_res.get(col.getId().replace("_", ".")));
        }
        paneMemory.setText(_res.get(paneMemory.getId().replace("_", ".")));

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

    @FXML private TextField txtAddressField;

    /**
     * This method is called from the controller of the views containing the MemoryTable
     * if a new project was created or a opened. It initializes the {@link TableView}
     * of the memory because it needs project data.
     */
    public void initMemTable() {
        mMemory = Main.getWorkspace().getProject().getMachine().getMemory();
        _addressFormatString = Util.createHexFormatString(mMemory.getAddressWidth(), false);

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
    public void updateMemTable() {
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

}
