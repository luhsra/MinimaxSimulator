package de.uni_hannover.sra.minimax_simulator.ui.tabs.project.memory.components;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;

import de.uni_hannover.sra.minimax_simulator.Main;
import net.miginfocom.swing.MigLayout;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.resources.Icons;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.common.AddressFormatter;
import de.uni_hannover.sra.minimax_simulator.ui.common.NullAwareIntFormatter;
import de.uni_hannover.sra.minimax_simulator.ui.common.components.JSpinnerEditor;
import de.uni_hannover.sra.minimax_simulator.ui.common.models.AddressSpinnerModel;
import de.uni_hannover.sra.minimax_simulator.ui.common.models.SpinnerNullModel;
import de.uni_hannover.sra.minimax_simulator.ui.common.models.WrapSpinnerModel;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.project.memory.MemoryImportWorker;

@Deprecated
public class MemoryImportPanel extends JPanel implements MemorySelectionListener
{
	private class OnClickFileChooser extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			JFileChooser chooser = new JFileChooser();
			int result = -1; //chooser.showOpenDialog(Application.getMainWindow());
			if (result == JFileChooser.APPROVE_OPTION)
			{
				updateFileSelection(chooser.getSelectedFile());
			}
		}
	}

	private class ImportAction extends AbstractAction
	{
		ImportAction(String label, Icon icon)
		{
			super(label, icon);
			setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			int result = -1; //JOptionPane.showConfirmDialog(Application.getMainWindow(),
				//_res.get("confirm.message"),
				//_res.get("confirm.title"), JOptionPane.OK_CANCEL_OPTION);

			if (result != JOptionPane.OK_OPTION)
				return;

			int address = (Integer) _addressSpinner.getValue();
			int size = (Integer) _sizeSpinner.getValue();
			UIUtil.executeWorker(new MemoryImportWorker(_memory, address, size,
				_currentFile, _res), _res.get("wait.title"), _res.get("wait.message"));
		}
	}

	private class PartialImportAction extends AbstractAction
	{
		PartialImportAction(String name)
		{
			super(name);
			setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			boolean state = !_sizeSpinner.isEnabled();

			_sizeSpinner.setEnabled(state);
			if (!state)
			{
				// set spinner to maximum value
				SpinnerModel model = _sizeSpinner.getModel();
				WrapSpinnerModel wrapModel = (WrapSpinnerModel) model;
				wrapModel.setValue(wrapModel.getMaxValue());
			}
		}
	}

	private final MachineMemory	_memory;

	private File				_currentFile;
	private final JTextField	_filenameField;
	private final JSpinner		_addressSpinner;
	private final JSpinner		_sizeSpinner;

	private final Action		_importAction;
	private final Action		_partialImportAction;

	private final TextResource	_res;

	private LayoutManager createLayout()
	{
		// labels -- spinners -- checkbox
		String cols = "[min!,left]rel[100lp:p:max, fill, right]";
		// file -- address -- bytes -- button
		String rows = "[min!]unrel[min!]rel[min!]unrel[min!]";
		return new MigLayout("fill", cols, rows);
	}

	public MemoryImportPanel(MachineMemory memory)
	{
		_memory = memory;

		_res = Main.getTextResource("project").using("memory.import");

		setLayout(createLayout());

		_filenameField = new JTextField(20);
		_filenameField.setEditable(false);
		_filenameField.addMouseListener(new OnClickFileChooser());

		_addressSpinner = new JSpinner(new AddressSpinnerModel(memory));
		_addressSpinner.setEditor(new JSpinnerEditor(_addressSpinner,
			new AddressFormatter(memory)));
		// _addressSpinner.setEnabled(false);
		_addressSpinner.setValue(0);

		_sizeSpinner = new JSpinner();
		_sizeSpinner.setEnabled(false);
		_sizeSpinner.setEditor(new JSpinnerEditor(_sizeSpinner,
			new NullAwareIntFormatter(10, false)));
		_sizeSpinner.setModel(new SpinnerNullModel());

		Icons icons = Icons.getInstance();
		_importAction = new ImportAction(_res.get("button.label"),
			icons.get(_res.get("button.icon")));
		_partialImportAction = new PartialImportAction(_res.get("partial"));

		add(new JLabel(_res.get("file")), "cell 0 0");
		add(_filenameField, "cell 1 0 2 1");

		add(new JLabel(_res.get("address")), "cell 0 1");
		add(_addressSpinner, "cell 1 1");

		add(new JLabel(_res.get("bytescount")), "cell 0 2");
		add(_sizeSpinner, "cell 1 2");
		add(new JCheckBox(_partialImportAction), "cell 1 2");

		add(new JButton(_importAction), "cell 0 3 2 1");

		setBorder(UIUtil.createGroupBorder(_res.get("title")));
	}

	void updateFileSelection(File file)
	{
		_currentFile = file;
		if (file == null)
		{
			_filenameField.setText("");
			_importAction.setEnabled(false);
			_sizeSpinner.setEnabled(false);
			_sizeSpinner.setModel(new SpinnerNullModel());

			_partialImportAction.setEnabled(false);
		}
		else
		{
			_filenameField.setText(_currentFile.getAbsolutePath());
			_importAction.setEnabled(true);

			int length = (int) Math.min(Integer.MAX_VALUE, file.length());

			// when set to length directly, the spinner does display 0 instead of the
			// value of the model...?
			_sizeSpinner.setModel(new WrapSpinnerModel(null, 0, length));
			_sizeSpinner.setValue(length);

			_partialImportAction.setEnabled(true);
		}
	}

	@Override
	public void onAddressSelection(int address)
	{
		_addressSpinner.setValue(address);
	}
}