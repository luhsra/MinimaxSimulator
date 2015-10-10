package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.alu.components;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import de.uni_hannover.sra.minimax_simulator.Main;
import net.miginfocom.swing.MigLayout;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.MachineConfigAluEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListener;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import de.uni_hannover.sra.minimax_simulator.resources.Icons;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;
import de.uni_hannover.sra.minimax_simulator.ui.common.Disposable;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.alu.models.SortedEditorAluTableModel;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.common.models.AbstractAluTableModel;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.common.models.AluTableModel2;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.common.models.TwoSidedListSelectionListener;

@Deprecated
public class AluEditorPanel extends JPanel implements MachineConfigListener, Disposable
{
	private final static String ALU_RESULT = "ALU.result \u2190 ";

	private class AddAction extends AbstractAction
	{
		public AddAction(String title)
		{
			super(title);
			setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			int[] indices = _availableTable.getSelectedRows();
			if (indices.length == 0)
				return;

			AluOperation[] operations = new AluOperation[indices.length];
			for (int i = 0; i < indices.length; i++)
				operations[i] = _availableModel.get(indices[i]);

			int[] newSelection = new int[indices.length];

			for (int i = 0; i < operations.length; i++)
			{
				AluOperation operation = operations[i];
				_config.addAluOperation(operation);

				newSelection[i] = _addedModel.getRowCount() - 1;
			}

			_addedTable.getSelectionModel().clearSelection();
			for (int selectedIndex : newSelection)
				_addedTable.getSelectionModel().addSelectionInterval(selectedIndex,
					selectedIndex);

			Main.getWorkspace().setProjectUnsaved();
		}
	}

	private class RemoveAction extends AbstractAction
	{
		public RemoveAction(String title)
		{
			super(title);
			setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			int[] indices = _addedTable.getSelectedRows();
			if (indices.length == 0)
				return;

			AluOperation[] operations = new AluOperation[indices.length];
			for (int i = 0; i < indices.length; i++)
				operations[i] = _addedModel.get(indices[i]);

			for (int i = 0; i < operations.length; i++)
			{
				AluOperation operation = operations[i];
				_config.removeAluOperation(operation);

				// Alu operation is now in available table again
			}

			_availableTable.getSelectionModel().clearSelection();
			for (AluOperation operation : operations)
			{
				int idx = _availableModel.indexOf(operation);
				_availableTable.getSelectionModel().addSelectionInterval(idx, idx);
			}

			Main.getWorkspace().setProjectUnsaved();
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
			if (_addedTable.getSelectedRowCount() != 1)
				return;

			int index1 = _addedTable.getSelectedRow();
			int index2 = index1 + _difference;
			if (index2 < 0 || index2 >= _addedTable.getRowCount())
				return;

			// Move operations in model and adapt selection
			_config.exchangeAluOperations(index1, index2);
			_addedTable.getSelectionModel().setSelectionInterval(index2, index2);

			Main.getWorkspace().setProjectUnsaved();
		}
	}

	private class ExclusiveAluSelectionListener2 extends TwoSidedListSelectionListener
	{
		private final AbstractAluTableModel	_model;

		public ExclusiveAluSelectionListener2(AbstractAluTableModel model,
				ListSelectionModel mySelectionModel,
				ListSelectionModel otherSelectionModel)
		{
			super(mySelectionModel, otherSelectionModel);
			_model = model;
		}

		@Override
		protected void onSelection(int minIndex, int maxIndex)
		{
			AluOperation operation = _model.get(minIndex);

			boolean isAdded = _model == _addedModel;
			boolean isFirst = minIndex == 0;
			boolean isLast = maxIndex == _model.getRowCount() - 1;
			boolean isMultiple = maxIndex != minIndex;

			updateSelectedOperation(operation, isAdded, isFirst, isLast, isMultiple);
		}

		@Override
		protected void onEmptySelection()
		{
			updateSelectedOperation(null, _model == _addedModel, false, false, false);
		}
	}

