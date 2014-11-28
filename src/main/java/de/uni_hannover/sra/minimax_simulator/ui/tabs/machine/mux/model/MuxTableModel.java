package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.mux.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.MachineConfigMuxEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListener;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.ConstantMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.util.Util;

public class MuxTableModel extends AbstractTableModel implements MachineConfigListener
{
	private final List<MuxInput>	_sources	= new ArrayList<MuxInput>();

	private final MuxType			_mux;

	public MuxTableModel(MuxType mux, MachineConfiguration config)
	{
		_mux = mux;
		_sources.addAll(config.getMuxSources(mux));
	}

	public MuxType getMux()
	{
		return _mux;
	}

	@Override
	public int getRowCount()
	{
		return _sources.size();
	}

	@Override
	public int getColumnCount()
	{
		return 3;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		MuxInput source = _sources.get(rowIndex);
		switch (columnIndex)
		{
			case 0:
				return Util.toBinaryAddress(rowIndex, _sources.size() - 1);
			case 1:
				return source.getName();
			case 2:
				return getExtendedSourceInfo(source);
		}
		return null;
	}

	private String getExtendedSourceInfo(MuxInput source)
	{
		if (source instanceof ConstantMuxInput)
		{
			int value = ((ConstantMuxInput) source).getConstant();
			return String.format("0x%08X", value);
		}
		return "";
	}

	public void addMuxSource(MuxInput source)
	{
		int size = _sources.size();
		_sources.add(source);
		fireTableRowsInserted(size, size);

		// binary addresses of other entries may have changed
		fireTableRowsUpdated(0, size - 1);
	}

	public void addMuxSource(int index, MuxInput source)
	{
		_sources.add(index, source);
		fireTableRowsInserted(index, index);

		// binary addresses of other entries may have changed
		fireTableRowsUpdated(0, _sources.size() - 1);
	}

	public void removeMuxSource(int index)
	{
		_sources.remove(index);
		fireTableRowsDeleted(index, index);

		// binary addresses of other entries may have changed
		fireTableRowsUpdated(0, _sources.size() - 1);
	}

	public void removeMuxSource(MuxInput source)
	{
		int index = _sources.indexOf(source);
		if (index == -1)
			return;

		_sources.remove(index);
		fireTableRowsDeleted(index, index);

		// binary addresses of other entries may have changed
		fireTableRowsUpdated(0, _sources.size() - 1);
	}

	public void setMuxSource(int index, MuxInput source)
	{
		_sources.set(index, source);
		fireTableRowsUpdated(index, index);
	}

	public MuxInput getMuxSource(int index)
	{
		return _sources.get(index);
	}

	@Override
	public void processEvent(MachineConfigEvent event)
	{
		if (event instanceof MachineConfigMuxEvent)
		{
			MachineConfigMuxEvent m = (MachineConfigMuxEvent) event;
			if (m.mux != _mux)
				return;

			switch (m.type)
			{
				case ELEMENT_ADDED:
					addMuxSource(m.index, m.element);
					break;
				case ELEMENT_REMOVED:
					removeMuxSource(m.index);
					break;
				case ELEMENT_REPLACED:
					setMuxSource(m.index, m.element);
					break;
				case ELEMENTS_EXCHANGED:
					setMuxSource(m.index, m.element2);
					setMuxSource(m.index2, m.element);
				default:
					break;
			}
		}
	}
}