package de.uni_hannover.sra.minimax_simulator.ui.common.dialogs;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.util.Util;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class FxValueUpdateDialog extends Alert
{
	protected enum Mode
	{
		HEX
		{
			@Override
			public String toString(FxValueUpdateDialog instance, Integer value)
			{
				return String.format(instance._hexFormat, value);
			}

			@Override
			public Integer decode(String value)
			{
				try
				{
					long lon = Long.parseLong(value, 16);
					if (lon > 0xFFFFFFFFL)
						return null;
					return (int) lon;
				}
				catch (NumberFormatException e)
				{
					return null;
				}
			}
		},
		DEC
		{
			@Override
			public String toString(FxValueUpdateDialog instance, Integer value)
			{
				return Integer.toString(value);
			}

			@Override
			public Integer decode(String value)
			{
				try
				{
					return Integer.valueOf(value, 10);
				}
				catch (NumberFormatException e)
				{
					return null;
				}
			}
		};

		public abstract String toString(FxValueUpdateDialog instance, Integer value);

		public abstract Integer decode(String value);
	}

	private class OkAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Integer value = _mode.decode(_field.getText());
			if (value != null)
			{
				setValue(value.intValue());
				//dispose();
				close();
			}
		}
	}

	protected abstract void setValue(int value);

	protected final Label			_messageLabel;
	protected final Button 			_swapMode;
	protected final Button			_okButton;
	protected final ButtonType		_okButtonType;
	protected final String			_hexFormat;
	protected final TextField 		_field;

	private Mode					_mode;

	// TODO: show current mode
	public FxValueUpdateDialog(int currentValue)
	{
		super(AlertType.NONE);

		// for setting the icon of the application to the dialog
		this.initOwner(Main.getPrimaryStage());

		_hexFormat = Util.createHexFormatString(32, false);
		_mode = Mode.DEC;

		this.setHeaderText(null);

		_field = new TextField();

		_field.setText(_mode.toString(this, currentValue));

		_swapMode = new Button();

		_messageLabel = new Label();

		_okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);

		//UIUtil.closeOnEscapePressed(this);

		GridPane pane = new GridPane();
		pane.setHgap(10);
		pane.setVgap(10);
		pane.add(_messageLabel, 0, 0, 2, 1);
		pane.add(_field, 0, 1);
		pane.add(_swapMode, 1, 1);

		this.getDialogPane().setContent(pane);
		this.getDialogPane().getButtonTypes().addAll(_okButtonType, ButtonType.CANCEL);

		_swapMode.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
					Mode newMode = _mode == Mode.DEC ? Mode.HEX : Mode.DEC;
					updateTextFieldMode(newMode);
				}
			}
		});

		_okButton = (Button) this.getDialogPane().lookupButton(_okButtonType);

		_field.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				boolean shouldEnable = _mode.decode(_field.getText()) != null;

				if (_okButton.isDisabled() != !shouldEnable) {
					_okButton.setDisable(!shouldEnable);
				}
			}
		});

		this.setResultConverter(dialogButton -> {
			if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
				Integer value = _mode.decode(_field.getText());
				if (value != null) {
					setValue(value.intValue());
				}
			}
			return null;
		});

	}

	private void updateTextFieldMode(Mode mode)
	{
		if (_mode == mode)
			return;

		Integer value = _mode.decode(_field.getText().trim());
		if (value == null)
			value = Integer.valueOf(0);
		_mode = mode;
		_field.setText(_mode.toString(this, value));
	}
}