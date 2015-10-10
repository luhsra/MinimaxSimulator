package de.uni_hannover.sra.minimax_simulator.ui.tabs.project.debugger.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.MachineConfigRegisterEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListener;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.Simulation;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.SimulationListener;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.SimulationState;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.Trackable;
import de.uni_hannover.sra.minimax_simulator.model.machine.simulation.TrackableChangeListener;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.common.Disposable;

@Deprecated
public class RegisterViewModel extends AbstractTableModel implements
		MachineConfigListener, Disposable, SimulationListener
{
	private class RegisterRow implements TrackableChangeListener<Integer>
	{
		public int						row;
		public final RegisterExtension	register;
		public Trackable<Integer>		value;

		private final String			_formatString;

		public RegisterRow(int row, RegisterExtension register)
		{
			this.row = row;
			this.register = register;
			this._formatString = register.getSize().getHexFormat();
		}

		public String toDec()
		{
			if (value == null)
				return "--";
			return Integer.toString(value.get().intValue());
		}

		public String toHex()
		{
			if (value == null)
				return "--";
			return String.format(_formatString, value.get().intValue());
		}

		@Override
		public void onValueChanged(Integer value)
		{
			fireTableRowsUpdated(row, row);
		}
	}

	private SimulationState				_oldState;
	private final List<RegisterRow>		_registers;
	private final MachineConfiguration	_config;
	private final Simulation			_simulation;

	private final String[]				_colNames;

	public RegisterViewModel(MachineConfiguration config, Simulation simulation)
	{
		_config = config;
		_simulation = simulation;

		TextResource res = Main.getTextResource("debugger");
		_colNames = new String[] { res.get("register.name"), res.get("register.dec"),
				res.get("register.hex") };

		_registers = new ArrayList<RegisterRow>();

		_config.addMachineConfigListener(this);

		for (RegisterExtension register : config.getBaseRegisters())
			_registers.add(createRow(_registers.size(), register));
		for (RegisterExtension register : config.getRegisterExtensions())
			_registers.add(createRow(_registers.size(), register));

		_oldState = simulation.getState();
		if (_oldState == SimulationState.IDLE)
		{
			for (int i = 0, n = _registers.size(); i < n; i++)
			{
				setRowValueTracker(i);
			}
		}

		_simulation.addSimulationListener(this);
	}

	private RegisterRow createRow(int rowIndex, RegisterExtension registerExt)
	{
		RegisterRow row = new RegisterRow(rowIndex, registerExt);
		return row;
	}

	private void setRowValueTracker(int rowIndex)
	{
		RegisterRow row = _registers.get(rowIndex);
		row.value = _simulation.getRegisterValue(row.register.getName());
		row.value.addChangeListener(row);
	}

	private void destroyRowValueTracker(int rowIndex)
	{
		RegisterRow row = _registers.get(rowIndex);
		if (row.value != null)
		{
			row.value.removeChangeListener(row);
			row.value = null;
		}
	}

	@Override
	public int getRowCount()
	{
		return _registers.size();
	}

	@Override
	public int getColumnCount()
	{
		return 3;
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		return _colNames[columnIndex];
	}

	public RegisterExtension getRegister(int row)
	{
		return _registers.get(row).register;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		RegisterRow reg = _registers.get(rowIndex);
		switch (columnIndex)
		{
			case 0:
				return reg.register.getName();
			case 1:
				return reg.toDec();
			case 2:
				return reg.toHex();
		}
		throw new AssertionError();
	}

	public Integer getValue(int rowIndex)
	{
		RegisterRow reg = _registers.get(rowIndex);
		return reg.value.get();
	}

	public void setValue(int rowIndex, Integer value)
	{
		RegisterRow reg = _registers.get(rowIndex);
		reg.value.set(value);
	}

	@Override
	public void processEvent(MachineConfigEvent event)
	{
		if (event instanceof MachineConfigRegisterEvent)
		{
			MachineConfigRegisterEvent r = (MachineConfigRegisterEvent) event;

			int baseRegSize = _config.getBaseRegisters().size();
			int index1 = baseRegSize + r.index;
			int index2 = baseRegSize + r.index2;

			switch (r.type)
			{
				case ELEMENT_ADDED:
					_registers.add(index1, createRow(index1, r.element));
					fireTableRowsInserted(index1, index1);
					break;

				case ELEMENT_REMOVED:
					destroyRowValueTracker(index1);
					_registers.remove(index1);
					fireTableRowsDeleted(index1, index1);
					break;

				case ELEMENT_REPLACED:
					destroyRowValueTracker(index1);
					_registers.set(index1, createRow(index1, r.element));
					fireTableRowsUpdated(index1, index1);
					break;

				case ELEMENTS_EXCHANGED:
					Collections.swap(_registers, index1, index2);
					_registers.get(index1).row = index2;
					_registers.get(index2).row = index1;
					fireTableRowsUpdated(index1, index1);
					fireTableRowsUpdated(index2, index2);
					break;
			}
		}
	}

	@Override
	public void dispose()
	{
		for (int i = 0, n = _registers.size(); i < n; i++)
		{
			destroyRowValueTracker(i);
		}
		_registers.clear();

		_config.removeMachineConfigListener(this);
		_simulation.removeSimulationListener(this);
	}

	@Override
	public void stateChanged(SimulationState state)
	{
		switch (state)
		{
			case OFF:
				if (_oldState == SimulationState.IDLE)
				{
					for (int i = 0, n = _registers.size(); i < n; i++)
					{
						destroyRowValueTracker(i);
					}
				}
				break;

			case IDLE:
				if (_oldState == SimulationState.OFF)
				{
					for (int i = 0, n = _registers.size(); i < n; i++)
					{
						setRowValueTracker(i);
					}
				}
				break;

			default:
				break;
		}
		_oldState = state;
		fireTableRowsUpdated(0, getRowCount() - 1);
	}
}