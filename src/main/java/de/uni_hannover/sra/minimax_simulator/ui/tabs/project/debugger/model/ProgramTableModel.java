package de.uni_hannover.sra.minimax_simulator.ui.tabs.project.debugger.model;

import java.util.ArrayList;
import java.util.List;

import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.Simulation;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.SimulationListener;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.SimulationState;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTableListener;
import de.uni_hannover.sra.minimax_simulator.ui.common.Disposable;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.AbstractProgramTableModel;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.AddressColumn;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.BreakpointColumn;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.Column;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.ConditionColumn;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.DescriptionColumn;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.JumpTargetColumn;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.LabelColumn;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.signal.model.RowIndicatorColumn;

public class ProgramTableModel extends AbstractProgramTableModel implements
		SignalTableListener, SimulationListener, Disposable
{
	private final Simulation	_simulation;

	private final static int	NO_ROW_MARKED	= -1;
	private int					_lastExecutedRow;

	public ProgramTableModel(SignalTable signalTable, Simulation simulation)
	{
		super(Application.getTextResource("signal"), signalTable);

		_simulation = simulation;
		_simulation.addSimulationListener(this);

		// no rows yet executed
		if (simulation.getState() == SimulationState.IDLE)
			_lastExecutedRow = simulation.getCurrentSignalRow();
		else
			_lastExecutedRow = NO_ROW_MARKED;

		updateColumns();
	}


	@Override
	public void onStructureChanged()
	{
		updateColumns();
		fireTableStructureChanged();
	}

	@Override
	public void onRowAdded(int index, SignalRow row)
	{
		fireTableRowsInserted(index, index);
	}

	@Override
	public void onRowRemoved(int index)
	{
		fireTableRowsDeleted(index, index);
	}

	@Override
	public void onRowsExchanged(int index1, int index2)
	{
		fireTableRowsUpdated(index1, index1);
		fireTableRowsUpdated(index2, index2);
	}

	@Override
	public void onRowReplaced(int index, SignalRow row)
	{
		fireTableRowsUpdated(index, index);
	}

	@Override
	public void onRowsUpdated(int fromIndex, int toIndex)
	{
		fireTableRowsUpdated(fromIndex, toIndex);
	}

	@Override
	public void stateChanged(SimulationState state)
	{
		if (state == SimulationState.IDLE)
		{
			// simulation has done a step
			if (_lastExecutedRow != NO_ROW_MARKED)
				fireTableCellUpdated(_lastExecutedRow, 0);

			_lastExecutedRow = _simulation.getCurrentSignalRow();
			fireTableCellUpdated(_lastExecutedRow, 0);
		}
		else if (state == SimulationState.HALTED)
		{
			// simulation ended
			if (_lastExecutedRow != NO_ROW_MARKED)
			{
				fireTableCellUpdated(_lastExecutedRow, 0);
				_lastExecutedRow = NO_ROW_MARKED;
			}
		}
		else if (state == SimulationState.OFF)
		{
			// simulation canceled
			if (_lastExecutedRow != NO_ROW_MARKED)
			{
				fireTableCellUpdated(_lastExecutedRow, 0);
				_lastExecutedRow = NO_ROW_MARKED;
			}
		}
	}

	@Override
	public void dispose()
	{
		_simulation.removeSimulationListener(this);
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		if (columnIndex > 1)
			return super.getColumnName(columnIndex);
		return "";
	}

	@Override
	protected List<Column> createColumns()
	{
		List<Column> cols = new ArrayList<Column>();
		cols.add(new RowIndicatorColumn()
		{
			@Override
			protected int getCurrentRow()
			{
				return _simulation.getCurrentSignalRow();
			}
		});
		cols.add(new BreakpointColumn());
		cols.add(new LabelColumn());
		cols.add(new AddressColumn());
		cols.add(new ConditionColumn());
		cols.add(new JumpTargetColumn());
		cols.add(new DescriptionColumn());
		return cols;
	}
}