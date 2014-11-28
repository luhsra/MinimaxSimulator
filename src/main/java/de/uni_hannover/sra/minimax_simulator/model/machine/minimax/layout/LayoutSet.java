package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import java.util.Set;

import de.uni_hannover.sra.minimax_simulator.layout.constraint.Layout;

public interface LayoutSet
{
	public Set<String> getComponents();

	public Layout getLayout(String component);
}