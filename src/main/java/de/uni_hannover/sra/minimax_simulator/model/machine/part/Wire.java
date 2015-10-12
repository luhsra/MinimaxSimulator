package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import com.google.common.collect.ImmutableSet;
import de.uni_hannover.sra.minimax_simulator.layout.Component;
import de.uni_hannover.sra.minimax_simulator.layout.Point;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;
import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.parts.WireSprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Wire implements Circuit, SpriteOwner
{
	private OutgoingPin		_source;
	private IngoingPin		_drain;

	private int				_value;

	private final Point[]	_points;

	protected Wire(int points)
	{
		_points = new Point[points];
		for (int i = 0; i < points; i++)
			_points[i] = new Point(0, 0);
		_value = 0;
	}

	public Wire(int segments, OutgoingPin source, IngoingPin drain)
	{
		this(segments);
		attachSource(source);
		attachDrain(drain);
	}

	public Part getSource()
	{
		return _source == null ? null : _source.getPart();
	}

	public void attachSource(OutgoingPin pin)
	{
		if (pin == null)
			throw new IllegalArgumentException("Cannot attach the wire to null");

		if (_source != null)
			_source.getWires().remove(this);
		_source = pin;
		pin.getWires().add(this);
	}

	public Part getDrain()
	{
		return _drain == null ? null : _drain.getPart();
	}

	public void attachDrain(IngoingPin pin)
	{
		if (pin == null)
			throw new IllegalArgumentException("Cannot attach the wire to null");
		if (_drain != null)
			_drain.setWire(null);
		_drain = pin;
		pin.setWire(this);
	}

	@Override
	public Set<Circuit> getSuccessors()
	{
		return ImmutableSet.of((Circuit) _drain.getPart());
	}

	public int getValue()
	{
		return _value;
	}

	public void setValue(int value)
	{
		_value = value;
	}

	@Override
	public void update()
	{
		_value = _source.getValue();
		_drain.setValue(_value);
	}

	public Point[] getPoints()
	{
		return _points;
	}

	public Component createWireComponent(int index)
	{
		return new WirePointComponent(this, index);
	}

	public List<Component> createWireComponents()
	{
		List<Component> components = new ArrayList<Component>(_points.length);
		for (int i = 0; i < _points.length; i++)
			components.add(createWireComponent(i));
		return components;
	}

	@Override
	public String toString()
	{
		return "W[" + String.valueOf(getSource()) + " >(" + _value + ")> "
			+ String.valueOf(getDrain()) + "]";
	}

	@Override
	public Sprite createSprite()
	{
		return new WireSprite(this);
	}

	@Override
	public void reset()
	{
		_value = 0;
	}
}
