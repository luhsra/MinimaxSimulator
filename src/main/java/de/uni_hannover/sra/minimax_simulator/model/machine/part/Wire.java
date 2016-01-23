package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import com.google.common.collect.ImmutableSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Component;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Point;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.parts.WireSprite;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.render.Sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A {@code Wire} is the connection between {@link Part}s.
 *
 * @author Martin L&uuml;ck
 */
public class Wire implements Circuit, SpriteOwner {

    private OutgoingPin source;
    private IngoingPin drain;

    private int value;

    private final Point[] points;

    /**
     * Constructs a new {@code Wire} with the specified amount of points.
     *
     * @param points
     *          the amount of points
     */
    protected Wire(int points) {
        this.points = new Point[points];
        for (int i = 0; i < points; i++) {
            this.points[i] = new Point(0, 0);
        }
        value = 0;
    }

    /**
     * Constructs a new {@code Wire} with the specified amount of segments, source and drain.
     *
     * @param segments
     *          the amount of segments
     * @param source
     *          the source of the {@code Wire}
     * @param drain
     *          the drain of the {@code Wire}
     */
    public Wire(int segments, OutgoingPin source, IngoingPin drain) {
        this(segments);
        attachSource(source);
        attachDrain(drain);
    }

    /**
     * Gets the source of the {@code Wire}.
     *
     * @return
     *          the source
     */
    public Part getSource() {
        return source == null ? null : source.getPart();
    }

    /**
     * Attaches the specified {@link OutgoingPin} as source.
     *
     * @param pin
     *          the new source
     */
    public void attachSource(OutgoingPin pin) {
        if (pin == null) {
            throw new IllegalArgumentException("Cannot attach the wire to null");
        }

        if (source != null) {
            source.getWires().remove(this);
        }
        source = pin;
        pin.getWires().add(this);
    }

    /**
     * Gets the drain of the {@code Wire}.
     *
     * @return
     *          the drain
     */
    public Part getDrain() {
        return drain == null ? null : drain.getPart();
    }

    /**
     * Attaches the specified {@link IngoingPin} as drain.
     *
     * @param pin
     *          the new drain
     */
    public void attachDrain(IngoingPin pin) {
        if (pin == null) {
            throw new IllegalArgumentException("Cannot attach the wire to null");
        }
        if (drain != null) {
            drain.setWire(null);
        }
        drain = pin;
        pin.setWire(this);
    }

    @Override
    public Set<Circuit> getSuccessors() {
        return ImmutableSet.of((Circuit) drain.getPart());
    }

    /**
     * Gets the value of the {@code Wire}.
     *
     * @return
     *          the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of the {@code Wire}.
     *
     * @param value
     *          the new value
     */
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public void update() {
        value = source.getValue();
        drain.setValue(value);
    }

    /**
     * Gets the {@link Point}s of the {@code Wire}.
     *
     * @return
     *          an array of the {@code Point}s
     */
    public Point[] getPoints() {
        return points;
    }

    /**
     * Creates a new {@link WirePointComponent} with the specified index.
     *
     * @param index
     *          the index of the {@code WirePointComponent}
     * @return
     *          the {@code Component} created
     */
    public Component createWireComponent(int index) {
        return new WirePointComponent(this, index);
    }

    /**
     * Creates all {@link WirePointComponent}s for the {@code Wire}.
     *
     * @return
     *          a list of the {@code Component}s created
     */
    public List<Component> createWireComponents() {
        List<Component> components = new ArrayList<>(points.length);
        for (int i = 0; i < points.length; i++) {
            components.add(createWireComponent(i));
        }
        return components;
    }

    @Override
    public String toString() {
        return "W[" + String.valueOf(getSource()) + " >(" + value + ")> " + String.valueOf(getDrain()) + "]";
    }

    @Override
    public Sprite createSprite() {
        return new WireSprite(this);
    }

    @Override
    public void reset() {
        value = 0;
    }
}
