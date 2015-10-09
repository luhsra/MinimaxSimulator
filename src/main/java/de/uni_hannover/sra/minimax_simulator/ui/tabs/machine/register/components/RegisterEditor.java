package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.register.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;
import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.Config;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterSize;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.common.AbstractDocumentListener;
import de.uni_hannover.sra.minimax_simulator.ui.common.LimitedLenghtDocument;

@Deprecated
class RegisterEditor extends JPanel
{
	private final TextResource			res	= Application.getTextResource("machine");

	private final MachineConfiguration	_config;
	private final JTextField			_name;
	private final JComboBox				_size;
	private final JTextArea				_description;
	private final JButton				_saveButton;

	private RegisterExtension			_register;

	public RegisterEditor(MachineConfiguration config)
	{
		_config = config;
		_register = null;

		_name = new JTextField(Config.EDITOR_MAX_REGISTER_LENGTH);
		_name.setDocument(new LimitedLenghtDocument(Config.EDITOR_MAX_REGISTER_LENGTH));

		_size = new JComboBox();
		_size.addItem(RegisterSize.BITS_32);
		_size.addItem(RegisterSize.BITS_24);
		_size.setRenderer(new RegisterSizeRenderer());
		_size.setEnabled(false);
		_description = new JTextArea();
		_description.setLineWrap(true);
		_description.setWrapStyleWord(true);
		_description.setLineWrap(true);

		_saveButton = UIUtil.loadButton(res, "register.save");

		JPanel nameContainer = UIUtil.wrapInBorderPanel(_name, new EmptyBorder(1, 1, 1,
			1), res.get("register.name.title"));
		JPanel sizeContainer = UIUtil.wrapInBorderPanel(_size, new EmptyBorder(1, 1, 1,
			1), res.get("register.size.title"));
		JPanel descriptionContainer = UIUtil.wrapInBorderPanel(_description,
			new EmptyBorder(1, 1, 1, 1), res.get("register.description.title"));

		// Rows: name field (min), size field (min), description field (fill), save button (min)
		setLayout(new MigLayout("fill", "[100lp:300lp:max,fill]",
			"[min]rel[min]rel[300lp,fill]unrel[min]"));
		add(nameContainer, "cell 0 0");
		add(sizeContainer, "cell 0 1");
		add(descriptionContainer, "cell 0 2, growx");
		add(_saveButton, "cell 0 3");

		updateComponents();
		updateButton();

		DocumentListener doc = new AbstractDocumentListener()
		{
			@Override
			public void documentChanged(DocumentEvent e)
			{
				updateButton();
			}
		};
		_name.getDocument().addDocumentListener(doc);
		_description.getDocument().addDocumentListener(doc);

		_size.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent event)
			{
				if (event.getStateChange() == ItemEvent.SELECTED)
				{
					updateButton();
				}
			}
		});

		_saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int index = _config.getRegisterExtensions().indexOf(_register);

				_register = new RegisterExtension(_name.getText().trim(),
					(RegisterSize) _size.getSelectedItem(),
					_description.getText(),
					_register.isExtended());

				// Just to notify listeners, since we change the original RegisterExtension object
				_config.setRegisterExtension(index, _register);

				updateButton();

				Application.getWorkspace().setProjectUnsaved();
			}
		});
	}

	public void focusFirstField()
	{
		_name.requestFocusInWindow();
		_name.selectAll();
	}

	protected void updateButton()
	{
		UIUtil.doEnable(isInputValid() && isUnsaved(), _saveButton);
	}

	private boolean isInputValid()
	{
		String newName = _name.getText();

		if (newName.isEmpty() || _size.getSelectedItem() == null)
			return false;

		for (RegisterExtension otherReg : _config.getRegisterExtensions())
		{
			if (!_register.getName().equals(otherReg.getName()))
			{
				if (newName.equalsIgnoreCase(otherReg.getName()))
					return false;
			}
		}

		return true;
	}

	private boolean isUnsaved()
	{
		if (_register == null)
			return false;

		if (!_register.getName().equals(_name.getText()))
			return true;

		if (_register.getSize() != _size.getSelectedItem())
			return true;

		if (!_register.getDescription().equals(_description.getText()))
			return true;

		return false;
	}

	public void setRegister(RegisterExtension register)
	{
		_register = register;
		updateComponents();
	}

	public RegisterExtension getRegister()
	{
		return _register;
	}

	public boolean isEditable()
	{
		return _register != null && _register.isExtended();
	}

	private void updateComponents()
	{
		UIUtil.doEnable(_register != null, _name, _description);

		if (isEditable())
		{
			_name.setEditable(true);
			//_size.setEnabled(true);
			_description.setEditable(true);
		}
		else
		{
			_name.setEditable(false);
			//_size.setEnabled(false);
			_description.setEditable(false);
		}

		if (_register == null)
		{
			_name.setText("");
			_size.setSelectedIndex(-1);
			_description.setText("");
		}
		else
		{
			_name.setText(_register.getName());
			_size.setSelectedItem(_register.getSize());
			_description.setText(_register.getDescription());
		}
	}
}