package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.register.models;

import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.MachineConfigRegisterEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListener;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;

public class MachineRegisterTableModel extends DefaultRegisterTableModel
		implements MachineConfigListener
{
	public MachineRegisterTableModel()
	{
	}

	public MachineRegisterTableModel(MachineConfiguration config)
	{
		for (RegisterExtension register : config.getRegisterExtensions())
			addRegister(register);
	}

	@Override
	public void processEvent(MachineConfigEvent event)
	{
		if (event instanceof MachineConfigRegisterEvent)
		{
			MachineConfigRegisterEvent r = (MachineConfigRegisterEvent) event;
			switch (r.type)
			{
				case ELEMENT_ADDED:
					addRegister(r.element);
					break;

				case ELEMENT_REMOVED:
					removeRegister(r.element);
					break;

				case ELEMENT_REPLACED:
					setRegister(r.index, r.element);
					break;

				case ELEMENTS_EXCHANGED:
					setRegister(r.index2, r.element);
					setRegister(r.index, r.element2);
					break;
			}
		}
	}
}
