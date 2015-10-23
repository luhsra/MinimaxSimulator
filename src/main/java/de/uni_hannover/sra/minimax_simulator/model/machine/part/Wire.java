package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import com.google.common.collect.ImmutableSet;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Component;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Point;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;
import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.parts.WireSprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A {@code Wire} is the connection between {@link Part}s.
 *
 * @author Martin L&uuml;ck
 */
public class Wire implements Circuit, SpriteOwner {

	private OutgoingPin		_source;
	private IngoingPin		_drain;

	private int				_value;

	private final Point[]	_points;

	/**
	 * Constructs a new {@code Wire} with the specified amount of points.
	 *
	 * @param points
	 *          the amount of points
	 */
	protected Wire(int points) {
		_points = new Point[points];
		for (int i = 0; i < points; i++) {
			_points[i] = new Point(0, 0);
		}
		_value = 0;
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
		return _source == null ? null : _source.getPart();
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

		if (_source != null) {
			_source.getWires().remove(this);
		}
		_source = pin;
		pin.getWires().add(this);
	}

	/**
	 * Gets the drain of the {@code Wire}.
	 *
	 * @return
	 *          the drain
	 */
	public Part getDrain() {
		return _drain == null ? null : _drain.getPart();
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
		if (_drain != null) {
			_drain.setWire(null);
		}
		_drain = pin;
		pin.setWire(this);
	}

	@Override
	public Set<Circuit> getSuccessors() {
		return ImmutableSet.of((Circuit) _drain.getPart());
	}

	/**
	 * Gets the value of the {@code Wire}.
	 *
	 * @return
	 *          the value
	 */
	public int getValue() {
		return _value;
	}

	/**
	 * Sets the value of the {@code Wire}.
	 *
	 * @param value
	 *          the new value
	 */
	public void setValue(int value) {
		_value = value;
	}

	@Override
	public void update() {
		_value = _source.getValue();
		_drain.setValue(_value);
	}

	/**
	 * Gets the {@link Point}s of the {@code Wire}.
	 *
	 * @return
	 *          an array of the {@code Point}s
	 */
	public Point[] getPoints() {
		return _points;
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
		List<Component> components = new ArrayList<Component>(_points.length);
		for (int i = 0; i < _points.length; i++) {
			components.add(createWireComponent(i));
		}
		return components;
	}

	@Override
	public String toString() {
		return "W[" + String.valueOf(getSource()) + " >(" + _value + ")> " + String.valueOf(getDrain()) + "]";
	}

	@Override
	public Sprite createSprite() {
		return new WireSprite(this);
	}

	@Override
	public void reset() {
		_value = 0;
	}
}
