package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.ConstraintBuilder;
import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.Layout;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Default implementation of {@link LayoutSet}.
 *
 * @author Martin L&uuml;ck
 */
public class DefaultLayoutSet implements LayoutSet {

    private final Map<String, Layout> layouts = new HashMap<>();

    /**
     * Adds the specified {@link Layout} and links it with the specified name.
     *
     * @param name
     *          the name of the component the {@code Layout} belongs to
     * @param layout
     *          the {@code Layout} to add
     */
    public void addLayout(String name, Layout layout) {

        if (layouts.containsKey(name)) {
            throw new IllegalStateException();
        }

        layouts.put(name, layout);
    }

    /**
     * Adds a {@link Layout} and links it with the specified name.<br>
     * The {@code Layout} is created using {@link DefaultLayout#DefaultLayout(Map)}
     * where the map is {@link ConstraintBuilder#constraints()}.
     *
     * @param name
     *          the name of the component the {@code Layout} belongs to
     * @param cb
     *          the {@code ConstraintBuilder} for the {@code DefaultLayout}
     */
    public void addLayout(String name, ConstraintBuilder cb) {
        if (layouts.containsKey(name)) {
            throw new IllegalStateException("Duplicate layout: " + name);
        }

        layouts.put(name, new DefaultLayout(cb.constraints()));
        cb.clear();
    }

    @Override
    public Set<String> getComponents() {
        return layouts.keySet();
    }

    @Override
    public Layout getLayout(String component) {
        return layouts.get(component);
    }
}