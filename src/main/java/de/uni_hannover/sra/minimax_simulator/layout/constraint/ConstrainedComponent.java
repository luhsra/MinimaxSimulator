package de.uni_hannover.sra.minimax_simulator.layout.constraint;

import de.uni_hannover.sra.minimax_simulator.layout.Component;

class ConstrainedComponent extends AbstractAttributeOwner
{
	private final Component								_component;

	public ConstrainedComponent(String name, Component component)
	{
		super(name);
		_component = component;
	}

	@Override
	public int getPreferredWidth()
	{
		return _component.getDimension().w;
	}

	@Override
	public int getPreferredHeight()
	{
		return _component.getDimension().h;
	}
}