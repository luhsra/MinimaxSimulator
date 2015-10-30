package de.uni_hannover.sra.minimax_simulator.ui.layout;

/**
 * A {@code Container} is a {@link Component} that consists of several {@code Component}s.
 *
 * @author Martin L&uuml;ck
 */
public abstract class Container extends AbstractComponent {

	/**
	 * Adds the specified {@link Component} to the container.
	 *
	 * @param component
	 * 			the component to add
	 */
	public void addComponent(Component component) {
		addComponent(component, null);
	}

	/**
	 * Adds the specified {@link Component} with the specified constraint to the container.
	 *
	 * @param component
	 * 			the component to add
	 * @param constraint
	 * 			the constraint
	 */
	public abstract void addComponent(Component component, Object constraint);

	/**
	 * Removes the specified {@link Component} from the container.
	 *
	 * @param component
	 * 			the component to remove
	 */
	public abstract void removeComponent(Component component);
}