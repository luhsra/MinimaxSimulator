package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.layout.constraint.Layout;

import java.util.Set;

public interface LayoutSet
{
	public Set<String> getComponents();

	public Layout getLayout(String component);
}