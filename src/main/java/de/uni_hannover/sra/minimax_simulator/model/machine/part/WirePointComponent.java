package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.ui.layout.Point;
import de.uni_hannover.sra.minimax_simulator.ui.layout.PointComponent;

/**
 * A {@code WirePointComponent} is a part of a {@link Wire}.<br>
 * It is used for deflection of a {@code Wire}.
 *
 * @author Martin L&uuml;ck
 */
public class WirePointComponent extends PointComponent {

    private final Wire wire;
    private final int index;

    /**
     * Constructs a new {@code WirePointComponent} for the specified {@link Wire} at the specified index.
     *
     * @param wire
     *          the {@code Wire} the {@code WirePointComponent} belongs to
     * @param index
     *          the index of the {@code WirePointComponent}
     */
    public WirePointComponent(Wire wire, int index) {
        this.wire = wire;
        this.index = index;
    }

    @Override
    public void doLayout() {
        wire.getPoints()[index] = new Point(getBounds().x, getBounds().y);
    }

    @Override
    public String toString() {
        return wire.toString() + "." + index;
    }
}