package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.mux.components;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;

import net.miginfocom.swing.MigLayout;
import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.MachineConfigMuxEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListener;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.ConstantMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.resources.Icons;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.common.Disposable;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.common.models.TwoSidedListSelectionListener;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.mux.model.MuxTableModel;

// TODO: moved mux inputs sometimes are marked as unsaved
public class MuxEditorPanel extends JPanel implements Disposable, MachineConfigListener
{
	private class MuxControlGroup
	{
		class AddAction extends AbstractAction
		{
			public AddAction(String title, Icon icon)
			{
				super(title, icon);
			}

			@Override
			public void actionPerformed(ActionEvent e)
			{
				int index = model.getRowCount();
				_config.addMuxSource(model.getMux(), createDefaultMuxSource());
				table.getSelectionModel().setSelectionInterval(index, index);

				Application.getWorkspace().setProjectUnsaved();
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
				int choice = JOptionPane.showConfirmDialog(Application.getMainWindow(),
					res.format("mux.delete-dialog.message", model.getMux().toString()),
					res.get("mux.delete-dialog.title"), JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);

				if (choice != JOptionPane.OK_OPTION)
					return;

				int[] selections = table.getSelectedRows();
				if (selections.length == 0)
					return;

				Arrays.sort(selections);

				for (int i = 0; i < selections.length; i++)
				{
					_config.removeMuxSource(model.getMux(), selections[i]);

					// Because subsequent elements moved on position up during
					// the removal of an element
					for (int j = i + 1; j < selections.length; j++)
						selections[j]--;
				}

				if (table.getRowCount() > 0)
				{
					int index = Math.min(table.getRowCount() - 1, selections[0]);
					table.setRowSelectionInterval(index, index);
				}
				else
				{
					table.clearSelection();
				}

				Application.getWorkspace().setProjectUnsaved();
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
				if (table.getSelectedRowCount() != 1)
					return;

				int index1 = table.getSelectedRow();
				int index2 = index1 + _difference;
				if (index2 < 0 || index2 >= table.getRowCount())
					return;

				// Move one up
				_config.exchangeMuxSources(model.getMux(), index1, index2);

				table.getSelectionModel().setSelectionInterval(index2, index2);

				Application.getWorkspace().setProjectUnsaved();
			}
		}

		private class MuxListSelectionListener extends TwoSidedListSelectionListener
		{
			public MuxListSelectionListener()
			{
				super(table.getSelectionModel(), otherTable.getSelectionModel());
			}

			@Override
			protected void onSelection(int minIndex, int maxIndex)
			{
				boolean isFirst = minIndex == 0;
				boolean isLast = maxIndex == model.getRowCount() - 1;

				_editor.setSource(model.getMux(), model.getMuxSource(minIndex), minIndex);

				removeAction.setEnabled(true);
				moveUpAction.setEnabled(!isFirst);
				moveDownAction.setEnabled(!isLast);
			}

			@Override
			protected void onEmptySelection()
			{
				removeAction.setEnabled(false);
				moveUpAction.setEnabled(false);
				moveDownAction.setEnabled(false);
				_editor.setSource(null, null, -1);
			}
		}

		public final Action			addAction;
		public final Action			removeAction;
		public final Action			moveUpAction;
		public final Action			moveDownAction;

		public final MuxTable		table;
		public final MuxTableModel	model;

		public final MuxTable		otherTable;

		public MuxControlGroup(TextResource res, MuxTable table, MuxTableModel model,
				MuxTable otherTable)
		{
			this.table = table;
			this.model = model;
			this.otherTable = otherTable;

			addAction = new AddAction(res.get("mux.add.label"), Icons.getInstance().get(
				res.get("mux.add.icon")));
			removeAction = new RemoveAction(res.get("mux.remove.label"),
				Icons.getInstance().get(res.get("mux.remove.icon")));

			moveUpAction = new MoveAction(-1, Icons.getInstance().get(
				res.get("mux.button.move-up.icon")));
			moveDownAction = new MoveAction(1, Icons.getInstance().get(
				res.get("mux.button.move-down.icon")));

			KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
			table.getInputMap(JComponent.WHEN_FOCUSED).put(ks, "delete");
			table.getActionMap().put("delete", removeAction);

			table.getSelectionModel().addListSelectionListener(
				new MuxListSelectionListener());
		}
	}

