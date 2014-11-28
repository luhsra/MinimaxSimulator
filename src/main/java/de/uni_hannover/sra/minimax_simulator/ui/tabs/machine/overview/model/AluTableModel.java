package de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.overview.model;

import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListener;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.MachineConfigAluEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.common.models.AluTableModel2;

public class AluTableModel extends AluTableModel2 implements MachineConfigListener
{
	public AluTableModel(MachineConfiguration config, TextResource aluDescriptionResource)
	{
		super(aluDescriptionResource);
		_aluOperations.addAll(config.getAluOperations());
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
					addOperation(a.element);
					break;
				case ELEMENT_REMOVED:
					removeOperation(a.index);
					break;
				case ELEMENTS_EXCHANGED:
					setOperation(a.index, a.element2);
					setOperation(a.index2, a.element);
					break;
				default:
					break;
			}
		}
	}
}