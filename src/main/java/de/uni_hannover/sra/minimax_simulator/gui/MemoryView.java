package de.uni_hannover.sra.minimax_simulator.gui;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


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

    @FXML
    private TableView<MemoryTableModel> memTable;

    @FXML
    private TableColumn<MemoryTableModel, String> col_adr;
    @FXML
    private TableColumn<MemoryTableModel, String> col_dec;
    @FXML
    private TableColumn<MemoryTableModel, String> col_hex;


    public void fillMemTable() {
        System.out.println("filling memory table");
        MachineMemory mMemory = Main.getWorkspace().getProject().getMachine().getMemory();

        final ObservableList<MemoryTableModel> data = FXCollections.observableArrayList(
                new MemoryTableModel("0000", "0", "FF"),
                new MemoryTableModel("0001", "3", "AD")
        );

        col_adr.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_dec.setCellValueFactory(new PropertyValueFactory<>("decimal"));
        col_hex.setCellValueFactory(new PropertyValueFactory<>("hex"));

        memTable.setItems(data);

    }

    @FXML
    private Button btnClear;

    public void clearMem() {
        // TODO: Clear the memory
        System.out.println("clearing memory");
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
