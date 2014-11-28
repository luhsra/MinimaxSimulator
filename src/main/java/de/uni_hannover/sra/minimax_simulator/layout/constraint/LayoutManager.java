package de.uni_hannover.sra.minimax_simulator.layout.constraint;

import de.uni_hannover.sra.minimax_simulator.layout.Component;
import de.uni_hannover.sra.minimax_simulator.layout.Container;

/**
 * A LayoutManager allows to constrain components with Layout instances.
 * <br>
 * It extends the {@link ConstraintsManager} in such a way that allows it
 * to layout the components of a container instance by instance by assigning
 * {@link Layout} instances to components.
 * <br>
 * As the {@link ConstraintsManager}, the LayoutManager is usually coupled
 * to a {@link Container} instance which must contain the components that
 * are constrained.
 * 
 * @author Martin
 *
 */
public interface LayoutManager extends ConstraintsManager
{
	/**
	 * Adds a layout to the component with the given name.
	 * <br>
	 * Internally, the instance tries to add each constraint imposed by the Layout
	 * to the container. In this process, any constraint
	 * that was manually added for the component is removed.
	 * <br>
	 * The component may also be a virtual one, which means that no {@link Component}
	 * instance corresponds to it.
	 * <br>
	 * If the layout is successfully set, the constraints it provides
	 * are added to the container. 
	 * 
	 * @param name the name of the component that is in the
	 * underlying Container
	 * @param layout the {@link Layout} instance to manage the component
	 * @throws IllegalArgumentException if the component is not in the
	 * underlying container
	 * @throws NullPointerException if any parameter is null
	 * @return the Layout instance that was previosly set for
	 * the given component
	 */
	public Layout putLayout(String name, Layout layout);

	/**
	 * Removes the layout that is assigned to the component with the given name.
	 * All constraints that are set for the component are removed from the container.
	 * 
	 * @param name the name of the component that is in the underlying Container
	 * @throws NullPointerException if the parameter is null
	 * @throws IllegalArgumentException if the component is not in the
	 * underlying container
	 * @return the Layout instance that was previously set for
	 * the given component
	 */
	public Layout removeLayout(String name);

	/**
	 * Notifies this instance that a layout is assigned to the given component
	 * and that the constraints of that layout have changed.
	 * <br><br>
	 * This method will clear the component of all constraints and re-add those
	 * that are provided by the assigned Layout instance.
	 * 
	 * 
	 * @param name the name of the component with a changed layout
	 * @throws NullPointerException if the parameter is null
	 * @throws IllegalArgumentException if the component is not in the
	 * underlying container
	 * @throws IllegalStateException if there is no Layout assigned to
	 * the given component
	 */
	public void updateLayout(String name);
}