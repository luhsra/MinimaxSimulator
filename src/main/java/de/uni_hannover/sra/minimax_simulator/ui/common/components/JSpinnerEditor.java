package de.uni_hannover.sra.minimax_simulator.ui.common.components;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.uni_hannover.sra.minimax_simulator.ui.common.FillLayout;

public class JSpinnerEditor extends JPanel implements ChangeListener,
		PropertyChangeListener
{
	private final JSpinner				_spinner;
	private final JFormattedTextField	_text;

	public JSpinnerEditor(JSpinner spinner, AbstractFormatter formatter)
	{
		_spinner = spinner;

		_text = new JFormattedTextField(formatter);
		_text.setEditable(true);

		add(_text);
		setLayout(FillLayout.INSTANCE);

		spinner.addChangeListener(this);
		_text.addPropertyChangeListener(this);
		_text.setValue(spinner.getValue());
	}

	@Override
	public void stateChanged(ChangeEvent evt)
	{
		JSpinner spinner = (JSpinner) evt.getSource();
		Integer value = (Integer) spinner.getValue();

		_text.setValue(value);
	}

	@Override
	public void propertyChange(PropertyChangeEvent e)
	{
		Object source = e.getSource();
		String name = e.getPropertyName();
		if ((source instanceof JFormattedTextField) && "value".equals(name))
		{
			Object lastValue = _spinner.getValue();

			// Try to set the new value
			try
			{
				_spinner.setValue(_text.getValue());
			}
			catch (IllegalArgumentException iae)
			{
				// SpinnerModel didn't like new value, reset
				try
				{
					((JFormattedTextField) source).setValue(lastValue);
				}
				catch (IllegalArgumentException iae2)
				{
					// Still bogus, nothing else we can do, the
					// SpinnerModel and JFormattedTextField are now out
					// of sync.
				}
			}
		}
	}
}
