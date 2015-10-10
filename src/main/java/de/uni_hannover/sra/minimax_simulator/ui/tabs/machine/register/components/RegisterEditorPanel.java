package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.register.components;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.uni_hannover.sra.minimax_simulator.Main;
import net.miginfocom.swing.MigLayout;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterSize;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import de.uni_hannover.sra.minimax_simulator.resources.Icons;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.common.Disposable;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.common.models.AbstractRegisterTableModel;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.common.models.TwoSidedListSelectionListener;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.register.models.DefaultRegisterTableModel;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.register.models.MachineRegisterTableModel;

@Deprecated
public class RegisterEditorPanel extends JPanel implements Disposable
{
	private class AddAction extends AbstractAction
	{
		public AddAction(String title, Icon icon)
		{
			super(title, icon);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			RegisterExtension register = createNewRegister();

			_config.addRegisterExtension(register);

			// select fresh register in tables
			int newIndex = _extendedModel.indexOf(register);
			_basicTable.getSelectionModel().clearSelection();
			_extendedTable.getSelectionModel().setSelectionInterval(newIndex, newIndex);

			_registerPanel.focusFirstField();

			Main.getWorkspace().setProjectUnsaved();
		}
	}

	private class RemoveAction extends AbstractAction
	{
		public RemoveAction(String title, Icon icon)
		{
			super(title, icon);
			setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			int row = _extendedTable.getSelectedRow();
			if (row == -1)
				return;

			RegisterExtension register = _extendedModel.get(row);

			int choice = -1; //JOptionPane.showConfirmDialog(Application.getMainWindow(),
				//res.format("register.dialog.delete.message", register.getName()),
				//res.get("register.dialog.delete.title"), JOptionPane.OK_CANCEL_OPTION,
				//JOptionPane.WARNING_MESSAGE);
			if (choice == JOptionPane.OK_OPTION)
			{
				_config.removeRegisterExtension(row);
				Main.getWorkspace().setProjectUnsaved();

				if (_extendedTable.getRowCount() > 0)
				{
					int index = Math.min(_extendedTable.getRowCount() - 1, row);
					_extendedTable.setRowSelectionInterval(index, index);
				}
				else
				{
					_extendedTable.clearSelection();
				}
			}
		}
	}

	private class MoveAction extends AbstractAction
	{
		private final int	_difference;

		public MoveAction(int difference, Icon icon)
		{
			super(null, icon);
			setEnabled(false);
			_difference = difference;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (_extendedTable.getSelectedRowCount() != 1)
				return;

			int index1 = _extendedTable.getSelectedRow();
			int index2 = index1 + _difference;
			if (index2 < 0 || index2 >= _extendedTable.getRowCount())
				return;

			// Move one up
			_config.exchangeRegisterExtensions(index1, index2);

			_extendedTable.getSelectionModel().setSelectionInterval(index2, index2);

			Main.getWorkspace().setProjectUnsaved();
		}
	}

	private final static TextResource		res	= Main.getTextResource("machine");

	private final MachineConfiguration		_config;

	private final RegisterEditor			_registerPanel;

	private final DefaultRegisterTableModel	_basicModel;
	private final MachineRegisterTableModel	_extendedModel;

	private final JTable					_basicTable;
	private final JTable					_extendedTable;

	private final Action					_addAction;
	private final Action					_removeAction;

	private final Action					_moveUpAction;
	private final Action					_moveDownAction;

	private MigLayout createLayout()
	{
		// register tables, up/down-button col, register panel
		String cols = "[min:200lp:250lp,fill]rel[min]unrel[min:300lp:max,fill]";

		// basic registers, extended registers (split in 2 rows for side buttons), add/remove button
		String rows = "[min:pref:pref,shrinkprio 1]rel[bottom,shrinkprio 2,sg]50px![top,shrinkprio 2,sg]rel[top,min]";

		return new MigLayout("left", cols, rows);
	}