	private final MachineConfiguration	_config;

	private final MuxTableModel			_modelA;
	private final MuxTableModel			_modelB;

	private final MuxInputEditor		_editor;

	private final MuxControlGroup		_groupA;
	private final MuxControlGroup		_groupB;

	private final TextResource			res	= Application.getTextResource("machine");

	private LayoutManager createLayout()
	{
		String muxCol = "[220lp:270lp:max,fill]";
		String listCol = "[200lp:pref:max,fill]";

		String row = "[min:pref:n,top,sg]";

		String cols = muxCol + "rel[min]unrel" + listCol + "unrel[min]rel" + muxCol;
		String rows = row + "50px" + row + "rel[top]";
		return new MigLayout("left", cols, rows);
	}

	public MuxEditorPanel(MachineConfiguration config)
	{
		_config = config;

		_modelA = new MuxTableModel(MuxType.A, config);
		_modelB = new MuxTableModel(MuxType.B, config);

		MuxTable tableA = new MuxTable(_modelA);
		MuxTable tableB = new MuxTable(_modelB);

		_groupA = new MuxControlGroup(res, tableA, _modelA, tableB);
		_groupB = new MuxControlGroup(res, tableB, _modelB, tableA);

		_editor = new MuxInputEditor(_config);

		setLayout(createLayout());

		add(createMuxTablePanel(tableA, MuxType.A), "cell 0 0 1 2, grow");
		add(createMuxTablePanel(tableB, MuxType.B), "cell 4 0 1 2, grow");

		add(new JButton(_groupA.addAction), "cell 0 2");
		add(new JButton(_groupA.removeAction), "cell 0 2");

		add(new JButton(_groupB.addAction), "cell 4 2");
		add(new JButton(_groupB.removeAction), "cell 4 2");

		add(UIUtil.wrapInTitledGroupPanel(_editor, res.get("mux.source-editor.title")),
			"cell 2 0 1 2, growy 0");

		add(new JButton(_groupA.moveUpAction), "cell 1 0, bottom");
		add(new JButton(_groupA.moveDownAction), "cell 1 1");

		add(new JButton(_groupB.moveUpAction), "cell 3 0, bottom");
		add(new JButton(_groupB.moveDownAction), "cell 3 1");

		_config.addMachineConfigListener(_modelA);
		_config.addMachineConfigListener(_modelB);
		_config.addMachineConfigListener(this);
	}

	private MuxInput createDefaultMuxSource()
	{
		return new ConstantMuxInput(0);
	}

	private JPanel createMuxTablePanel(JTable table, MuxType mux)
	{
		return UIUtil.wrapInTitledGroupScroller(table,
			res.format("mux.table.title", mux.toString()));
	}

	@Override
	public void dispose()
	{
		_config.removeMachineConfigListener(_modelA);
		_config.removeMachineConfigListener(_modelB);
		_config.removeMachineConfigListener(this);
		_editor.dispose();
	}

	@Override
	public void processEvent(MachineConfigEvent event)
	{
		if (event instanceof MachineConfigMuxEvent)
		{
			MachineConfigMuxEvent m = (MachineConfigMuxEvent) event;
			if (m.mux == null)
				return;

			switch (m.type)
			{
				case ELEMENT_REPLACED:
					if (m.element2.equals(_editor.getSource()))
						_editor.setSource(m.mux, m.element, m.index);
					break;

				default:
					break;
			}
		}
	}
}