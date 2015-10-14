package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterSize;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.SynchronousCircuit;
import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.parts.RegisterSprite;

import java.util.Collections;
import java.util.Set;

public class Register extends SimplePart implements SynchronousCircuit, SpriteOwner
{
	private final RegisterSize	_size;
	private final boolean		_isExtended;
	private final IngoingPin	_dataIn;
	private final IngoingPin	_writeEnabled;

	private final String		_label;

	private int					_value;

	public Register(String label)
	{
		this(label, RegisterSize.BITS_32, false);
	}

	public Register(String label, RegisterSize size, boolean isExtended)
	{
		_label = label;
		_size = size;

		_value = 0;
		_isExtended = isExtended;

		_dataIn = new IngoingPin(this);
		_writeEnabled = new IngoingPin(this);
	}

	public Register(RegisterExtension ext)
	{
		this(ext.getName(), ext.getSize(), ext.isExtended());
	}

	public int getValue()
	{
		return _value;
	}

	public void setValue(int value)
	{
		value = _size.getBitMask() & value;
		_value = value;
		getDataOut().setValue(value);
	}

	@Override
	public void update()
	{
	}

	@Override
	public Set<Circuit> getSuccessors()
	{
		return Collections.emptySet();
	}

	public IngoingPin getDataIn()
	{
		return _dataIn;
	}

	public IngoingPin getWriteEnabled()
	{
		return _writeEnabled;
	}

	@Override
	public void nextCycle()
	{
		if (_writeEnabled.read() != 0)
		{
			_value = _dataIn.read();
			getDataOut().write(_value);
		}
	}

	public boolean isExtended()
	{
		return _isExtended;
	}

	public String getLabel()
	{
		return _label;
	}

	@Override
	public Sprite createSprite()
	{
		return new RegisterSprite(this);
	}

	public RegisterSize getSize()
	{
		return _size;
	}

	@Override
	public void reset()
	{
		setValue(0);
	}

	@Override
	public String toString()
	{
		return "Register[name=" + getName() + ", value=" + _value + "]";
	}
}