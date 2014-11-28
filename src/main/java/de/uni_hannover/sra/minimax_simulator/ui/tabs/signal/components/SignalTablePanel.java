package de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;

import com.google.common.base.Objects;

import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;
import de.uni_hannover.sra.minimax_simulator.resources.Icons;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.common.Disposable;
import de.uni_hannover.sra.minimax_simulator.ui.common.DoubleClickListener;
import de.uni_hannover.sra.minimax_simulator.ui.common.EditableCellFocusAction;
import de.uni_hannover.sra.minimax_simulator.ui.common.TableColumnAdjuster;
import de.uni_hannover.sra.minimax_simulator.ui.common.components.JFastScrollPane;
import de.uni_hannover.sra.minimax_simulator.ui.common.renderer.MultiLineTableCellRenderer;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.BreakpointColumn;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.Column;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.ConditionColumn;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.DescriptionColumn;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.EditableColumn;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.FocusableColumn;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.JumpTargetColumn;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.LabelColumn;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.SignalColumn;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.SignalTableColumnModel;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.SignalTableModel;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.ToggleColumn;
import de.uni_hannover.sra.minimax_simulator.util.EnumerationIterator;

public class SignalTablePanel extends JPanel implements Disposable
{
	private class AddAction extends AbstractAction
	{
		AddAction(Icon icon)
		{
			super(null, icon);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			int selectedIndex = _table.getSelectionModel().getMaxSelectionIndex();
			if (selectedIndex == -1)
			{
				// Append a line at the end.
				selectedIndex = _table.getRowCount();
			}
			else
			{
				// Append a line after the currently selected line.
				selectedIndex++;
			}
			_signalTable.addSignalRow(selectedIndex, new SignalRow());
			_table.getSelectionModel().setSelectionInterval(selectedIndex, selectedIndex);
			Application.getWorkspace().setProjectUnsaved();
		}
	}

	private class RemoveAction extends AbstractAction
	{
		RemoveAction(Icon icon)
		{
			super(null, icon);
			setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			// Remove all currently selected lines
			int[] selections = _table.getSelectedRows();
			for (int i = 0; i < selections.length; i++)
			{
				_signalTable.removeSignalRow(selections[i]);
				for (int j = i + 1; j < selections.length; j++)
				{
					selections[j]--;
				}
			}

			Application.getWorkspace().setProjectUnsaved();
		}
	}

	private class MoveUpAction extends AbstractAction
	{
		MoveUpAction(Icon icon)
		{
			super(null, icon);
			setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			int min = _table.getSelectionModel().getMinSelectionIndex();
			int max = _table.getSelectionModel().getMaxSelectionIndex();

			_signalTable.moveSignalRows(min, max, -1);

			_table.getSelectionModel().setSelectionInterval(min - 1, max - 1);
			Application.getWorkspace().setProjectUnsaved();
		}
	}

	private class MoveDownAction extends AbstractAction
	{
		MoveDownAction(Icon icon)
		{
			super(null, icon);
			setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			int min = _table.getSelectionModel().getMinSelectionIndex();
			int max = _table.getSelectionModel().getMaxSelectionIndex();

			_signalTable.moveSignalRows(min, max, 1);

			_table.getSelectionModel().setSelectionInterval(min + 1, max + 1);
			Application.getWorkspace().setProjectUnsaved();
		}
	}

//	private class KeyStrokeAction extends AbstractAction
//	{
//		private final int	_value;
//
//		KeyStrokeAction(int value)
//		{
//			_value = value;
//		}
//
//		@Override
//		public void actionPerformed(ActionEvent e)
//		{
//			// System.out.println(_value);
//		}
//	}

	private final Action				_addAction;
	private final Action				_removeAction;
	private final Action				_moveUpAction;
	private final Action				_moveDownAction;

	private final SignalTable			_signalTable;
	private final SignalConfiguration	_signalConfig;

	private final SignalTableModel		_model;
	private final JTable				_table;

	private final TextResource			_res;

	public SignalTablePanel(SignalTable signalTable, SignalConfiguration config)
	{
		_res = Application.getTextResource("machine");

		_signalTable = signalTable;
		_signalConfig = config;

		_model = new SignalTableModel(_signalTable, _signalConfig)
		{
			@Override
			public void setValueAt(Object aValue, int rowIndex, int columnIndex)
			{
				Object oldValue = getValueAt(rowIndex, columnIndex);
				if (Objects.equal(oldValue, aValue))
					return;

				super.setValueAt(aValue, rowIndex, columnIndex);
				// changed values could change the description column width
				revalidate();
				Application.getWorkspace().setProjectUnsaved();
			}
		};

		Icons icons = Icons.getInstance();

		_addAction = new AddAction(icons.get(_res.get("signal.add.icon")));
		_removeAction = new RemoveAction(icons.get(_res.get("signal.remove.icon")));
		_moveUpAction = new MoveUpAction(icons.get(_res.get("signal.move-up.icon")));
		_moveDownAction = new MoveDownAction(icons.get(_res.get("signal.move-down.icon")));

		_signalTable.addSignalTableListener(_model);
		_signalConfig.addSignalConfigListener(_model);

		_table = createTable(_model);

		JScrollPane scroller = new JFastScrollPane(_table);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		setBorder(new EmptyBorder(3, 3, 3, 3));
		setLayout(new MigLayout("center, filly", "[min:pref:pref, fill]unrel[min!]",
			"[fill]"));
		add(scroller);
		add(buildButtonRow(), BorderLayout.EAST);
	}

