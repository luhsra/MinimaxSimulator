package de.uni_hannover.sra.minimax_simulator.ui.gui.components.tableview;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs.JumpTargetDialog;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 * An extended {@link SignalTableColumn} for the UI representation of the jump attribute of a {@link SignalRow}.
 *
 * @author Philipp Rohde
 */
public class JumpTargetColumn extends SignalTableColumn {

    /** The style for highlighting invalid jump targets. */
    private static final String INVALID_JUMP_STYLE = "-fx-text-fill: red; -fx-font-weight: bold;";

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

        setCellFactory(param -> {
            TableCell<ObservableList, String> cell = new TableCell<ObservableList, String>() {

                @Override
                public void updateItem(String item, boolean empty) {

                    if (item == null) {
                        setGraphic(null);
                    }
                    else {
                        if ( item.contains("-") && "JUMPTARGET".equals(id) ) {
                            // the JumpTarget is invalid
                            setGraphic(new CenteredCellPane(item, INVALID_JUMP_STYLE));
                        }
                        else {
                            setGraphic(new CenteredCellPane(item));
                        }
                    }
                }

            };

            cell.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                if (event.getClickCount() == 2) {
                    int cIndex = cell.getTableView().getSelectionModel().getSelectedIndex();
                    SignalTable signalTable = Main.getWorkspace().getProject().getSignalTable();
                    SignalRow signalRow = signalTable.getRow(cIndex);

                    new JumpTargetDialog(signalTable, signalRow, cIndex).showAndApply();
                }
            });

            return cell;
        });
    }

}