	private final TextResource			_res;
	private final TextResource			_resAlu;

	private final MachineConfiguration	_config;

	private final AbstractAluTableModel	_addedModel;
	private final AbstractAluTableModel	_availableModel;

	private final JTable				_availableTable;
	private final JTable				_addedTable;

	private final JTextField			_rtNotation;

	private final Action				_addAction;
	private final Action				_removeAction;
	private final Action				_moveUpAction;
	private final Action				_moveDownAction;

	private final JTextArea				_description;

	private MigLayout createLayout()
	{
		// String table1Col = "[220lp:320lp:450lp, fill]";
		String table2Col = "[150lp:250lp:400lp, fill]";
		String moveButtonsCol = "[min]";
		String middleCol = "[min:400lp:400lp, fill]";

		String cols = table2Col + "rel" + moveButtonsCol + "unrel" + middleCol + "unrel"
			+ table2Col;
		return new MigLayout("left", cols,
			"[fill,top]push[min,bottom]50px![min,top]push[fill]");
	}

	public AluEditorPanel(final Project project)
	{
		setLayout(createLayout());
		_res = Main.getTextResource("machine");
		_resAlu = Main.getTextResource("alu");
		_config = project.getMachineConfiguration();

		_addAction = new AddAction(_res.get("alu.button.add"));
		_removeAction = new RemoveAction(_res.get("alu.button.remove"));
		_moveUpAction = new MoveAction(-1, Icons.getInstance().get(
			_res.get("alu.button.move-up.icon")));
		_moveDownAction = new MoveAction(1, Icons.getInstance().get(
			_res.get("alu.button.move-down.icon")));

		// Create models and components
		_addedModel = new AluTableModel2(_resAlu);
		_availableModel = new SortedEditorAluTableModel(_resAlu);

		for (AluOperation op : _config.getAluOperations())
			_addedModel.addOperation(op);
		for (AluOperation op : AluOperation.values())
			if (!_config.getAluOperations().contains(op))
				_availableModel.addOperation(op);

		_rtNotation = new JTextField();
		_rtNotation.setEditable(false);

		_rtNotation.setFont(new Font("Monospaced", Font.PLAIN, 14));
		_description = new JTextArea();
		_description.setEditable(false);
		_description.setFont(UIManager.getFont("Label.font"));
		_description.setWrapStyleWord(true);
		_description.setLineWrap(true);

		_addedTable = new AluOperationTable(_addedModel);
		_availableTable = new AluOperationTable(_availableModel);

		// Add components
		add(UIUtil.wrapInTitledGroupScroller(_addedTable, _res.get("alu.added.title")),
			"cell 0 0 1 4");
		add(UIUtil.wrapInTitledGroupScroller(_availableTable,
			_res.get("alu.available.title")), "cell 3 0 1 4");
		add(buildAluDescriptionPanel(), "cell 2 0 1 4");

		add(new JButton(_moveUpAction), "cell 1 1");
		add(new JButton(_moveDownAction), "cell 1 2");

		_addedTable.addMouseListener(new AluOperationDoubleClickListener(_addedTable,
			_addedModel)
		{
			@Override
			public void aluDblClicked(AluOperation operation, int index)
			{
				_config.removeAluOperation(operation);

				int newIndex = _availableModel.indexOf(operation);
				_availableTable.getSelectionModel().setSelectionInterval(newIndex,
					newIndex);

				Main.getWorkspace().setProjectUnsaved();
			}
		});
		_availableTable.addMouseListener(new AluOperationDoubleClickListener(
			_availableTable, _availableModel)
		{
			@Override
			public void aluDblClicked(AluOperation operation, int index)
			{
				_config.addAluOperation(operation);

				int newIndex = _addedModel.getRowCount() - 1;
				_addedTable.getSelectionModel().setSelectionInterval(newIndex, newIndex);

				Main.getWorkspace().setProjectUnsaved();
			}
		});

		InputMap im = _addedTable.getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap actionMap = _addedTable.getActionMap();
		KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
		im.put(ks, "delete");
		actionMap.put("delete", _removeAction);

		_addedTable.getSelectionModel().addListSelectionListener(
			new ExclusiveAluSelectionListener2(_addedModel,
				_addedTable.getSelectionModel(), _availableTable.getSelectionModel()));
		_availableTable.getSelectionModel().addListSelectionListener(
			new ExclusiveAluSelectionListener2(_availableModel,
				_availableTable.getSelectionModel(), _addedTable.getSelectionModel()));

		_config.addMachineConfigListener(this);
	}

