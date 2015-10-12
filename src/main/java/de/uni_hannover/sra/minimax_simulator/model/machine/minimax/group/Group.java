package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group;

import de.uni_hannover.sra.minimax_simulator.layout.Component;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;

import java.util.Set;

public interface Group
{
	public void initialize(MachineTopology cr, FontMetricsProvider fontProvider);

	public String getName(Object object);

	public Set<Circuit> getGroupCircuits();

	public Set<SpriteOwner> getSpriteOwners();

	public Set<Component> getComponents();

	public Set<String> getVirtualComponents();

	public boolean hasLayouts();

	public LayoutSet createLayouts();
}