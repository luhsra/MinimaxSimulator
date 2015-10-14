package de.uni_hannover.sra.minimax_simulator.ui.layout;


public abstract class Container extends AbstractComponent
{
	public void addComponent(Component component)
	{
		addComponent(component, null);
	}

	public abstract void addComponent(Component component, Object constraint);

	public abstract void removeComponent(Component component);
}