	private JPanel buildAluDescriptionPanel()
	{
		JPanel aluDescription = new JPanel();
		aluDescription.setLayout(new MigLayout("fillx, flowy", "[fill]",
			"[min]unrel[100lp:50%:300lp,grow,fill]unrel[min]"));
		aluDescription.setBorder(UIUtil.createGroupBorder(_res.get("alu.selected.title")));

		aluDescription.add(UIUtil.wrapInBorderPanel(_rtNotation, new EmptyBorder(1, 1,
			1, 1), _res.get("alu.rt.title")));
		aluDescription.add(UIUtil.wrapInBorderPanel(buildDescriptionScroller(),
			new EmptyBorder(1, 1, 1, 1), _res.get("alu.description.title")));
		aluDescription.add(buildButtonRow());
		return aluDescription;
	}

	private JScrollPane buildDescriptionScroller()
	{
		JScrollPane descriptionScroller = new JScrollPane(_description);
		return descriptionScroller;
	}

	private JPanel buildButtonRow()
	{
		JPanel row = new JPanel();
		row.setLayout(new BorderLayout());
		row.add(new JButton(_addAction), BorderLayout.EAST);
		row.add(new JPanel(), BorderLayout.CENTER);
		row.add(new JButton(_removeAction), BorderLayout.WEST);

		return row;
	}

	protected void updateSelectedOperation(AluOperation operation, boolean isAdded,
			boolean isFirst, boolean isLast, boolean isMultiple)
	{
		if (operation == null)
		{
			_addAction.setEnabled(false);
			_removeAction.setEnabled(false);
			_moveUpAction.setEnabled(false);
			_moveDownAction.setEnabled(false);
		}
		else
		{
			if (isAdded)
			{
				_addAction.setEnabled(false);
				_removeAction.setEnabled(true);
				_moveUpAction.setEnabled(!isFirst && !isMultiple);
				_moveDownAction.setEnabled(!isLast && !isMultiple);
			}
			else
			{
				_addAction.setEnabled(true);
				_removeAction.setEnabled(false);
				_moveUpAction.setEnabled(false);
				_moveDownAction.setEnabled(false);
			}
		}

		if (operation == null)
		{
			_description.setText("");
			_rtNotation.setText(" ");
		}
		else
		{
			_description.setText(operation.getDescription(_resAlu));
			_rtNotation.setText(ALU_RESULT + operation.getRtNotation(_resAlu));
		}
	}

	@Override
	public void processEvent(MachineConfigEvent event)
	{
		if (event instanceof MachineConfigAluEvent)
		{
			MachineConfigAluEvent a = (MachineConfigAluEvent) event;
			switch (a.type)
			{
				case ELEMENT_ADDED:
					_addedModel.addOperation(a.element);
					_availableModel.removeOperation(a.element);
					break;

				case ELEMENT_REMOVED:
					_addedModel.removeOperation(a.element);
					_availableModel.addOperation(a.element);
					break;

				case ELEMENTS_EXCHANGED:
					_addedModel.setOperation(a.index, a.element2);
					_addedModel.setOperation(a.index2, a.element);
					break;

				default:
					// ignore
					break;
			}
		}
	}

	public void setSelectedAddedOperation(int index)
	{
		_addedTable.setRowSelectionInterval(index, index);
	}

	@Override
	public void dispose()
	{
		_config.removeMachineConfigListener(this);
	}
}