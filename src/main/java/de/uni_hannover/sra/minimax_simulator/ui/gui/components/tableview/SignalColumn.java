package de.uni_hannover.sra.minimax_simulator.ui.gui.components.tableview;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.signal.DescriptionFactory;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.util.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.util.List;
import java.util.regex.Pattern;

/**
 * An extended {@link SignalTableColumn} for the UI representation of the {@link de.uni_hannover.sra.minimax_simulator.model.signal.SignalValue}s
 * of a {@link SignalRow}.
 *
 * @author Philipp Rohde
 */
public class SignalColumn extends SignalTableColumn {

    private SignalTable signalTable;
    private SignalRow signalRow;
    private int rowIndex;

    /**
     * Creates a {@link TableColumn} for the {@link de.uni_hannover.sra.minimax_simulator.model.signal.SignalValue}s of a {@link SignalRow}.
     *
     * @param label
     *          the text for the column header
     * @param id
     *          the column's ID
     * @param index
     *          the column index
     */
    public SignalColumn(String label, String id, int index) {
        super(label, id, index);

        setCellFactory(new Callback<TableColumn<ObservableList, String>, TableCell<ObservableList, String>>() {
            @Override
            public TableCell<ObservableList, String> call(TableColumn<ObservableList, String> param) {
                TableCell<ObservableList, String> cell = new TableCell<ObservableList, String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {

                        if (item == null) {
                            setGraphic(null);
                        } else if (!item.equals("-")) {
                            // set text of label according to the current column
                            MachineConfiguration mConfig = Main.getWorkspace().getProject().getMachineConfiguration();
                            String newText;
                            if (getId().equals("ALU_SELECT_A")) {
                                newText = Util.toBinaryAddress(Integer.parseInt(item), mConfig.getMuxSources(MuxType.A).size() - 1);
                            } else if (getId().equals("ALU_SELECT_B")) {
                                newText = Util.toBinaryAddress(Integer.parseInt(item), mConfig.getMuxSources(MuxType.B).size() - 1);
                            } else if (getId().equals("ALU_CTRL")) {
                                newText = Util.toBinaryAddress(Integer.parseInt(item), mConfig.getAluOperations().size() - 1);
                            } else {
                                newText = item;
                            }
                            setGraphic(new CenteredCellPane(newText));
                        } else {
                            setGraphic(new CenteredCellPane("-"));
                        }
                    }

                };

                cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getClickCount() == 2) {
                            rowIndex = cell.getTableView().getSelectionModel().getSelectedIndex();
                            signalTable = Main.getWorkspace().getProject().getSignalTable();
                            signalRow = signalTable.getRow(rowIndex);
                            ComboBox<String> cbRegister = new ComboBox<>();

                            ObservableList<String> data = FXCollections.observableArrayList();

                            // get choices according to table column
                            String id = cell.getTableColumn().getId();

                            switch (id) {
                                case "ALU_SELECT_A":
                                    List<MuxInput> sourcesA = Main.getWorkspace().getProject().getMachineConfiguration().getMuxSources(MuxType.A);

                                    data.add("-");
                                    for (int i = 0; i < sourcesA.size(); i++) {
                                        data.add(Util.toBinaryAddress(i, sourcesA.size() - 1) + " " + sourcesA.get(i).getName());
                                    }
                                    break;

                                case "ALU_SELECT_B":
                                    List<MuxInput> sourcesB = Main.getWorkspace().getProject().getMachineConfiguration().getMuxSources(MuxType.B);

                                    data.add("-");
                                    for (int i = 0; i < sourcesB.size(); i++) {
                                        data.add(Util.toBinaryAddress(i, sourcesB.size() - 1) + " " + sourcesB.get(i).getName());
                                    }
                                    break;

                                case "ALU_CTRL":
                                    List<AluOperation> ops = Main.getWorkspace().getProject().getMachineConfiguration().getAluOperations();

                                    data.add("-");
                                    for (int i = 0; i < ops.size(); i++) {
                                        data.add(Util.toBinaryAddress(i, ops.size() - 1) + " " + ops.get(i).getOperationName());
                                    }
                                    break;

                                case "MDR_SEL":
                                    data.addAll("-", "0 ALU.result", "1 MEM.DO");
                                    break;

                                case "MEM_CS":
                                    data.addAll("0 ", "1 enable");
                                    break;

                                case "MEM_RW":
                                    data.addAll("-", "0 write", "1 read");
                                    break;

                                default:
                                    data.addAll("0 ", "1 write");
                            }

                            // get the index of the currently set signal value
                            int indexCurrentValue;
                            if (signalRow.getSignalValues().containsKey(id)) {
                                indexCurrentValue = signalRow.getSignalValue(id) + 1;
                                if (Pattern.matches(".+\\.W", id)) {
                                    indexCurrentValue -= 1;
                                }
                            }
                            else {
                                indexCurrentValue = 0;
                            }


                            cbRegister.setItems(data);
                            cbRegister.getSelectionModel().select(indexCurrentValue);

                            cbRegister.valueProperty().addListener((obs, oldValue, newValue) -> {
                                if (newValue.startsWith("-")) {
                                    signalRow.setSignal(cell.getTableColumn().getId(), null);
                                }
                                else {
                                    String binary = newValue.substring(0, newValue.indexOf(" "));
                                    int code = Integer.parseInt(binary, 2);
                                    signalRow.setSignalValue(cell.getTableColumn().getId(), code);
                                }

                                DescriptionFactory dFac = signalTable.getDescriptionFactory();
                                signalRow.setDescription(dFac.createDescription(rowIndex, signalRow));
                                fireUpdateTable();
                            });

                            cbRegister.focusedProperty().addListener((obs, oldValue, newValue) -> {
                                if (!newValue) {
                                    fireUpdateTable();
/*                                    if (signalRow.getSignalValues().containsKey(id)) {
                                        //cell.setGraphic(new Label(String.valueOf(signalRow.getSignalValue(id))));
                                        // TODO: better update of the graphic
                                        fireUpdateTable();
                                    } else {
                                        cell.setGraphic(new Label("-"));
                                    }   */
                                }
                            });
                            cbRegister.setOnKeyReleased(new EventHandler<KeyEvent>() {
                                @Override
                                public void handle(KeyEvent evt) {
                                    if (KeyCode.ESCAPE == evt.getCode()) {
/*                                        if (signalRow.getSignalValues().containsKey(id)) {
                                            //cell.setGraphic(new Label(String.valueOf(signalRow.getSignalValue(id))));
                                            //cell.setText(String.valueOf(signalRow.getSignalValue(id)));
                                            // TODO: better update of the graphic
                                            fireUpdateTable();
                                        } else {
                                            cell.setGraphic(new Label("-"));
                                        }   */
                                        fireUpdateTable();
                                    }
                                }
                            });

                            cell.setGraphic(cbRegister);
                            cbRegister.requestFocus();
                            cbRegister.show();

                        }
                    }
                });

                return cell;
            }

        });
    }

    /**
     * Exchanges a {@link SignalRow} of a {@link SignalTable} with itself for notification of {@link de.uni_hannover.sra.minimax_simulator.model.signal.SignalTableListener}s.
     */
    private void fireUpdateTable() {
        signalTable.setSignalRow(rowIndex, signalRow);
        Main.getWorkspace().setProjectUnsaved();
    }
}
