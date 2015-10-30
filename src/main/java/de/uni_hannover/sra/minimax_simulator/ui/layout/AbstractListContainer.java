package de.uni_hannover.sra.minimax_simulator.ui.layout;

import java.util.ArrayList;

/**
 * The {@code AbstractListContainer} is a {@link Container} that holds a list of {@link Component}s.
 *
 * @author Martin L&uuml;ck
 */
public abstract class AbstractListContainer extends Container {

	protected final ArrayList<Component> _children = new ArrayList<Component>();

	@Override
	public void doLayout() {
		layoutChildren();

		for (Component child : _children) {
			child.doLayout();
		}
	}

	/**
	 * Layouts the child {@link Component}s to the container before their own layout is done.
	 */
	protected abstract void layoutChildren();

	@Override
	public void addComponent(Component component, Object constraint) {
		_children.add(component);
	}

	@Override
	public void removeComponent(Component component) {
		_children.remove(component);
	}

	/**
	 * Gets the {@link Component} at the specified index.
	 *
	 * @param index
	 * 			the index of the {@code Component}
	 * @return
	 * 			the {@code Component} at the specified index
	 */
	public Component getComponent(int index) {
		return _children.get(index);
	}

	/**
	 * Sets the specified {@link Component} to the specified index.
	 *
	 * @param index
	 * 			the index of the new {@code Component}
	 * @param child
	 * 			the new {@code Component}
	 */
	public void setComponent(int index, Component child) {
		_children.set(index, child);
	}

	@Override
	public void updateSize() {
		for (Component child : _children) {
			child.updateSize();
		}

		updateMySize();
	}

	/**
	 * Updates the size of the {@code AbstractListContainer}.
	 */
	protected abstract void updateMySize();
}
