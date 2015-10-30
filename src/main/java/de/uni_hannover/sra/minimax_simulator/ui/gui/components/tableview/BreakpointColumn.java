package de.uni_hannover.sra.minimax_simulator.ui.gui.components.tableview;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 * An extended {@link SignalTableColumn} for the UI representation of the breakpoint attribute of a {@link SignalRow}.
 *
 * @author Philipp Rohde
 */
public class BreakpointColumn extends SignalTableColumn {

    private static final Image imgBreakpoint = new Image("/images/fugue/control-record.png");

    /**
     * Creates a {@link TableColumn} for the breakpoint attribute of a {@link SignalRow}.
     *
     * @param label
     *          the text for the column header
     * @param index
     *          the column index
     */
    public BreakpointColumn(String label, int index) {
        super(label, "BREAKPOINT", index);
        setResizable(false);
        setWidth(30);

        setCellFactory(new Callback<TableColumn<ObservableList, String>, TableCell<ObservableList, String>>() {
            @Override
            public TableCell<ObservableList, String> call(TableColumn<ObservableList, String> param) {
                TableCell<ObservableList, String> cell = new TableCell<ObservableList, String>() {
                    ImageView imageview = new ImageView();

                    @Override
                    public void updateItem(String item, boolean empty) {
                        if (item != null && item.equals("true")) {
                            imageview.setImage(imgBreakpoint);
                            setGraphic(new CenteredCellPane(imageview));
                        } else {
                            setGraphic(null);
                        }
                    }
                };

                cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getClickCount() == 2) {
                            int index = cell.getTableView().getSelectionModel().getSelectedIndex();
                            SignalTable signalTable = Main.getWorkspace().getProject().getSignalTable();
                            SignalRow signalRow = signalTable.getRow(index);

                            signalRow.setBreakpoint(!signalRow.isBreakpoint());
                            signalTable.setSignalRow(index, signalRow);
                        }
                    }
                });

                return cell;
            }

        });
    }
}
