package de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.util.Util;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;

/**
 * The {@code ValueUpdateDialog} is a dialog for updating a value e.g. a value stored in memory.<br>
 * It contains a {@link TextField} for entering the new value and a {@link Button} for swapping the input mode.<br>
 * <br>
 * Supported input modes are decimal and hexadecimal.
 *
 * @author Philipp Rohde
 */
public abstract class ValueUpdateDialog extends FXDialog {

    /** The input mode. */
    protected enum Mode {
        HEX {
            @Override
            public String toString(ValueUpdateDialog instance, Integer value) {
                return String.format(instance.hexFormat, value);
            }

            @Override
            public Integer decode(String value) {
                try {
                    long lon = Long.parseLong(value, 16);
                    if (lon > 0xFFFFFFFFL) {
                        return null;
                    }
                    return (int) lon;
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        },
        BIN {
            @Override
            public String toString(ValueUpdateDialog instance, Integer value) {
                return Util.to32BitBinary(value).replaceAll(" ", "");
            }

            @Override
            public Integer decode(String value) {
                try {
                    if (!value.matches("[01]{1,32}")) {
                        return null;
                    }
                    long lon = Long.parseLong(value, 2);
                    return (int) lon;
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        },
        DEC {
            @Override
            public String toString(ValueUpdateDialog instance, Integer value) {
                return Integer.toString(value);
            }

            @Override
            public Integer decode(String value) {
                try {
                    return Integer.valueOf(value, 10);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        };

        /**
         * Converts the specified value to {@code String} according to the currently set mode.
         *
         * @param instance
         *          the {@code ValueUpdateDialog} instance
         * @param value
         *          the value to convert
         * @return
         *          the {@code String} representation
         */
        public abstract String toString(ValueUpdateDialog instance, Integer value);

        /**
         * Decodes the specified {@code String} to {@code Integer} according to the currently set mode.
         *
         * @param value
         *          the value to decode
         * @return
         *          the decoded {@code Integer}
         */
        public abstract Integer decode(String value);

        private static Mode[] vals = values();

        /**
         * Returns the next input mode.
         *
         * @return
         *          the next input {@code Mode}
         */
        public Mode next() {
            return vals[(this.ordinal() + 1) % vals.length];
        }
    }

    protected final Text messageText;
    protected final Text modeText;
    protected final Text currentModeText;
    protected final Button swapMode;
    protected final Button okButton;
    protected final ButtonType okButtonType;
    protected final String hexFormat;
    protected final TextField field;

    protected final TextResource res;

    private Mode mode;

    /**
     * Constructs a new {@code ValueUpdateDialog} with the specified starting value.
     *
     * @param currentValue
     *          the value at the moment of opening the dialog
     * @param binaryInputAllowed
     *          whether binary input is allowed or not
     */
    protected ValueUpdateDialog(int currentValue, boolean binaryInputAllowed) {
        super(AlertType.NONE, null, null);

        res = Main.getTextResource("project").using("memory.update");

        hexFormat = Util.createHexFormatString(32, false);
        mode = Mode.DEC;

        field = new TextField();
        double width = 285;
        field.setMinWidth(width);
        field.setMaxWidth(width);
        field.setPrefWidth(width);

        field.setText(mode.toString(this, currentValue));

        swapMode = new Button();
        swapMode.setTooltip(new Tooltip(res.get("swapmode.tooltip")));
        swapMode.setGraphic(new ImageView("images/" + res.get("swapmode.icon")));

        messageText = new Text();
        messageText.setFontSmoothingType(FontSmoothingType.LCD);
        messageText.setId("dialog-label");

        okButtonType = new ButtonType(res.get("ok"), ButtonBar.ButtonData.OK_DONE);

        modeText = new Text(res.get("mode.label"));
        modeText.setFontSmoothingType(FontSmoothingType.LCD);
        modeText.setId("dialog-label");
        currentModeText = new Text(res.get("mode.dec"));
        currentModeText.setId("boldred");                               // set text bold and red via CSS
        currentModeText.setFontSmoothingType(FontSmoothingType.LCD);
        HBox modeBox = new HBox(3);
        modeBox.getChildren().addAll(modeText, currentModeText);
        updateLabelMode();

        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        pane.add(messageText, 0, 0, 2, 1);
        pane.add(field, 0, 2);
        pane.add(swapMode, 1, 2);
        pane.add(modeBox, 0, 1, 2, 1);

        this.getDialogPane().setContent(pane);
        this.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        swapMode.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                Mode newMode = mode.next();
                // jump over to DEC if next mode would be BIN but binary input is not allowed
                if (!binaryInputAllowed && (newMode == Mode.BIN)) {
                    newMode = newMode.next();
                }
                updateTextFieldMode(newMode);
                updateLabelMode();
            }
        });

        okButton = (Button) this.getDialogPane().lookupButton(okButtonType);

        // invalid input should disable the OK button
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean shouldEnable = mode.decode(field.getText()) != null;

            if (okButton.isDisabled() == shouldEnable) {
                okButton.setDisable(!shouldEnable);
            }
        });

        // due to the use of own buttons we need an resultConverter for getting the user's choice
        this.setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                Integer value = mode.decode(field.getText());
                if (value != null) {
                    setValue(value);
                    return ButtonType.OK;
                }
            }
            return ButtonType.CANCEL;
        });

    }

    /**
     * Sets the value to the specified value.
     *
     * @param value
     *          the new value
     */
    protected abstract void setValue(int value);

    /**
     * Changes the input mode of the {@code TextField} to the specified mode
     * and converts its value to the new numerical system.
     *
     * @param mode
     *          the new mode
     */
    private void updateTextFieldMode(Mode mode) {
        if (this.mode == mode) {
            return;
        }

        Integer value = this.mode.decode(field.getText().trim());
        if (value == null) {
            value = 0;
        }
        this.mode = mode;
        field.setText(this.mode.toString(this, value));
    }

    /**
     * Updates the text of the current mode {@code Label}.
     */
    private void updateLabelMode() {
        String currentMode = "";
        switch (mode) {
            case DEC:
                currentMode = res.get("mode.dec");
                break;
            case HEX:
                currentMode = res.get("mode.hex");
                break;
            case BIN:
                currentMode = res.get("mode.bin");
                break;
            default:
                break;
        }
        currentModeText.setText(currentMode);
    }
}