package de.uni_hannover.sra.minimax_simulator.ui.tabs.project.debugger.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.Simulation;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.SimulationListener;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.SimulationState;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.resources.Icons;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.common.Disposable;
import de.uni_hannover.sra.minimax_simulator.ui.common.DoubleClickListener;
import de.uni_hannover.sra.minimax_simulator.ui.common.TableColumnAdjuster;
import de.uni_hannover.sra.minimax_simulator.ui.common.components.JFastScrollPane;
import de.uni_hannover.sra.minimax_simulator.ui.common.renderer.MultiLineTableCellRenderer;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.project.debugger.model.ProgramTableModel;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.components.FlagIconCellRenderer;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.BreakpointColumn;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.ConditionColumn;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.DescriptionColumn;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.JumpTargetColumn;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.RowIndicatorColumn;
import de.uni_hannover.sra.minimax_simulator.util.EnumerationIterator;

public class ProgramPanel extends JPanel implements Disposable, SimulationListener
{
	private final Simulation		_simulation;
	private final SignalTable		_signalTable;

	private final ProgramTableModel	_model;
	private final JTable			_table;
	private final TextResource		_res;

	public ProgramPanel(Simulation simulation, SignalTable signalTable)
	{
		_res = Application.getTextResource("machine");

		_simulation = simulation;
		_signalTable = signalTable;
		_model = new ProgramTableModel(_signalTable, simulation);

		_simulation.addSimulationListener(this);
		_signalTable.addSignalTableListener(_model);

		_table = createTable(_model);

		JScrollPane scroller = new JFastScrollPane(_table);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		setBorder(new EmptyBorder(3, 3, 3, 3));
		setLayout(new BorderLayout());
		add(scroller);
	}

	private static void configTableColumns(JTable table)
	{
		for (TableColumn col : EnumerationIterator.iterate(table.getColumnModel().getColumns()))
		{
			col.setPreferredWidth(col.getMinWidth());
		}

		int iconColWidth = 30;
		for (int i = 0; i < 2; i++)
		{
			TableColumn col = table.getColumnModel().getColumn(i);
			col.setMaxWidth(iconColWidth);
			col.setMinWidth(iconColWidth);
			col.setPreferredWidth(iconColWidth);
			col.setResizable(false);
		}
	}

	private JTable createTable(ProgramTableModel model)
	{
		final JTable table = new JTable(model, new DefaultTableColumnModel())
		{
			@Override
			public boolean getScrollableTracksViewportWidth()
			{
				return getParent().getWidth() > getPreferredSize().width;
			}

			@Override
			public Dimension getPreferredScrollableViewportSize()
			{
				return getPreferredSize();
			}
		};

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setShowGrid(true);
		table.setGridColor(new Color(231, 236, 241));
		table.setFillsViewportHeight(true);

		table.createDefaultColumnsFromModel();

		configTableColumns(table);

		TableCellRenderer varHeightRenderer = new MultiLineTableCellRenderer(
			new DefaultTableCellRenderer());
		table.setDefaultRenderer(DescriptionColumn.class, varHeightRenderer);
		table.setDefaultRenderer(ConditionColumn.class, varHeightRenderer);
		table.setDefaultRenderer(JumpTargetColumn.class, varHeightRenderer);

		// hide table header
		//table.getTableHeader().setPreferredSize(new Dimension(1, 0));
		table.getTableHeader().setReorderingAllowed(false);

		// Breakpoints
		{
			Icon icon = Icons.getInstance().get(_res.get("signal.breakpoint.icon"));
			table.setDefaultRenderer(BreakpointColumn.class, new FlagIconCellRenderer(
				icon));
		}
		// Row indicator
		{
			Icon icon = Icons.getInstance().get(_res.get("signal.row-indicator.icon"));
			table.setDefaultRenderer(RowIndicatorColumn.class, new FlagIconCellRenderer(
				icon));
		}

		FontMetrics metrics = table.getFontMetrics(table.getFont());
		int fontHeight = metrics.getHeight();
		table.setRowHeight(fontHeight + 4);
		table.addMouseListener(new DoubleClickListener()
		{
			@Override
			public void doubleClicked(MouseEvent e)
			{
				int row = table.rowAtPoint(e.getPoint());
				int col = table.columnAtPoint(e.getPoint());
				if (isValidCell(row, col))
				{
					if (_model.toggleCell(row, col))
						Application.getWorkspace().setProjectUnsaved();
				}
			}
		});

		final TableColumnAdjuster tca = new TableColumnAdjuster(table, 6);
		tca.setColumnDataIncluded(true);
		tca.setColumnHeaderIncluded(true);
		tca.setDynamicAdjustment(false);
		tca.setOnlyAdjustLarger(false);
		tca.adjustColumns();

		return table;
	}

	private boolean isValidCell(int row, int col)
	{
		return row >= 0 && row < _table.getRowCount() && col >= 0
			&& col < _table.getColumnCount();
	}

	@Override
	public void dispose()
	{
		_simulation.removeSimulationListener(this);
		_signalTable.removeSignalTableListener(_model);
	}

	@Override
	public void stateChanged(SimulationState state)
	{
		if (state == SimulationState.IDLE)
		{
			int currentRow = _simulation.getCurrentSignalRow();
			_table.scrollRectToVisible(_table.getCellRect(currentRow, 0, true));
		}
	}
}