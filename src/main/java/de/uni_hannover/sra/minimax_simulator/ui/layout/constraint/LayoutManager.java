package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import de.uni_hannover.sra.minimax_simulator.ui.layout.Component;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Container;

/**
 * A {@code LayoutManager} allows to constrain components with {@link Layout} instances.<br>
 * <br>
 * It extends the {@link ConstraintsManager} in such a way that allows it
 * to layout the components of a container instance by instance by assigning
 * {@link Layout} instances to components.<br>
 * <br>
 * As the {@link ConstraintsManager}, the {@code LayoutManager} is usually coupled
 * to a {@link Container} instance which must contain the components that are constrained.
 * 
 * @author Martin L&uuml;ck
 */
public interface LayoutManager extends ConstraintsManager {

    /**
     * Adds a {@link Layout} to the component with the specified name.<br>
     * <br>
     * Internally, the instance tries to add each {@link Constraint} imposed by the {@code Layout}
     * to the container. In this process, any {@code Constraint} that was manually added for the component is removed.
     * <br><br>
     * The component may also be a virtual one, which means that no {@link Component}
     * instance corresponds to it.<br>
     * <br>
     * If the {@code Layout} is successfully set, the {@code Constraint}s it provides
     * are added to the container.
     *
     * @param name
     *          the name of the component that is in the underlying container
     * @param layout
     *          the {@code Layout} instance to manage the component
     * @throws IllegalArgumentException
     *          thrown if the component is not in the underlying container
     * @throws NullPointerException
     *          thrown if any argument is {@code null}
     * @return
     *          the {@code Layout} instance that was previously set for the specified component
     */
    public Layout putLayout(String name, Layout layout);

    /**
     * Removes the {@link Layout} that is assigned to the component with the specified name.
     * All {@link Constraint}s that are set for the component are removed from the container.
     *
     * @param name
     *          the name of the component that is in the underlying container
     * @throws NullPointerException
     *          thrown if {@code name} is {@code null}
     * @throws IllegalArgumentException
     *          thrown if the component is not in the underlying container
     * @return
     *          the {@code Layout} instance that was previously set for the specified component
     */
    public Layout removeLayout(String name);

    /**
     * Notifies the instance that a {@link Layout} is assigned to the specified component
     * and that the {@link Constraint}s of that {@code Layout} have changed.
     * <br><br>
     * This method will clear all {@code Constraint}s of the component and re-add those
     * that are provided by the assigned {@code Layout} instance.
     *
     *
     * @param name
     *          the name of the component with a changed {@code Layout}
     * @throws NullPointerException
     *          thrown if {@code name} is {@code null}
     * @throws IllegalArgumentException
     *          thrown if the component is not in the underlying container
     * @throws IllegalStateException
     *          if there is no {@code Layout} assigned to the specified component
     */
    public void updateLayout(String name);
}