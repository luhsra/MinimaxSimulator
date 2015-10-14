package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import java.util.Set;

public interface LayoutRule
{
	public Set<Layout> getLayouts();

	public String getTarget(Layout layout);
}