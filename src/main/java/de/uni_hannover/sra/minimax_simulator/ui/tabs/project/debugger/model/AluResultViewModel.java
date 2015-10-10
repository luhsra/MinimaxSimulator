package de.uni_hannover.sra.minimax_simulator.ui.tabs.project.debugger.model;

import javax.swing.table.AbstractTableModel;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.Simulation;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.SimulationListener;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.SimulationState;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.Trackable;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.TrackableChangeListener;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.common.Disposable;

@Deprecated
public class AluResultViewModel extends AbstractTableModel implements
		TrackableChangeListener<Integer>, Disposable, SimulationListener
{
	private final Simulation	_simulation;
	private Trackable<Integer>	_aluResult;

	private final String[]		_colNames;

	public AluResultViewModel(Simulation simulation)
	{
		_simulation = simulation;

		if (simulation.getState() != SimulationState.OFF)
			_aluResult = simulation.getAluResult();

		TextResource res = Main.getTextResource("debugger");
		_colNames = new String[] { res.get("alu.dec"), res.get("alu.hex") };

		_simulation.addSimulationListener(this);
	}

	@Override
	public int getRowCount()
	{
		return 1;
	}

	@Override
	public int getColumnCount()
	{
		return 2;
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		return _colNames[columnIndex];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		if (_aluResult == null)
			return "--";

		Integer val = _aluResult.get();
		switch (columnIndex)
		{
			case 0:
				return val.intValue();
			case 1:
				return String.format("0x%08X", val);
		}
		throw new AssertionError();
	}

	@Override
	public void onValueChanged(Integer value)
	{
		fireTableRowsUpdated(0, 0);
	}

	@Override
	public void dispose()
	{
		if (_aluResult != null)
		{
			_aluResult.removeChangeListener(this);
			_aluResult = null;
		}
		_simulation.removeSimulationListener(this);
	}

	@Override
	public void stateChanged(SimulationState state)
	{
		switch (state)
		{
			case OFF:
				if (_aluResult != null)
				{
					_aluResult.removeChangeListener(this);
					_aluResult = null;
					fireTableRowsUpdated(0, 0);
				}
				break;
			case IDLE:
				if (_aluResult == null)
				{
					_aluResult = _simulation.getAluResult();
					_aluResult.addChangeListener(this);
					fireTableRowsUpdated(0, 0);
				}
				break;

			default:
				break;
		}
	}
}