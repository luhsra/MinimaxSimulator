package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.mux.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.ConstantMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.NullMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.RegisterMuxInput;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.common.Disposable;
import de.uni_hannover.sra.minimax_simulator.ui.common.NullAwareIntFormatter;
import de.uni_hannover.sra.minimax_simulator.ui.common.components.JSpinnerEditor;
import de.uni_hannover.sra.minimax_simulator.ui.common.models.NullAwareIntegerSpinnerModel;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.mux.model.MuxSourceComboModel;

class MuxInputEditor extends JPanel implements Disposable
{
	private MuxType						_mux;
	private MuxInput					_source;
	private int							_index;

	private final MachineConfiguration	_config;

	private final JRadioButton			_radioConstant;
	private final JRadioButton			_radioRegister;

	private final ButtonGroup			_buttonGroup;

	private final JComboBox				_registerBox;
	private final MuxSourceComboModel	_registerModel;

	private final JSpinner				_decimalField;
	private final JSpinner				_hexaField;
	private final SpinnerModel			_numberModel;

	private final JButton				_saveButton;

	public MuxInputEditor(MachineConfiguration config)
	{
		_config = config;

		TextResource res = Application.getTextResource("machine");

		_radioConstant = new JRadioButton(res.get("mux.radio.constant"));
		_radioRegister = new JRadioButton(res.get("mux.radio.register"));

		_saveButton = UIUtil.loadButton(res, "mux.save");
		_saveButton.setEnabled(false);

		_buttonGroup = new ButtonGroup();
		_buttonGroup.add(_radioConstant);
		_buttonGroup.add(_radioRegister);

		_registerModel = new MuxSourceComboModel(config);
		_registerBox = new JComboBox(_registerModel);
		_registerBox.setRenderer(new RegisterNameRenderer());

		_numberModel = new NullAwareIntegerSpinnerModel();

		_decimalField = new JSpinner(_numberModel);
		_hexaField = new JSpinner(_numberModel);
		_decimalField.setEditor(new JSpinnerEditor(_decimalField,
			new NullAwareIntFormatter(10, true)));
		_hexaField.setEditor(new JSpinnerEditor(_hexaField, new NullAwareIntFormatter(16,
			false)));

		JLabel decimalLabel = new JLabel(res.get("mux.input.decimal"));
		JLabel hexaLabel = new JLabel(res.get("mux.input.hexadecimal"));

		// two columns for constant text field label, "Hex", "Dec"
		setLayout(new MigLayout("fillx", "[150lp:pref:max, grow, fill]",
			"[min!]rel[min!]unrel[min!]rel[min!]rel[min!]30px[grow]"));

		add(_radioRegister, "cell 0 0");
		add(_registerBox, "cell 0 1");

		add(_radioConstant, "cell 0 2");
		add(decimalLabel, "cell 0 3");
		add(_decimalField, "cell 0 3, grow");
		add(hexaLabel, "cell 0 4");
		add(_hexaField, "cell 0 4, grow");

		add(_saveButton, "cell 0 5");

		resetComponents();

		_config.addMachineConfigListener(_registerModel);

		ActionListener onRadioChange = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				updateComponents();
				updateButton();
			}
		};

		_radioConstant.addActionListener(onRadioChange);
		_radioRegister.addActionListener(onRadioChange);

		_numberModel.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				updateButton();
			}
		});

		_registerBox.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				updateButton();
			}
		});

		_saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				doSave();
			}
		});
	}

	public MuxInput getSource()
	{
		return _source;
	}

	public void setSource(MuxType mux, MuxInput source, int index)
	{
		_mux = mux;
		_source = source;
		_index = index;
		resetComponents();
		updateButton();
	}

	private void resetComponents()
	{
		if (_source == null)
		{
			UIUtil.doDisable(_radioConstant, _radioRegister, _decimalField, _hexaField,
				_registerBox);
			_buttonGroup.clearSelection();
			_numberModel.setValue(null);
			_registerBox.setSelectedItem(null);
		}
		else
		{
			UIUtil.doEnable(_radioConstant, _radioRegister);

			if (_source instanceof ConstantMuxInput)
			{
				_radioConstant.setSelected(true);
				_radioRegister.setSelected(false);

				UIUtil.doDisable(_registerBox);
				UIUtil.doEnable(_decimalField, _hexaField);

				int constant = ((ConstantMuxInput) _source).getConstant();
				_numberModel.setValue(constant);
				_registerBox.setSelectedItem(null);
			}
			else if (_source instanceof RegisterMuxInput)
			{
				_radioRegister.setSelected(true);
				_radioConstant.setSelected(false);

				UIUtil.doEnable(_registerBox);
				UIUtil.doDisable(_decimalField, _hexaField);

				_numberModel.setValue(null);
				_registerBox.setSelectedItem(_source);
			}
			else if (_source instanceof NullMuxInput)
			{
				_buttonGroup.clearSelection();

				UIUtil.doDisable(_registerBox);
				UIUtil.doDisable(_decimalField, _hexaField);
				_registerBox.setSelectedItem(null);
				_numberModel.setValue(null);
			}
		}
	}

	private void updateComponents()
	{
		if (_radioConstant.isSelected())
		{
			UIUtil.doDisable(_registerBox);
			UIUtil.doEnable(_decimalField, _hexaField);

			int constant = 0;
			if (_source instanceof ConstantMuxInput)
				constant = ((ConstantMuxInput) _source).getConstant();

			_numberModel.setValue(constant);
			_registerBox.setSelectedItem(null);
		}
		else if (_radioRegister.isSelected())
		{
			UIUtil.doEnable(_registerBox);
			UIUtil.doDisable(_decimalField, _hexaField);

			MuxInput selected = null;
			// RegisterExtension register = null;
			if (_source instanceof RegisterMuxInput)
				selected = _source;

			_registerBox.setSelectedItem(selected);
			_numberModel.setValue(null);
		}
	}

	protected void updateButton()
	{
		boolean isValid = isInputValid();
		boolean isUnsaved = isUnsaved();

		boolean saveEnabled = isValid && isUnsaved;
		if (_saveButton.isEnabled() != saveEnabled)
			_saveButton.setEnabled(saveEnabled);
	}

	private boolean isInputValid()
	{
		if (_radioConstant.isSelected())
		{
			if (_numberModel.getValue() == null)
				return false;
			return true;
		}
		else if (_radioRegister.isSelected())
		{
			if (_registerBox.getSelectedItem() == null)
				return false;
			return true;
		}

		return false;
	}

	private boolean isUnsaved()
	{
		if (_source == null)
			return false;

		if (_source instanceof ConstantMuxInput && !_radioConstant.isSelected())
			return true;

		if (_source instanceof RegisterMuxInput && !_radioRegister.isSelected())
			return true;

		if (_source instanceof NullMuxInput
			&& (_radioConstant.isSelected() || _radioRegister.isSelected()))
			return true;

		if (_source instanceof ConstantMuxInput)
		{
			Integer value = ((ConstantMuxInput) _source).getConstant();
			if (!value.equals(_numberModel.getValue()))
				return true;
		}
		else if (_source instanceof RegisterMuxInput)
		{
			// RegisterExtension register = ((RegisterMuxInput) _source).getRegister();
			if (_source != _registerBox.getSelectedItem())
				return true;
		}

		return false;
	}

	protected void doSave()
	{
		MuxInput input;
		if (_radioConstant.isSelected())
		{
			Object i = _numberModel.getValue();
			if (i instanceof Integer)
				input = new ConstantMuxInput((Integer) _numberModel.getValue());
			else
				return;
		}
		else if (_radioRegister.isSelected())
		{
			Object register = _registerBox.getSelectedItem();
			if (register instanceof MuxInput)
				input = (MuxInput) register;
			else
				return;
		}
		else
			return;

		_source = input;
		_config.setMuxSource(_mux, _index, input);

		updateButton();

		Application.getWorkspace().setProjectUnsaved();
	}

	@Override
	public void dispose()
	{
		_config.removeMachineConfigListener(_registerModel);
	}
}