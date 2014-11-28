package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.mux.model;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListener;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.MachineConfigMuxEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;

public class MuxSourceComboModel extends AbstractListModel implements ComboBoxModel,
		MachineConfigListener
{
	private ArrayList<MuxInput>	_inputs;
	private MuxInput			_selectedInput;

	public MuxSourceComboModel(MachineConfiguration config)
	{
		_inputs = new ArrayList<MuxInput>(config.getAvailableSources());
	}

	@Override
	public void processEvent(MachineConfigEvent event)
	{
		if (event instanceof MachineConfigMuxEvent)
		{
			MachineConfigMuxEvent m = (MachineConfigMuxEvent) event;

			// Not interested in selected inputs for a specific multiplexer
			if (m.mux != null)
				return;

			switch (m.type)
			{
				case ELEMENT_ADDED:
					_inputs.add(m.element);
					fireIntervalAdded(this, m.index, m.index);
					break;
				case ELEMENT_REMOVED:
					_inputs.remove(m.index);
					if (m.element.equals(_selectedInput))
						setSelectedItem(null);

					fireIntervalRemoved(this, m.index, m.index);
					break;
				case ELEMENT_REPLACED:
					if (m.element2.equals(_selectedInput))
						setSelectedItem(m.element);

					_inputs.set(m.index, m.element);
					fireContentsChanged(this, m.index, m.index);
					break;

				case ELEMENTS_EXCHANGED:
					Collections.swap(_inputs, m.index, m.index2);
					fireContentsChanged(this, m.index, m.index);
					fireContentsChanged(this, m.index2, m.index2);
					break;
			}
		}
	}

	@Override
	public int getSize()
	{
		return _inputs.size();
	}

	@Override
	public Object getElementAt(int index)
	{
		return _inputs.get(index);
	}

	@Override
	public void setSelectedItem(Object anItem)
	{
		_selectedInput = (MuxInput) anItem;
		fireContentsChanged(this, -1, -1);
	}

	@Override
	public Object getSelectedItem()
	{
		return _selectedInput;
	}
}