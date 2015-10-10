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
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import de.uni_hannover.sra.minimax_simulator.Main;
import net.miginfocom.swing.MigLayout;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.resources.Icons;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.common.AddressFormatter;
import de.uni_hannover.sra.minimax_simulator.ui.common.components.JSpinnerEditor;
import de.uni_hannover.sra.minimax_simulator.ui.common.models.AddressSpinnerModel;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.project.memory.MemoryExportWorker;

@Deprecated
public class MemoryExportPanel extends JPanel implements MemorySelectionListener
{
	private class OnClickFileChooser extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			JFileChooser chooser = new JFileChooser();
			int result = -1; //chooser.showSaveDialog(Application.getMainWindow());
			if (result == JFileChooser.APPROVE_OPTION)
			{
				File file = chooser.getSelectedFile();
				if (file.exists())
				{
					int overwrite = -1; //JOptionPane.showConfirmDialog(
						//Application.getMainWindow(),
						//_res.format("overwrite.confirm.message", file.getName()),
						//_res.get("overwrite.confirm.title"), JOptionPane.OK_CANCEL_OPTION);

					if (overwrite != JOptionPane.OK_OPTION)
						return;
				}
				updateFileSelection(chooser.getSelectedFile());
			}
		}
	}

	private class ExportAction extends AbstractAction
	{
		ExportAction(String label, Icon icon)
		{
			super(label, icon);
			setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			Integer fromAddress = (Integer) _addressFromSpinner.getValue();
			Integer toAddress = (Integer) _addressToSpinner.getValue();
			if (fromAddress == null || toAddress == null)
				return;

			UIUtil.executeWorker(new MemoryExportWorker(_memory, fromAddress.intValue(), toAddress.intValue(), _currentFile, _res),
				_res.get("wait.title"), _res.get("wait.message"));
		}
	}

	private final MachineMemory	_memory;

	private File				_currentFile;
	private final JTextField	_filenameField;
	private final JSpinner		_addressFromSpinner;
	private final JSpinner		_addressToSpinner;

	private final Action		_exportAction;

	private final TextResource	_res;

	private LayoutManager createLayout()
	{
		// labels -- spinners
		String cols = "[min!,left]rel[100lp:p:max, fill, right]";
		// file -- address 1 -- address 2 -- button
		String rows = "[min!]unrel[min!]rel[min!]unrel[min!]";
		return new MigLayout("fill", cols, rows);
	}

	public MemoryExportPanel(MachineMemory memory)
	{
		_memory = memory;

		_res = Main.getTextResource("project").using("memory.export");

		setLayout(createLayout());

		_filenameField = new JTextField(20);
		_filenameField.setEditable(false);
		_filenameField.addMouseListener(new OnClickFileChooser());

		_addressFromSpinner = new JSpinner(new AddressSpinnerModel(memory));
		_addressFromSpinner.setEditor(new JSpinnerEditor(_addressFromSpinner,
			new AddressFormatter(memory)));
		_addressFromSpinner.setValue(0);

		_addressToSpinner = new JSpinner(new AddressSpinnerModel(memory));
		_addressToSpinner.setEditor(new JSpinnerEditor(_addressToSpinner,
			new AddressFormatter(memory)));
		_addressToSpinner.setValue(0);

		Icons icons = Icons.getInstance();
		_exportAction = new ExportAction(_res.get("button.label"),
			icons.get(_res.get("button.icon")));

		add(new JLabel(_res.get("file")), "cell 0 0");
		add(_filenameField, "cell 1 0");
		add(new JLabel(_res.get("address-from")), "cell 0 1");
		add(_addressFromSpinner, "cell 1 1");
		add(new JLabel(_res.get("address-to")), "cell 0 2");
		add(_addressToSpinner, "cell 1 2");
		add(new JButton(_exportAction), "cell 0 3 2 1");

		setBorder(UIUtil.createGroupBorder(_res.get("title")));
	}

	void updateFileSelection(File file)
	{
		_currentFile = file;
		if (file == null)
		{
			_filenameField.setText("");
			_exportAction.setEnabled(false);
		}
		else
		{
			_filenameField.setText(_currentFile.getAbsolutePath());
			_exportAction.setEnabled(true);
		}
	}

	@Override
	public void onAddressSelection(int address)
	{
		int addressDiff = 0;
		Integer from = (Integer) _addressFromSpinner.getValue();
		Integer to = (Integer) _addressToSpinner.getValue();
		if (from != null && to != null)
			addressDiff = to.intValue() - from.intValue();

		_addressFromSpinner.setValue(Integer.valueOf(address));
		_addressToSpinner.setValue(Integer.valueOf(address + addressDiff));
	}
}