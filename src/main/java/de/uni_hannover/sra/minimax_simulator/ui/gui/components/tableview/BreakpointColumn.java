package de.uni_hannover.sra.minimax_simulator.ui.gui.components.tableview;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.UndoManager;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands.SignalRowModifiedCommand;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * An extended {@link SignalTableColumn} for the UI representation of the breakpoint attribute of a {@link SignalRow}.
 *
 * @author Philipp Rohde
 */
public class BreakpointColumn extends SignalTableColumn {

    private static final Image IMG_BREAKPOINT = new Image("/images/fugue/control-record.png");

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

        setCellFactory(param -> {
            TableCell<ObservableList, String> cell = new TableCell<ObservableList, String>() {
                ImageView imageview = new ImageView();

                @Override
                public void updateItem(String item, boolean empty) {
                    if ("true".equals(item)) {
                        imageview.setImage(IMG_BREAKPOINT);
                        setGraphic(new CenteredCellPane(imageview));
                    } else {
                        setGraphic(null);
                    }
                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                if (event.getClickCount() == 2) {
                    int cIndex = cell.getTableView().getSelectionModel().getSelectedIndex();
                    SignalTable table = Main.getWorkspace().getProject().getSignalTable();
                    SignalRow oldRow = table.getRow(cIndex);

                    SignalRow newRow = new SignalRow(oldRow);
                    newRow.setBreakpoint(!newRow.isBreakpoint());
                    UndoManager.INSTANCE.addCommand(new SignalRowModifiedCommand(cIndex, oldRow, newRow, table));
                }
            });

            return cell;
        });
    }
}
