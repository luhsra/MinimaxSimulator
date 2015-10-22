package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.ConstraintBuilder;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.DefaultLayout;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.IngoingPin;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Multiplexer;
import de.uni_hannover.sra.minimax_simulator.model.machine.shape.MuxShape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Default implementation of the {@link MuxInputManager}.
 *
 * @author Martin L&uuml;ck
 */
class DefaultMuxInputManager implements MuxInputManager {

	private final MinimaxTopology				_topology;
	private final MinimaxLayout					_layout;
	private final MinimaxDisplay				_display;

	private final ArrayList<InputEntry>			_inputs;

	private final MuxType						_type;
	private final Multiplexer					_mux;
	private final String						_muxComponentName;

	private final List<MuxInputGroupManager>	_groupManagers;

	private int									_currentElementId;

	/**
	 * Constructs a new {@code DefaultMuxInputManager} with the specified {@link MuxType},
	 * name of the multiplexer component and {@link MinimaxMachine}.
	 *
	 * @param type
	 *          the multiplexer
	 * @param muxComponentName
	 *          the name of the multiplexer component
	 * @param machine
	 *          the {@code MinimaxMachine}
	 */
	public DefaultMuxInputManager(MuxType type, String muxComponentName, MinimaxMachine machine) {
		_type = type;

		_topology = machine.getTopology();
		_layout = machine.getLayout();
		_display = machine.getDisplay();

		_mux = _topology.getCircuit(Multiplexer.class, muxComponentName);
		_muxComponentName = muxComponentName;

		_inputs = new ArrayList<InputEntry>();

		_groupManagers = new ArrayList<MuxInputGroupManager>();
	}

	@Override
	public List<InputEntry> getMuxInputs() {
		return _inputs;
	}

	@Override
	public void add(MuxInput input) {
		addInternal(input);

		update();
	}

	@Override
	public void addAll(Collection<? extends MuxInput> inputs) {
		for (MuxInput element : inputs) {
			addInternal(element);
		}

		update();
	}

	@Override
	public void remove(int index) {
		removeInternal(index, false);

		for (int i = index, n = _inputs.size(); i < n; i++) {
			layoutPinComponent(_inputs.get(i).pinId, i);
		}

		update();
	}

	@Override
	public void swap(int index1, int index2) {
		Collections.swap(_mux.getDataInputs(), index1, index2);
		Collections.swap(_inputs, index1, index2);

		layoutPinComponent(_inputs.get(index1).pinId, index1);
		layoutPinComponent(_inputs.get(index2).pinId, index2);

		update();
	}

	@Override
	public void set(int index, MuxInput input) {
		removeInternal(index, true);
		addInternal(index, input, true);

		update();
	}

	/**
	 * Adds the specified {@link MuxInput} to the internal list of inputs.
	 *
	 * @param element
	 *          the {@code MuxInput} to add
	 */
	private void addInternal(MuxInput element) {
		addInternal(_inputs.size(), element, false);
	}

	/**
	 * Adds the specified {@link MuxInput} to the internal list of inputs at
	 * the specified index with the specified {@code updating} property.
	 *
	 * @param index
	 *          the index of the new {@code MuxInput}
	 * @param element
	 *          the {@code MuxInput} to add
	 * @param updating
	 *          whether the {@link MuxInputGroupManager}s should update afterwards
	 */
	private void addInternal(int index, MuxInput element, boolean updating) {
		// generate a new name for the Pin object.
		String pinId = getMuxPinId(nextPinId());

		InputEntry entry = new InputEntry();
		entry.input = element;
		entry.pinId = pinId;

		// add and layout the pin
		entry.pin = addPinComponent(pinId, index);

		// add the name to the list of pin names
		if (updating) {
			_inputs.set(index, entry);
		}
		else {
			_inputs.add(index, entry);
		}
	}

	/**
	 * Removes the {@link MuxInput} at the specified index from the internal list of inputs
	 * with the specified {@code updating} property.
	 *
	 * @param index
	 *          the index of the {@code MuxInput} to remove
	 * @param updating
	 *          whether the {@link MuxInputGroupManager}s should update afterwards
	 */
	private void removeInternal(int index, boolean updating) {
		InputEntry entry = updating ? _inputs.get(index) : _inputs.remove(index);

		removePinComponent(entry.pinId, index);
	}

	/**
	 * Gets the next available Pin ID.
	 *
	 * @return
	 *          the next Pin ID
	 */
	private int nextPinId() {
		return _currentElementId++;
	}

	/**
	 * Gets the multiplexer Pin ID of the {@link MuxInput} at the specified index.
	 *
	 * @param index
	 *          the index of the {@code MuxInput}
	 * @return
	 *          the Pin ID of the {@code MuxInput}
	 */
	private String getMuxPinId(int index) {
		return Parts.MUX_INPUT + ":" + _muxComponentName + ":" + index;
	}

	// add and layout a pin at the given position.

	/**
	 * Adds and layouts a Pin at the specified position.
	 *
	 * @param pinId
	 *          the ID of the Pin
	 * @param index
	 *          the index of the {@code MuxInput}
	 * @return
	 *          the {@code IngoingPin} related to the {@code MuxInput}
	 */
	private IngoingPin addPinComponent(String pinId, int index) {
		IngoingPin pin = new IngoingPin(_mux);
		_mux.getDataInputs().add(index, pin);
		_layout.getContainer().addComponent(pin, pinId);
		layoutPinComponent(pinId, index);
		return pin;
	}

	/**
	 * Removes the Pin at the specified position.
	 *
	 * @param name
	 *          the ID of the Pin
	 * @param index
	 *          the index of the {@code MuxInput}
	 */
	private void removePinComponent(String name, int index) {
		_mux.getDataInputs().remove(index);
		_layout.removeLayout(name);
		_layout.getContainer().removeComponent(name);
	}

	/**
	 * Fetches the name of the Pin at the specified index and constrains it.
	 *
	 * @param pinId
	 *          the ID of the Pin
	 * @param index
	 *          the index of the {@code MuxInput}
	 */
	private void layoutPinComponent(String pinId, int index) {
		ConstraintBuilder cb = new ConstraintBuilder();
		int yOffset = MuxShape.MUX_CORNER_SPACING + index * MuxShape.MUX_HEIGHT_PER_PIN;
		cb.left(_muxComponentName).above(_muxComponentName, -yOffset);

		_layout.putLayout(pinId, new DefaultLayout(cb.constraints()));
	}

	@Override
	public MuxType getMuxType() {
		return _type;
	}

	/**
	 * Updates the {@link MuxInputGroupManager}s and the {@link MinimaxLayout}.
	 */
	private void update() {
		for (MuxInputGroupManager mig : _groupManagers) {
			mig.update(this);
		}

		_layout.updateLayout();
		_display.setDimension(_layout.getDimension());
	}

	@Override
	public void registerGroupManager(MuxInputGroupManager inputGroupManager) {
		_groupManagers.add(inputGroupManager);
	}
}