	public RegisterEditorPanel(final Project project)
	{
		setLayout(createLayout());

		_config = project.getMachineConfiguration();

		_addAction = new AddAction(res.get("register.add.label"),
			Icons.getInstance().get(res.get("register.add.icon")));
		_removeAction = new RemoveAction(res.get("register.delete.label"),
			Icons.getInstance().get(res.get("register.delete.icon")));
		_moveUpAction = new MoveAction(-1, Icons.getInstance().get(
			res.get("register.button.move-up.icon")));
		_moveDownAction = new MoveAction(1, Icons.getInstance().get(
			res.get("register.button.move-down.icon")));

		_basicModel = new DefaultRegisterTableModel();
		_extendedModel = new MachineRegisterTableModel();

		_basicTable = new RegisterExtensionsTable(_basicModel)
		{
			@Override
			public Dimension getPreferredScrollableViewportSize()
			{
				return getPreferredSize();
			}
		};
		_extendedTable = new RegisterExtensionsTable(_extendedModel);

		for (RegisterExtension register : _config.getRegisterExtensions())
			_extendedModel.addRegister(register);
		for (RegisterExtension register : _config.getBaseRegisters())
			_basicModel.addRegister(register);

		add(UIUtil.wrapInTitledGroupScroller(_basicTable,
			res.get("register.basic.title")), "cell 0 0");
		add(UIUtil.wrapInTitledGroupScroller(_extendedTable,
			res.get("register.extended.title")), "cell 0 1 1 2, top, grow");

		_registerPanel = new RegisterEditor(project.getMachineConfiguration());
		_registerPanel.setBorder(UIUtil.createGroupBorder(res.get("register.properties.title")));
		add(_registerPanel, "cell 2 0 1 4, top");

		add(new JButton(_addAction), "cell 0 3");
		add(new JButton(_removeAction), "cell 0 3");
		add(new JButton(_moveUpAction), "cell 1 1");
		add(new JButton(_moveDownAction), "cell 1 2");

		_basicTable.getSelectionModel().addListSelectionListener(
			new RegisterTableSelectionListener2(_basicModel,
				_basicTable.getSelectionModel(), _extendedTable.getSelectionModel()));
		_extendedTable.getSelectionModel().addListSelectionListener(
			new RegisterTableSelectionListener2(_extendedModel,
				_extendedTable.getSelectionModel(), _basicTable.getSelectionModel()));

		_extendedTable.getSelectionModel().addListSelectionListener(
			new ListSelectionListener()
			{
				@Override
				public void valueChanged(ListSelectionEvent e)
				{
					if (!e.getValueIsAdjusting())
					{
						boolean enabled = _extendedTable.getSelectedRow() != -1;
						if (enabled != _removeAction.isEnabled())
							_removeAction.setEnabled(enabled);
					}
				}
			});

		InputMap im = _extendedTable.getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap actionMap = _extendedTable.getActionMap();
		KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
		im.put(ks, "delete");
		actionMap.put("delete", _removeAction);

		_config.addMachineConfigListener(_extendedModel);
	}

	private class RegisterTableSelectionListener2 extends TwoSidedListSelectionListener
	{
		private AbstractRegisterTableModel	_model;

		public RegisterTableSelectionListener2(AbstractRegisterTableModel model,
				ListSelectionModel mySelectionModel,
				ListSelectionModel otherSelectionModel)
		{
			super(mySelectionModel, otherSelectionModel);
			_model = model;
		}

		@Override
		protected void onSelection(int minIndex, int maxIndex)
		{
			boolean isFirst = minIndex == 0;
			boolean isLast = maxIndex == _model.getRowCount() - 1;

			RegisterExtension register = _model.get(minIndex);
			updateSelection(register, isFirst, isLast);
		}

		@Override
		protected void onEmptySelection()
		{
			updateSelection(null, false, false);
		}
	}

	protected void updateSelection(RegisterExtension register, boolean isFirst,
			boolean isLast)
	{
		_registerPanel.setRegister(register);

		_moveUpAction.setEnabled(!isFirst && register != null && register.isExtended());
		_moveDownAction.setEnabled(!isLast && register != null && register.isExtended());
	}

	public void setSelectedExtendedRegister(int index)
	{
		_extendedTable.setRowSelectionInterval(index, index);
	}

	private RegisterExtension createNewRegister()
	{
		String newName;
		int number = 0;

		NameSearch:
		while (true)
		{
			number++;

			newName = res.format("register.new-name", "(" + number + ")");

			// Check if this is a new name
			for (RegisterExtension register : _config.getRegisterExtensions())
				if (register.getName().equalsIgnoreCase(newName))
					continue NameSearch;

			// new name
			break NameSearch;
		}

		return new RegisterExtension(newName, RegisterSize.BITS_32, "", true);
	}

	@Override
	public void dispose()
	{
		_config.removeMachineConfigListener(_extendedModel);
	}
}