package de.uni_hannover.sra.minimax_simulator.gui.components;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.ui.common.dialogs.FXJumpTargetDialog;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 * An extended {@link SignalTableColumn} for the UI representation of the jump attribute.
 *
 * @author Philipp Rohde
 */
public class JumpTargetColumn extends SignalTableColumn {

    private final String invalidJumpStyle = "-fx-text-fill: red; -fx-font-weight: bold;";

    /**
     * Creates a {@link TableColumn} for the jump attribute of a {@link SignalRow}.
     *
     * @param label
     *          the text of the column header
     * @param id
     *          the column's id
     * @param index
     *          the column index
     */
    public JumpTargetColumn(String label, String id, int index) {
        super(label, id, index);

        setCellFactory(new Callback<TableColumn<ObservableList, String>, TableCell<ObservableList, String>>() {
            @Override
            public TableCell<ObservableList, String> call(TableColumn<ObservableList, String> param) {
                TableCell<ObservableList, String> cell = new TableCell<ObservableList, String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {

                        if (item == null) {
                            setGraphic(null);
                        }
                        else {
                            if ( item.contains("-") && id.equals("JUMPTARGET") ) {
                                // the JumpTarget is invalid
                                setGraphic(new CenteredCellPane(item, invalidJumpStyle));
                            }
                            else {
                                setGraphic(new CenteredCellPane(item));
                            }
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

                            new FXJumpTargetDialog(signalTable, signalRow, index).showAndApply();
                        }
                    }
                });

                return cell;
            }

        });
    }

}