	private static void configTableColumns(JTable table)
	{
		for (TableColumn col : EnumerationIterator.iterate(table.getColumnModel().getColumns()))
		{
			col.setPreferredWidth(col.getMinWidth());
		}

		int iconColWidth = 30;
		for (int i = 0; i < 1; i++)
		{
			TableColumn col = table.getColumnModel().getColumn(i);
			col.setMaxWidth(iconColWidth);
			col.setMinWidth(iconColWidth);
			col.setPreferredWidth(iconColWidth);
			col.setResizable(false);
		}
	}

	private JTable createTable(SignalTableModel model)
	{
		final JTable table = new JTable(model, new SignalTableColumnModel())
		{
			@Override
			public boolean getScrollableTracksViewportWidth()
			{
				return getParent().getWidth() > getPreferredSize().width;
			}

			@Override
			public Dimension getPreferredScrollableViewportSize()
			{
				return new Dimension(getPreferredSize().width, getParent().getHeight());
			}

//			private void selectNextCellAfter(int row, int col)
//			{
//				col++;
//				if (col >= getColumnCount())
//				{
//					col = 0;
//					row++;
//					if (row >= getRowCount())
//					{
//						clearSelection();
//					}
//					else
//					{
//						setRowSelectionInterval(row, row);
//						setColumnSelectionInterval(col, col);
//					}
//				}
//			}

			@Override
			public void removeEditor()
			{
				super.removeEditor();
				
			}
		};
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setShowGrid(true);
		table.setGridColor(new Color(231, 236, 241));
		table.setSurrendersFocusOnKeystroke(true);

		table.createDefaultColumnsFromModel();

		class CellFocusAction extends EditableCellFocusAction
		{
			public CellFocusAction(JTable table, KeyStroke keyStroke)
			{
				super(table, keyStroke);
			}

			@Override
			protected boolean shouldFocus(int row, int column)
			{
				return _model.getColumn(column) instanceof FocusableColumn;
			}
		}

		new CellFocusAction(table, KeyStroke.getKeyStroke("TAB"));
		new CellFocusAction(table, KeyStroke.getKeyStroke("shift TAB"));
		new CellFocusAction(table, KeyStroke.getKeyStroke("RIGHT"));
		new CellFocusAction(table, KeyStroke.getKeyStroke("LEFT"));
		new CellFocusAction(table, KeyStroke.getKeyStroke("UP"));
		new CellFocusAction(table, KeyStroke.getKeyStroke("DOWN"));

		JTableHeader header = table.getTableHeader();
		header.setReorderingAllowed(false);

		configTableColumns(table);

		// Signal editor
		table.setDefaultEditor(SignalColumn.class, new SignalEditor(model));
		// Signal renderer
		table.setDefaultRenderer(SignalColumn.class, new SignalTableRenderer(model));

		// Label editor
		JTextField textEdit = new JTextField();
		textEdit.setBorder(BorderFactory.createEmptyBorder());
		table.setDefaultEditor(LabelColumn.class, new DefaultCellEditor(textEdit));

		// Breakpoints
		Icon icon = Icons.getInstance().get(_res.get("signal.breakpoint.icon"));
		table.setDefaultRenderer(BreakpointColumn.class, new FlagIconCellRenderer(icon));

		// row height is now variable
		// FontMetrics metrics = table.getFontMetrics(table.getFont());
		// int fontHeight = metrics.getHeight();
		// table.setRowHeight(fontHeight + 4);

		TableCellRenderer varHeightRenderer = new MultiLineTableCellRenderer(
			new DefaultTableCellRenderer());
		table.setDefaultRenderer(DescriptionColumn.class, varHeightRenderer);
		table.setDefaultRenderer(ConditionColumn.class, varHeightRenderer);
		table.setDefaultRenderer(JumpTargetColumn.class, varHeightRenderer);

		table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				int firstRow = table.getSelectionModel().getMinSelectionIndex();
				int lastRow = table.getSelectionModel().getMaxSelectionIndex();
				_removeAction.setEnabled(firstRow != -1);
				_moveUpAction.setEnabled(firstRow > 0);
				_moveDownAction.setEnabled(lastRow >= 0
					&& lastRow < table.getRowCount() - 1);
			}
		});
		table.addMouseListener(new DoubleClickListener()
		{
			@Override
			public void doubleClicked(MouseEvent e)
			{
				int rowIndex = table.rowAtPoint(e.getPoint());
				int colIndex = table.columnAtPoint(e.getPoint());
				if (isValidCell(rowIndex, colIndex))
				{
					interactWithCell(rowIndex, colIndex);
				}
			}
		});

		final TableColumnAdjuster tca = new TableColumnAdjuster(table, 6);
		tca.setColumnDataIncluded(true);
		tca.setColumnHeaderIncluded(true);
		// We do this manually (see below) after the table structure has changed.
		// otherwise the Adjuster and the JTable (which auto-creates its columns
		// without an explicit column model) enter a race condition when the table
		// structure changes -- the adjuster refreshes the column widths and the
		// JTable then destroys all columns.
