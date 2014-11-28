package de.uni_hannover.sra.minimax_simulator.ui.common.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

import net.miginfocom.swing.MigLayout;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.common.AbstractDocumentListener;
import de.uni_hannover.sra.minimax_simulator.util.Util;

public abstract class ValueUpdateDialog extends JDialog
{
	protected enum Mode
	{
		HEX
		{
			@Override
			public String toString(ValueUpdateDialog instance, Integer value)
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
			public String toString(ValueUpdateDialog instance, Integer value)
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

		public abstract String toString(ValueUpdateDialog instance, Integer value);

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
				dispose();
			}
		}
	}

	protected abstract void setValue(int value);

	protected final JLabel			_messageLabel;
	protected final JButton			_swapMode;
	protected final JButton			_okButton;
	protected final JButton			_cancelButton;
	protected final String			_hexFormat;
	protected final JTextField		_field;
	private final ActionListener	_okAction	= new OkAction();

	private Mode					_mode;

	public ValueUpdateDialog(int currentValue)
	{
		setLayout(new MigLayout("", "[]", "[]unrel[]unrel[]"));
		setModalityType(ModalityType.APPLICATION_MODAL);

		_hexFormat = Util.createHexFormatString(32, false);
		_mode = Mode.DEC;

		_field = new JTextField(10);
		_field.setText(_mode.toString(this, currentValue));

		_swapMode = new JButton();

		_messageLabel = new JLabel();
		add(_messageLabel, "cell 0 0");
		add(_field, "cell 0 1");
		add(_swapMode, "cell 0 1");

		_okButton = new JButton();
		_cancelButton = new JButton();
		add(_okButton, "cell 0 2");
		add(_cancelButton, "cell 0 2");

		UIUtil.closeOnEscapePressed(this);

		_field.getDocument().addDocumentListener(new AbstractDocumentListener()
		{
			@Override
			public void documentChanged(DocumentEvent e)
			{
				boolean shouldEnable = _mode.decode(_field.getText()) != null;
				if (_okButton.isEnabled() != shouldEnable)
					_okButton.setEnabled(shouldEnable);
			}
		});
		_field.addActionListener(_okAction);

		_swapMode.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Mode newMode = _mode == Mode.DEC ? Mode.HEX : Mode.DEC;
				updateTextFieldMode(newMode);
			}
		});

		_cancelButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});

		_okButton.addActionListener(_okAction);
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