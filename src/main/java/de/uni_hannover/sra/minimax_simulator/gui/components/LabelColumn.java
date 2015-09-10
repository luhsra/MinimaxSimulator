package de.uni_hannover.sra.minimax_simulator.gui.components;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 * An extended {@link SignalTableColumn} for the UI representation of the label attribute.
 *
 * @author Philipp Rohde
 */
public class LabelColumn extends SignalTableColumn {

    /**
     * Creates a {@link TableColumn} for the label attribute of a {@link SignalRow}.
     *
     * @param label
     *          the text for the column header
     * @param index
     *          the column index
     */
    public LabelColumn(String label, int index) {
        super(label, "LABEL", index);
        setPrefWidth(100);
        setMaxWidth(200);

        setCellFactory(new Callback<TableColumn<ObservableList, String>, TableCell<ObservableList, String>>() {
            @Override
            public TableCell<ObservableList, String> call(TableColumn<ObservableList, String> param) {
                TableCell<ObservableList, String> cell = new TableCell<ObservableList, String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {

                        if (item == null) {
                            setGraphic(null);
                        } else {
                            setGraphic(new CenteredCellPane(item));
                        }
                    }
                };

                cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getClickCount() == 2) {
                            System.out.println("double clicked!");
                            int index = cell.getTableView().getSelectionModel().getSelectedIndex();
                            SignalTable signalTable = Main.getWorkspace().getProject().getSignalTable();
                            SignalRow signalRow = signalTable.getRow(index);

                            TextField txtLabel = new TextField(signalRow.getLabel());

                            txtLabel.setOnKeyReleased(new EventHandler<KeyEvent>() {
                                @Override
                                public void handle(KeyEvent evt) {

                                    if (KeyCode.ENTER == evt.getCode()) {
                                        signalRow.setLabel(txtLabel.getText());
                                        txtLabel.cancelEdit();
                                        signalTable.setSignalRow(index, signalRow);
                                        Main.getWorkspace().setProjectUnsaved();
                                    }
                                    else if (KeyCode.ESCAPE == evt.getCode()) {
                                        txtLabel.cancelEdit();
                                        cell.setGraphic(new Label(signalRow.getLabel()));
                                    }
                                }
                            });

                            txtLabel.focusedProperty().addListener(new ChangeListener<Boolean>() {
                                @Override
                                public void changed(ObservableValue<? extends Boolean> val, Boolean oldVal, Boolean newVal) {

                                    if (!newVal) {
                                        txtLabel.cancelEdit();
                                        cell.setGraphic(new Label(signalRow.getLabel()));
                                    }
                                }
                            });


                            cell.setGraphic(txtLabel);
                            txtLabel.requestFocus();
                        }
                    }
                });

                return cell;
            }

        });
    }
}