//		tca.setDynamicAdjustment(true);
		tca.setDynamicAdjustment(false);
		tca.setOnlyAdjustLarger(false);
		tca.adjustColumns();

		model.addTableModelListener(new TableModelListener()
		{
			@Override
			public void tableChanged(TableModelEvent e)
			{
				if (e.getFirstRow() == TableModelEvent.HEADER_ROW)
				{
					// table structure changed
					table.createDefaultColumnsFromModel();
					configTableColumns(table);
				}
				tca.adjustColumns();
			}
		});

		InputMap im = table.getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap am = table.getActionMap();

		KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		im.put(enterKey, "startEdit");
		KeyStroke spaceKey = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0);
		im.put(spaceKey, "startEdit");
		am.put("startEdit", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				if (table.getSelectedRowCount() == 1
					&& table.getSelectedColumnCount() == 1)
				{
					int rowIndex = table.getSelectedRow();
					int colIndex = table.getSelectedColumn();
					if (isValidCell(rowIndex, colIndex) && !table.isEditing())
					{
						interactWithCell(rowIndex, colIndex);
						//table.setRowSelectionInterval(rowIndex, rowIndex);
						//table.setColumnSelectionInterval(colIndex, colIndex);
					}
				}
			}
		});
		// Delete typed
		KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
		im.put(ks, "delete");
		am.put("delete", _removeAction);
//		// 0 typed
//		ks = KeyStroke.getKeyStroke(KeyEvent.VK_0, 0);
//		im.put(ks, "signal0");
//		am.put("signal0", new KeyStrokeAction(0));
//		// 1 typed
//		ks = KeyStroke.getKeyStroke(KeyEvent.VK_1, 0);
//		im.put(ks, "signal1");
//		am.put("signal1", new KeyStrokeAction(1));

		return table;
	}

	private JPanel buildButtonRow()
	{
		JPanel buttons = new JPanel();
		buttons.setLayout(new MigLayout("", "[min!]",
			"[grow]unrel[]unrel[]30lp[]rel[]unrel[grow]"));

		buttons.add(new JButton(_addAction), "cell 0 1");
		buttons.add(new JButton(_removeAction), "cell 0 2");
		buttons.add(new JButton(_moveUpAction), "cell 0 3");
		buttons.add(new JButton(_moveDownAction), "cell 0 4");

		return buttons;
	}

	private boolean isValidCell(int rowIndex, int colIndex)
	{
		return rowIndex >= 0 && rowIndex < _table.getRowCount() && colIndex >= 0
			&& colIndex < _table.getColumnCount();
	}

	private void interactWithCell(int rowIndex, int colIndex)
	{
		Column col = _model.getColumn(colIndex);
		if (col instanceof ToggleColumn)
		{
			_model.toggleCell(rowIndex, colIndex);
			Application.getWorkspace().setProjectUnsaved();
		}
		else if (col instanceof JumpTargetColumn || col instanceof ConditionColumn)
		{
			SignalRow row = _signalTable.getRow(rowIndex);
			JumpEditorDialog editor = new JumpEditorDialog(_signalTable, row);
			editor.setVisible(true);
			// wait until editor dialog is hidden
			editor.dispose();
			Jump jump = editor.getJump();

			// action canceled?
			if (jump == null)
				return;

			if (!jump.equals(row.getJump()))
			{
				_signalTable.setRowJump(rowIndex, jump);
				Application.getWorkspace().setProjectUnsaved();
			}
		}
		else if (col instanceof EditableColumn)
		{
			if (_table.editCellAt(rowIndex, colIndex))
			{
				Component editor = _table.getEditorComponent();
				if (editor != null)
				{
					if (editor instanceof JComboBox
						&& !((JComboBox) editor).isPopupVisible())
						((JComboBox) editor).setPopupVisible(true);
				}
			}
		}
	}

	@Override
	public void dispose()
	{
		_signalTable.removeSignalTableListener(_model);
		_signalConfig.removeSignalConfigListener(_model);
	}
}