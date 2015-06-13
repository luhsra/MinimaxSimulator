package de.uni_hannover.sra.minimax_simulator.gui;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import de.uni_hannover.sra.minimax_simulator.gui.MemoryTableModel;


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
    private TableColumn<MemoryTableModel, String> cl_adr;
    @FXML
    private TableColumn<MemoryTableModel, String> cl_dec;
    @FXML
    private TableColumn<MemoryTableModel, String> cl_hex;


    public void fillMemTable() {
        System.out.println("filling memory table");
        MachineMemory mMemory = Main.getWorkspace().getProject().getMachine().getMemory();

        final ObservableList<MemoryTableModel> data = FXCollections.observableArrayList(
                new de.uni_hannover.sra.minimax_simulator.gui.MemoryTableModel("0000", "0", "FF"),
                new de.uni_hannover.sra.minimax_simulator.gui.MemoryTableModel("0001", "3", "AD")
        );

        cl_adr.setCellValueFactory(new PropertyValueFactory<>("address"));
        cl_dec.setCellValueFactory(new PropertyValueFactory<>("decimal"));
        cl_hex.setCellValueFactory(new PropertyValueFactory<>("hex"));

        memTable.setItems(data);

    }

    @FXML
    private Button btnClear;

    public void clearMem() {
        // TODO: Clear the memory
        System.out.println("clearing memory");
    }



}
