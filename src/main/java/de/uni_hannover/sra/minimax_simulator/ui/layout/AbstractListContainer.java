package de.uni_hannover.sra.minimax_simulator.ui.layout;

import java.util.ArrayList;

public abstract class AbstractListContainer extends Container
{
	protected final ArrayList<Component> _children = new ArrayList<Component>();

	@Override
	public void doLayout()
	{
		layoutChildren();

		for (Component child : _children)
			child.doLayout();
	}

	protected abstract void layoutChildren();

	@Override
	public void addComponent(Component component, Object constraint)
	{
		_children.add(component);
	}

	@Override
	public void removeComponent(Component component)
	{
		_children.remove(component);
	}

	public Component getComponent(int index)
	{
		return _children.get(index);
	}

	public void setComponent(int index, Component child)
	{
		_children.set(index, child);
	}

	@Override
	public void updateSize()
	{
		for (Component child : _children)
			child.updateSize();

		updateMySize();
	}

	protected abstract void updateMySize();
}
