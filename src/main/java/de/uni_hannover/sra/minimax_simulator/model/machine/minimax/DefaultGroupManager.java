package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.Group;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link GroupManager}.
 *
 * @author Martin L&uuml;ck
 */
class DefaultGroupManager implements GroupManager {

    private final MinimaxLayout layout;
    private final MinimaxTopology topology;
    private final MinimaxDisplay display;

    private final Map<String, Group> groups = new HashMap<>();
    private final Map<String, LayoutSet> layouts = new HashMap<>();

    /**
     * Constructs a new {@code DefaultGroupManager} with the specified {@link MinimaxLayout},
     * {@link MinimaxTopology} and {@link MinimaxDisplay}.
     *
     * @param layout
     *          the layout of the {@link MinimaxMachine}
     * @param topology
     *          the topology of the {@code MinimaxMachine}
     * @param display
     *          the display of the {@code MinimaxMachine}
     */
    DefaultGroupManager(MinimaxLayout layout, MinimaxTopology topology, MinimaxDisplay display) {
        this.layout = layout;
        this.topology = topology;
        this.display = display;
    }

    @Override
    public void removeGroup(String id) {
        Group group = groups.remove(id);
        if (group == null) {
            throw new IllegalArgumentException("Unknown group: " + id);
        }

        LayoutSet set = layouts.remove(id);
        if (set != null) {
            layout.removeLayouts(set);
        }

        display.removeGroup(group);
        layout.removeGroup(group);
        topology.removeGroup(group);
    }

    @Override
    public void initializeGroup(String id, Group group) {
        if (groups.containsKey(id)) {
            throw new IllegalArgumentException("Group already registered: " + id);
        }

        groups.put(id, group);

        group.initialize(topology, display);
        topology.addGroup(group);
        layout.addGroup(group);
        display.addGroup(group);

        if (group.hasLayouts()) {
            LayoutSet set = group.createLayouts();
            layouts.put(id, set);
            layout.putLayouts(group.createLayouts());
        }
    }
}