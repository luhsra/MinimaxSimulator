package de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.ConditionalJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.DefaultJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.UnconditionalJump;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.gui.components.NumberTextField;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.JumpLabelSelector;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A {@code JumpTargetDialog} is basically an {@link FXDialog} with customized content.<br>
 * The {@code JumpTargetDialog} is the UI used to change the {@link Jump} of a {@link SignalRow}.
 *
 * @author Philipp Rohde
 */
public class JumpTargetDialog extends FXDialog {

    private final SignalTable table;
    private final SignalRow row;
    private final int rowIndex;

    private RadioButton rBtnNext;
    private RadioButton rBtnUncond;
    private RadioButton rBtnCond;

    private TextField txtUncond = new NumberTextField();
    private TextField txtCond0 = new NumberTextField();
    private TextField txtCond1 = new NumberTextField();

    private ComboBox cbUncond;
    private ComboBox cbCond0 = new ComboBox();
    private ComboBox cbCond1 = new ComboBox();

    private ButtonType okButtonType;

    private final TextResource res;

    /**
     * Constructs a new {@code JumpTargetDialog} for the specified {@link SignalRow}
     * of the specified {@link SignalTable}.
     *
     * @param table
     *          the {@code SignalTable} the simulator currently works with
     * @param row
     *          the {@code SignalRow} that is currently being edited
     * @param rowIndex
     *          the index of the {@code SignalRow}
     */
    public JumpTargetDialog(SignalTable table, SignalRow row, int rowIndex) {
        super(AlertType.NONE, "", "");
        res = Main.getTextResource("machine").using("signal.jump");

        this.table = table;
        this.row = row;
        this.rowIndex = rowIndex;

        createUI();

        Jump jump = row.getJump();
        if (jump instanceof DefaultJump) {
            rBtnNext.setSelected(true);
        }
        else if (jump instanceof UnconditionalJump) {
            rBtnUncond.setSelected(true);
            UnconditionalJump uJump = (UnconditionalJump) jump;
            txtUncond.setText(String.valueOf(uJump.getTargetRow()));
        }
        else if (jump instanceof ConditionalJump) {
            rBtnCond.setSelected(true);
            ConditionalJump cJump = (ConditionalJump) jump;
            txtCond0.setText(String.valueOf(cJump.getTargetRow(0)));
            txtCond1.setText(String.valueOf(cJump.getTargetRow(1)));
        }

    }

    /**
     * Gets the current target row of an {@link UnconditionalJump}.
     *
     * @return
     *          the current target row
     */
    private int getCurrentUnconditionalRow() {
        Jump jump = row.getJump();
        if (jump instanceof UnconditionalJump) {
            UnconditionalJump uJump = (UnconditionalJump) jump;
            return uJump.getTargetRow();
        }
        return -1;
    }

    /**
     * Gets the current target row of a {@link ConditionalJump}.
     *
     * @param condition
     *          the condition of the jump for which the current target row should be returned
     * @return
     *          the current target row
     */
    private int getCurrentConditionalRow(int condition) {
        Jump jump = row.getJump();
        if (jump instanceof ConditionalJump) {
            ConditionalJump cJump = (ConditionalJump) jump;
            return cJump.getTargetRow(condition);
        }
        return -1;
    }

