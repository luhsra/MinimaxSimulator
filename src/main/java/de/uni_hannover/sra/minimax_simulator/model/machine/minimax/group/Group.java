package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Component;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;

import java.util.Set;

/**
 * A {@code Group} combines parts of a {@link Circuit} belonging to one component part, e.g. the ALU.
 *
 * @author Martin L&uuml;ck
 */
public interface Group {

    /**
     * Initializes the group.
     *
     * @param cr
     *          the {@link MachineTopology} containing the parts of the {@code Group}
     * @param fontProvider
     *          the {@link FontMetricsProvider} used for rendering text
     */
    public void initialize(MachineTopology cr, FontMetricsProvider fontProvider);

    /**
     * Gets the name of the specified object.
     *
     * @param object
     *          the object
     * @return
     *          the name of the object
     */
    public String getName(Object object);

    /**
     * Gets the {@link Circuit}s of the {@code Group}.
     *
     * @return
     *          a set of the {@code Group}'s {@code Circuit}s
     */
    public Set<Circuit> getGroupCircuits();

    /**
     * Get the {@link SpriteOwner}s related to the {@code Group}.
     *
     * @return
     *          a set of the {@code SpriteOwner}s of the {@code Group}
     */
    public Set<SpriteOwner> getSpriteOwners();

    /**
     * Gets the {@link Component}s of the {@code Group}.
     *
     * @return
     *          a set of the {@code Component}s of the {@code Group}
     */
    public Set<Component> getComponents();

    /**
     * Gets the virtual components of the {@code Group}.
     *
     * @return
     *          a set with the name of the virtual {@code Component}s
     */
    public Set<String> getVirtualComponents();

    /**
     * Gets the value of the {@code has layout} property.
     *
     * @return
     *          {@code true} if the {@code Group} has a layout, {@code false} otherwise
     */
    public boolean hasLayouts();

    /**
     * Creates the layout of the {@code Group}.
     *
     * @return
     *          the layout
     */
    public LayoutSet createLayouts();
}