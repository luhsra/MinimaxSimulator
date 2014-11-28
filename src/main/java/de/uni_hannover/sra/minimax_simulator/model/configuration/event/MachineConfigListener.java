package de.uni_hannover.sra.minimax_simulator.model.configuration.event;

public interface MachineConfigListener
{
	public void processEvent(MachineConfigEvent event);
}