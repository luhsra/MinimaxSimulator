package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.Layout;

import java.util.Set;

/**
 * A {@code LayoutSet} is a set of layouts.
 *
 * @author Martin L&uuml;ck
 */
public interface LayoutSet {

    /**
     * Gets the components of the {@code LayoutSet}.
     *
     * @return
     *          a set with the names of the components
     */
    public Set<String> getComponents();

    /**
     * Gets the {@link Layout} of the component with the specified name.
     *
     * @param component
     *          the name of a component
     * @return
     *          the {@code Layout} of the component
     */
    public Layout getLayout(String component);
}