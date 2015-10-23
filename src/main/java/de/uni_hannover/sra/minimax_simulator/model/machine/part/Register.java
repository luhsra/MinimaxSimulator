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

/**
 * Implementation of a register a component part of a machine.
 *
 * @author Martin L&uuml;ck
 */
public class Register extends SimplePart implements SynchronousCircuit, SpriteOwner {

	private final RegisterSize	_size;
	private final boolean		_isExtended;
	private final IngoingPin	_dataIn;
	private final IngoingPin	_writeEnabled;

	private final String		_label;

	private int					_value;

	/**
	 * Constructs a new 32 Bits not-extended {@code Register} with the specified name.
	 *
	 * @param label
	 *          the name of the {@code Register}
	 */
	public Register(String label) {
		this(label, RegisterSize.BITS_32, false);
	}

	/**
	 * Constructs a new {@code Register} with the specified name, {@link RegisterSize} and {@code is extended} property.
	 *
	 * @param label
	 *          the name of the {@code Register}
	 * @param size
	 *          the {@code RegisterSize} of the {@code Register}
	 * @param isExtended
	 *          whether the {@code Register} is a base register or not
	 */
	public Register(String label, RegisterSize size, boolean isExtended) {
		_label = label;
		_size = size;

		_value = 0;
		_isExtended = isExtended;

		_dataIn = new IngoingPin(this);
		_writeEnabled = new IngoingPin(this);
	}

	/**
	 * Constructs a new {@code Register} using the specified {@link RegisterExtension}.
	 *
	 * @param ext
	 *          the {@code RegisterExtension} to create a {@code Register} from
	 */
	public Register(RegisterExtension ext) {
		this(ext.getName(), ext.getSize(), ext.isExtended());
	}

	/**
	 * Gets the value of the {@code Register}.
	 *
	 * @return
	 *          the value
	 */
	public int getValue() {
		return _value;
	}

	/**
	 * Sets the value of the {@code Register}.
	 *
	 * @param value
	 *          the new value
	 */
	public void setValue(int value) {
		value = _size.getBitMask() & value;
		_value = value;
		getDataOut().setValue(value);
	}

	@Override
	public void update() {

	}

	@Override
	public Set<Circuit> getSuccessors() {
		return Collections.emptySet();
	}

	/**
	 * Gets the data {@link IngoingPin}.
	 *
	 * @return
	 *          the data in pin
	 */
	public IngoingPin getDataIn() {
		return _dataIn;
	}

	/**
	 * Gets the control {@link IngoingPin}.
	 *
	 * @return
	 *          the control pin
	 */
	public IngoingPin getWriteEnabled() {
		return _writeEnabled;
	}

	@Override
	public void nextCycle() {
		if (_writeEnabled.read() != 0) {
			_value = _dataIn.read();
			getDataOut().write(_value);
		}
	}

	/**
	 * Gets the value of the {@code is extended} property.
	 *
	 * @return
	 *          {@code false} if the {@code Register} is part of the base machine, {@code true} otherwise
	 */
	public boolean isExtended() {
		return _isExtended;
	}

	/**
	 * Gets the name of the {@code Register}.
	 *
	 * @return
	 *          the name of the {@code Register}
	 */
	public String getLabel() {
		return _label;
	}

	@Override
	public Sprite createSprite() {
		return new RegisterSprite(this);
	}

	/**
	 * Gets the {@link RegisterSize} of the {@code Register}.
	 *
	 * @return
	 *          the size of the {@code Register}
	 */
	public RegisterSize getSize() {
		return _size;
	}

	@Override
	public void reset() {
		setValue(0);
	}

	@Override
	public String toString() {
		return "Register[name=" + getName() + ", value=" + _value + "]";
	}
}