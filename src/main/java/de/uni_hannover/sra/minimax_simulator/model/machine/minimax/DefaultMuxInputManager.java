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

class DefaultMuxInputManager implements MuxInputManager
{
	private final MinimaxTopology				_topology;
	private final MinimaxLayout					_layout;
	private final MinimaxDisplay				_display;

	private final ArrayList<InputEntry>			_inputs;

	private final MuxType						_type;
	private final Multiplexer					_mux;
	private final String						_muxComponentName;

	private final List<MuxInputGroupManager>	_groupManagers;

	private int									_currentElementId;

	public DefaultMuxInputManager(MuxType type, String muxComponentName,
			MinimaxMachine machine)
	{
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
	public List<InputEntry> getMuxInputs()
	{
		return _inputs;
	}

	@Override
	public void add(MuxInput input)
	{
		addInternal(input);

		update();
	}

	@Override
	public void addAll(Collection<? extends MuxInput> inputs)
	{
		for (MuxInput element : inputs)
			addInternal(element);

		update();
	}

	@Override
	public void remove(int index)
	{
		removeInternal(index, false);

		for (int i = index, n = _inputs.size(); i < n; i++)
			layoutPinComponent(_inputs.get(i).pinId, i);

		update();
	}

	@Override
	public void swap(int index1, int index2)
	{
		Collections.swap(_mux.getDataInputs(), index1, index2);
		Collections.swap(_inputs, index1, index2);

		layoutPinComponent(_inputs.get(index1).pinId, index1);
		layoutPinComponent(_inputs.get(index2).pinId, index2);

		update();
	}

	@Override
	public void set(int index, MuxInput input)
	{
		removeInternal(index, true);
		addInternal(index, input, true);

		update();
	}

	private void addInternal(MuxInput element)
	{
		addInternal(_inputs.size(), element, false);
	}

	private void addInternal(int index, MuxInput element, boolean updating)
	{
		// Generate a new name for the Pin object.
		String pinId = getMuxPinId(nextPinId());

		InputEntry entry = new InputEntry();
		entry.input = element;
		entry.pinId = pinId;

		// Add and layout the pin
		entry.pin = addPinComponent(pinId, index);

		// Add the name to the list of pin names
		if (updating)
			_inputs.set(index, entry);
		else
			_inputs.add(index, entry);
	}

	private void removeInternal(int index, boolean updating)
	{
		InputEntry entry = updating ? _inputs.get(index) : _inputs.remove(index);

		removePinComponent(entry.pinId, index);
	}

	private int nextPinId()
	{
		return _currentElementId++;
	}

	private String getMuxPinId(int index)
	{
		return Parts.MUX_INPUT + ":" + _muxComponentName + ":" + index;
	}

	// Add and layout a pin at the given position.
	private IngoingPin addPinComponent(String pinId, int index)
	{
		IngoingPin pin = new IngoingPin(_mux);
		_mux.getDataInputs().add(index, pin);
		_layout.getContainer().addComponent(pin, pinId);
		layoutPinComponent(pinId, index);
		return pin;
	}

	private void removePinComponent(String name, int index)
	{
		_mux.getDataInputs().remove(index);
		_layout.removeLayout(name);
		_layout.getContainer().removeComponent(name);
	}

	// Fetch the name of the pin at the given index and constrain it.
	private void layoutPinComponent(String pinId, int index)
	{
		ConstraintBuilder cb = new ConstraintBuilder();
		int yOffset = MuxShape.MUX_CORNER_SPACING + index * MuxShape.MUX_HEIGHT_PER_PIN;
		cb.left(_muxComponentName).above(_muxComponentName, -yOffset);

		_layout.putLayout(pinId, new DefaultLayout(cb.constraints()));
	}

	@Override
	public MuxType getMuxType()
	{
		return _type;
	}

	private void update()
	{
		for (MuxInputGroupManager mig : _groupManagers)
			mig.update(this);

		_layout.updateLayout();
		_display.setDimension(_layout.getDimension());
	}

	@Override
	public void registerGroupManager(MuxInputGroupManager inputGroupManager)
	{
		_groupManagers.add(inputGroupManager);
	}
}