    /**
     * Sets up the UI of the dialog.
     */
    private void createUI() {
        okButtonType = new ButtonType(res.get("ok"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType(res.get("cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);

        rBtnNext = new RadioButton(res.get("default"));
        rBtnUncond = new RadioButton(res.get("unconditional"));
        rBtnCond = new RadioButton(res.get("conditional"));

        ToggleGroup tgrp = new ToggleGroup();
        rBtnNext.setToggleGroup(tgrp);
        rBtnUncond.setToggleGroup(tgrp);
        rBtnCond.setToggleGroup(tgrp);

        List<TextField> textFields = new ArrayList<>(Arrays.asList(txtUncond, txtCond0, txtCond1));
        for (TextField tf : textFields) {
            tf.setPrefWidth(60);
            tf.setMaxWidth(60);

            tf.textProperty().addListener((observable, oldValue, newValue) -> validateTargetRow());
        }

        cbUncond = new JumpLabelSelector(txtUncond, table, getCurrentUnconditionalRow());
        cbCond1 = new JumpLabelSelector(txtCond1, table, getCurrentConditionalRow(1));
        cbCond0 = new JumpLabelSelector(txtCond0, table, getCurrentConditionalRow(0));

        txtUncond.setDisable(true);
        txtCond0.setDisable(true);
        txtCond1.setDisable(true);
        cbUncond.setDisable(true);
        cbCond0.setDisable(true);
        cbCond1.setDisable(true);

        rBtnUncond.selectedProperty().addListener((obs, oldValue, newValue) -> {
            validateTargetRow();
            if (newValue) {
                txtUncond.setDisable(false);
                cbUncond.setDisable(false);
            }
            else {
                txtUncond.setDisable(true);
                cbUncond.setDisable(true);
            }
        });

        rBtnCond.selectedProperty().addListener((obs, oldValue, newValue) -> {
            validateTargetRow();
            if (newValue) {
                txtCond0.setDisable(false);
                cbCond0.setDisable(false);
                txtCond1.setDisable(false);
                cbCond1.setDisable(false);
            }
            else {
                txtCond0.setDisable(true);
                cbCond0.setDisable(true);
                txtCond1.setDisable(true);
                cbCond1.setDisable(true);
            }
        });

        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        pane.add(rBtnNext, 0, 0);
        pane.add(rBtnUncond, 0, 1);
        pane.add(rBtnCond, 0, 2);
        pane.add(txtUncond, 1, 1);
        pane.add(createCondLabel(1, txtCond1), 1, 2);
        pane.add(createCondLabel(0, txtCond0), 1, 3);
        pane.add(cbUncond, 2, 1);
        pane.add(cbCond1, 2, 2);
        pane.add(cbCond0, 2, 3);

        this.getDialogPane().setContent(pane);
        this.getDialogPane().getButtonTypes().addAll(cancelButtonType, okButtonType);
    }

    /**
     * Enables or disables the OK button according to the current state.
     */
    private void validateTargetRow() {
        Button btnOK = (Button) this.getDialogPane().lookupButton(okButtonType);
        if (rBtnUncond.isSelected()) {
            if ("".equals(txtUncond.getText())) {
                btnOK.setDisable(true);
            }
            else {
                btnOK.setDisable(false);
            }
        }
        else if (rBtnCond.isSelected()) {
            if ("".equals(txtCond0.getText()) || "".equals(txtCond1.getText())) {
                btnOK.setDisable(true);
            }
            else {
                btnOK.setDisable(false);
            }
        }
        else {
            // rBtnNext is selected
            btnOK.setDisable(false);
        }
    }

    /**
     * Creates a {@link GridPane} with a {@link Label} and a {@link TextField}.
     *
     * @param condition
     *          the number to use as text for the {@code Label}
     * @param txtCondition
     *          the {@code TextField} to add to the {@code GridPane}
     * @return
     *          the created {@code GridPane}
     */
    private GridPane createCondLabel(int condition, TextField txtCondition) {
        GridPane grid = new GridPane();
        Label lbl = new Label(String.valueOf(condition));

        grid.add(lbl, 0, 0);
        grid.add(txtCondition, 1, 0);
        grid.setHgap(10);
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setFillWidth(true);
        columnConstraints.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().add(columnConstraints);
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setFillHeight(true);
        rowConstraints.setVgrow(Priority.ALWAYS);
        grid.getRowConstraints().add(rowConstraints);
        grid.setHalignment(lbl, HPos.CENTER);
        grid.setValignment(lbl, VPos.CENTER);

        return grid;
    }

    /**
     * Shows the dialog and applies the changes.
     */
    public void showAndApply() {
        if (this.getChoice().equals(okButtonType)) {
            Jump newJump = null;
            if (rBtnNext.isSelected()) {
                newJump = DefaultJump.INSTANCE;
            }
            else if (rBtnUncond.isSelected()) {
                try {
                    int targetRow = Integer.parseInt(txtUncond.getText());
                    newJump = new UnconditionalJump(targetRow);
                } catch (NumberFormatException nfe) {
                    // ok button should not be enabled
                    return;
                }
            }
            else if (rBtnCond.isSelected()) {
                try {
                    int targetRow0 = Integer.parseInt(txtCond0.getText());
                    int targetRow1 = Integer.parseInt(txtCond1.getText());
                    newJump = new ConditionalJump(targetRow0, targetRow1);
                } catch (NumberFormatException nfe) {
                    // ok button should not be enabled
                    return;
                }
            }
            this.row.setJump(newJump);
            this.table.setSignalRow(this.rowIndex, this.row);
            Main.getWorkspace().setProjectUnsaved();
        }
    }

}
