package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import de.uni_hannover.sra.minimax_simulator.ui.layout.Component;

/**
 * A {@code ConstrainedComponent} is a {@link Component} with {@link Constraint}s.
 *
 * @author Martin L&uuml;ck
 */
class ConstrainedComponent extends AbstractAttributeOwner {

    private final Component component;

    /**
     * Constructs a new {@code ConstrainedComponent} with the specified name and {@link Component}.
     *
     * @param name
     *          the name of the {@code ConstrainedComponent}
     * @param component
     *          the {@code Component} to constrain
     */
    public ConstrainedComponent(String name, Component component) {
        super(name);
        this.component = component;
    }

    @Override
    public int getPreferredWidth() {
        return component.getDimension().w;
    }

    @Override
    public int getPreferredHeight() {
        return component.getDimension().h;
    }
}