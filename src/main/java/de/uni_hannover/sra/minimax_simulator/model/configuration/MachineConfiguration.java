package de.uni_hannover.sra.minimax_simulator.model.configuration;

import com.google.common.collect.ImmutableList;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.MachineConfigAluEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.MachineConfigMuxEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.MachineConfigRegisterEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListener;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.NullMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.RegisterMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;

import java.util.*;

/**
 * This class represents the configuration of a register machine. <br>
 * <br>
 * Represented machines consist at least of some registers (which can be divided into base registers
 * and extension (user) registers), an ALU for the execution of binary operations and two
 * multiplexers providing the input for the ALU. <br>
 * <br>
 * The purpose of this class is loose the coupling between a user-modifiable, export-friendly
 * configuration of a machine and its actual simulation, representation and persistence. <br>
 * Instances are created using an {@link MachineConfigurationBuilder}. <br>
 * This class makes use of the <i>Listener</i> pattern: If the user wishes to visualize or simulate
 * a concrete machine represented by an instance of this class, client classes have to register as
 * {@link MachineConfigListener} to the instance and then synchronize to it on their own.
 * 
 * @author Martin L&uuml;ck
 */
// is final since the only constructor is package-private
public final class MachineConfiguration {

	private final List<MachineConfigListener>	_listeners;

	private final List<AluOperation>			_alu;
	private final List<AluOperation>			_aluView;

	private final List<RegisterExtension>		_baseRegisters;

	private final List<RegisterExtension>		_extendedRegisters;
	private final List<RegisterExtension>		_registersView;

	private final List<MuxInput>				_muxSourcesA;
	private final List<MuxInput>				_muxSourcesB;
	private final List<MuxInput>				_muxSourcesAView;
	private final List<MuxInput>				_muxSourcesBView;

	private final List<MuxInput>				_availableMuxSources;
	private final List<MuxInput>				_availableMuxSourcesView;

	MachineConfiguration(List<AluOperation> aluOperations, List<RegisterExtension> baseRegisters, List<RegisterExtension> extendedRegisters,
						 List<MuxInput> availableMuxInput, Map<MuxType, List<MuxInput>> selectedMuxInput) {
		_listeners = new ArrayList<MachineConfigListener>(5);

		_alu = new ArrayList<AluOperation>(aluOperations);
		_aluView = Collections.unmodifiableList(_alu);

		if (_alu.contains(null)) {
			throw new NullPointerException("Alu operations cannot contain null");
		}

		_baseRegisters = ImmutableList.copyOf(baseRegisters);

		// implicitly check for null
		for (RegisterExtension reg : _baseRegisters) {
			if (reg.isExtended()) {
				throw new IllegalArgumentException("Base register cannot have isExtended");
			}
		}

		// implicitly check for null
		_extendedRegisters = new ArrayList<RegisterExtension>(extendedRegisters);
		_registersView = Collections.unmodifiableList(_extendedRegisters);

		for (RegisterExtension reg : _extendedRegisters) {
			if (!reg.isExtended()) {
				throw new IllegalArgumentException("Extended register must have isExtended set");
			}
		}

		_availableMuxSources = new ArrayList<MuxInput>(availableMuxInput);
		_availableMuxSourcesView = Collections.unmodifiableList(_availableMuxSources);

		List<MuxInput> sourcesA = selectedMuxInput.get(MuxType.A);
		List<MuxInput> sourcesB = selectedMuxInput.get(MuxType.B);

		if (_availableMuxSources.contains(null)) {
			throw new NullPointerException("Mux inputs cannot contain null");
		}

		_muxSourcesA = new ArrayList<MuxInput>(sourcesA);
		_muxSourcesB = new ArrayList<MuxInput>(sourcesB);
		_muxSourcesAView = Collections.unmodifiableList(_muxSourcesA);
		_muxSourcesBView = Collections.unmodifiableList(_muxSourcesB);
	}

	/**
	 * Adds an {@link AluOperation} to the machine's configuration.
	 *
	 * @param aluOp
	 *          the {@code AluOperation} to add
	 */
	public void addAluOperation(AluOperation aluOp) {
		if (_alu.contains(aluOp)) {
			throw new IllegalStateException("Already contains " + aluOp);
		}

		_alu.add(aluOp);
		postEvent(MachineConfigAluEvent.eventAdded(aluOp, _alu.size() - 1));
	}

	/**
	 * Removes an {@link AluOperation} from the machine's configuration.
	 *
	 * @param aluOp
	 *          the {@code AluOperation} to remove
	 */
	public void removeAluOperation(AluOperation aluOp) {
		int index = _alu.indexOf(aluOp);
		if (index == -1) {
			throw new IllegalStateException(aluOp + " not in list");
		}

		_alu.remove(index);
		postEvent(MachineConfigAluEvent.eventRemoved(aluOp, index));
	}

	/**
	 * Exchanges two {@link AluOperation}s of the machine's configuration.
	 *
	 * @param index1
	 *          the index of the first {@code AluOperation}
	 * @param index2
	 *          the index of the second {@code AluOperation}
	 */
	public void exchangeAluOperations(int index1, int index2) {
		if (index1 == index2) {
			return;
		}

		AluOperation alu1 = _alu.get(index1);
		AluOperation alu2 = _alu.get(index2);
		_alu.set(index2, alu1);
		_alu.set(index1, alu2);

		postEvent(MachineConfigAluEvent.eventExchanged(alu1, alu2, index1, index2));
	}

	/**
	 * Gets the {@link AluOperation} at the specified index.
	 *
	 * @param index
	 *          the index of the {@code AluOperation} to get
	 * @return
	 *          the {@code AluOperation} at the specified index
	 */
	public AluOperation getAluOperation(int index) {
		return _alu.get(index);
	}

	/**
	 * Gets all {@link AluOperation}s of the machine's configuration.
	 *
	 * @return
	 *          a list of all {@code AluOperation}s of the machine
	 */
	public List<AluOperation> getAluOperations() {
		return _aluView;
	}

	/**
	 * Adds a register ({@link RegisterExtension}) to the machine's configuration.
	 *
	 * @param register
	 *          the {@code RegisterExtension} to add
	 */
	public void addRegisterExtension(RegisterExtension register) {
		if (_extendedRegisters.contains(register)) {
			throw new IllegalStateException("Already contains " + register);
		}

		if (!register.isExtended()) {
			throw new IllegalArgumentException("Can only add extended registers");
		}

		_extendedRegisters.add(register);
		postEvent(MachineConfigRegisterEvent.eventAdded(register, _extendedRegisters.size() - 1));

		RegisterMuxInput rm = new RegisterMuxInput(register.getName());
		_availableMuxSources.add(rm);
		postEvent(MachineConfigMuxEvent.eventAdded(null, rm, _availableMuxSources.size() - 1));
	}

	/**
	 * Removes a register ({@link RegisterExtension}) from the machine's configuration.
	 *
	 * @param register
	 *          the {@code RegisterExtension} to remove
	 */
	public void removeRegisterExtension(RegisterExtension register) {
		int index = _extendedRegisters.indexOf(register);
		if (index == -1) {
			throw new IllegalStateException(register + " not in list");
		}

		removeRegister(register, index);
	}

	/**
	 * Removes the register ({@link RegisterExtension}) at the specified index.
	 *
	 * @param index
	 *          the index of the {@code RegisterExtension} to remove
	 */
	public void removeRegisterExtension(int index) {
		RegisterExtension register = _extendedRegisters.get(index);

		removeRegister(register, index);
	}

	/**
	 * Removes the specified register ({@link RegisterExtension}) at the specified index.
	 *
	 * @param register
	 *          the {@code RegisterExtension} to remove
	 * @param index
	 *          the index of the {@code RegisterExtension}
	 */
	private void removeRegister(RegisterExtension register, int index) {
		removeAllMuxInputsOfRegister(register.getName(), null, _availableMuxSources);

		for (MuxType type : MuxType.values()) {
			replaceAllMuxInputsOfRegister(register.getName(), type, getMuxSourcesInternal(type));
		}

		_extendedRegisters.remove(index);
		postEvent(MachineConfigRegisterEvent.eventRemoved(register, index));
	}

	/**
	 * Removes the specified register ({@link RegisterExtension}) from the specified
	 * {@link MuxType} and list of {@link MuxInput}s.
	 *
	 * @param registerName
	 *          the name of the {@code RegisterExtension} to remove
	 * @param type
	 *          the {@code MuxType} the {@code RegisterExtension} should be removed from
	 * @param list
	 *          a list of the {@code MuxInput}s of the multiplexer
	 */
	private void removeAllMuxInputsOfRegister(String registerName, MuxType type, List<MuxInput> list) {
		for (ListIterator<MuxInput> muxIterator = list.listIterator(); muxIterator.hasNext(); ) {
			int i = muxIterator.nextIndex();
			MuxInput input = muxIterator.next();

			if (input instanceof RegisterMuxInput && ((RegisterMuxInput) input).getRegisterName().equals(registerName)) {
				muxIterator.remove();
				postEvent(MachineConfigMuxEvent.eventRemoved(type, input, i));
			}
		}
	}

	/**
	 * Replaces all occurrences of the specified register ({@link RegisterExtension}) at
	 * the specified multiplexer ({@link MuxType}) and the list of the {@link MuxInput}s
	 * with {@link NullMuxInput#INSTANCE}.
	 *
	 * @param registerName
	 *          the name of the {@code RegisterExtension} to replace
	 * @param type
	 *          the {@code MuxType} the {@code RegisterExtension} should be replaced at
	 * @param list
	 *          a list of the {@code MuxInput}s of the multiplexer
	 */
	private void replaceAllMuxInputsOfRegister(String registerName, MuxType type, List<MuxInput> list) {
		for (ListIterator<MuxInput> muxIterator = list.listIterator(); muxIterator.hasNext(); ) {
			int i = muxIterator.nextIndex();
			MuxInput input = muxIterator.next();

			if (input instanceof RegisterMuxInput && ((RegisterMuxInput) input).getRegisterName().equals(registerName)) {
				muxIterator.set(NullMuxInput.INSTANCE);
				postEvent(MachineConfigMuxEvent.eventReplaced(type, input, NullMuxInput.INSTANCE, i));
			}
		}
	}

	/**
	 * Replaces all occurrences of the specified register ({@link RegisterExtension}) at
	 * the specified multiplexer ({@link MuxType}) and the list of the {@link MuxInput}s
	 * with another register.
	 *
	 * @param oldRegisterName
	 *          the name of the {@code RegisterExtension} to replace
	 * @param newRegisterName
	 *          the name of the {@code RegisterExtension} to use instead
	 * @param type
	 *          the {@code MuxType} the {@code RegisterExtension} should be replaced at
	 * @param list
	 *          a list of the {@code MuxInput}s of the multiplexer.
	 */
	private void replaceAllMuxInputsOfRegister(String oldRegisterName, String newRegisterName, MuxType type, List<MuxInput> list) {
		for (ListIterator<MuxInput> muxIterator = list.listIterator(); muxIterator.hasNext(); ) {
			int i = muxIterator.nextIndex();
			MuxInput input = muxIterator.next();

			if (input instanceof RegisterMuxInput && ((RegisterMuxInput) input).getRegisterName().equals(oldRegisterName)) {
				RegisterMuxInput newInput = new RegisterMuxInput(newRegisterName);
				muxIterator.set(newInput);
				postEvent(MachineConfigMuxEvent.eventReplaced(type, input, newInput, i));
			}
		}
	}

	/**
	 * Sets the specified register ({@link RegisterExtension}) at the specified index.
	 *
	 * @param index
	 *          the index where the {@code RegisterExtension} should be set
	 * @param register
	 *          the {@code RegisterExtension} to set
	 */
	public void setRegisterExtension(int index, RegisterExtension register) {
		if (!register.isExtended()) {
			throw new IllegalArgumentException("Can only add extended registers");
		}

		RegisterExtension oldRegister = _extendedRegisters.get(index);

		if (!oldRegister.equals(register) && _extendedRegisters.contains(register)) {
			throw new IllegalStateException("Already contains " + register);
		}

		// fetch the indices of the mux inputs that currently use this register
		Map<MuxType, List<Integer>> indicesInUse = new EnumMap<MuxType, List<Integer>>(MuxType.class);
		for (MuxType type : MuxType.values()) {
			indicesInUse.put(type, fetchRegisterInputIndices(oldRegister.getName(), getMuxSourcesInternal(type)));
		}

		// replace the mux inputs that are currently using the register with null
		for (MuxType type : MuxType.values()) {
			List<MuxInput> inputs = getMuxSourcesInternal(type);
			for (int inputIndex : indicesInUse.get(type)) {
				MuxInput oldInput = inputs.get(inputIndex);
				inputs.set(inputIndex, NullMuxInput.INSTANCE);
				postEvent(MachineConfigMuxEvent.eventReplaced(type, oldInput, NullMuxInput.INSTANCE, inputIndex));
			}
		}

		// now, since no mux input is pointing to the register, actually replace the register
		_extendedRegisters.set(index, register);

		// notify clients that the register was replaced
		postEvent(MachineConfigRegisterEvent.eventReplaced(oldRegister, register, index));

		// replace the available mux input for this register since the register changed
		replaceAllMuxInputsOfRegister(oldRegister.getName(), register.getName(), null, _availableMuxSources);

		// put the new mux input at the places set to null before
		RegisterMuxInput newInput = new RegisterMuxInput(register.getName());
		for (MuxType type : MuxType.values()) {
			List<MuxInput> inputs = getMuxSourcesInternal(type);
			for (Integer inputIndex : indicesInUse.get(type)) {
				MuxInput oldInput = inputs.get(inputIndex);
				inputs.set(inputIndex.intValue(), newInput);
				postEvent(MachineConfigMuxEvent.eventReplaced(type, oldInput, newInput, inputIndex));
			}
		}
	}

	/**
	 * Fetches the indices of all occurrences of the register ({@link RegisterExtension}) specified
	 * by name at the specified list of {@link MuxInput}s.
	 *
	 * @param registerName
	 *          the name of the {@code RegisterExtension}
	 * @param list
	 *          the list of the {@code MuxInput}s
	 * @return
	 *          a list containing the indices of all occurrences of the {@code RegisterExtension}
	 */
	private List<Integer> fetchRegisterInputIndices(String registerName, List<MuxInput> list) {
		List<Integer> result = new ArrayList<Integer>();
		ListIterator<MuxInput> iter = list.listIterator();
		while (iter.hasNext()) {
			int idx = iter.nextIndex();
			MuxInput mux = iter.next();
			if (mux instanceof RegisterMuxInput) {
				if (((RegisterMuxInput) mux).getRegisterName().equals(registerName)) {
					result.add(idx);
				}
			}
		}
		return result;
	}

	/**
	 * Exchanges two registers ({@link RegisterExtension}) of the machine's configuration.
	 *
	 * @param index1
	 *          the index of the first {@code RegisterExtension}
	 * @param index2
	 *          the index of the second {@code RegisterExtension}
	 */
	public void exchangeRegisterExtensions(int index1, int index2) {
		if (index1 == index2) {
			return;
		}

		RegisterExtension reg1 = _extendedRegisters.get(index1);
		RegisterExtension reg2 = _extendedRegisters.get(index2);
		_extendedRegisters.set(index2, reg1);
		_extendedRegisters.set(index1, reg2);

		postEvent(MachineConfigRegisterEvent.eventExchanged(reg1, reg2, index1, index2));

		int muxIdx1 = -1;
		int muxIdx2 = -1;
		MuxInput muxInput1 = null;
		MuxInput muxInput2 = null;
		for (MuxInput mux : _availableMuxSources) {
			if (mux instanceof RegisterMuxInput) {
				String registerName = ((RegisterMuxInput) mux).getRegisterName();
				if (registerName.equals(reg1.getName())) {
					muxIdx1 = _availableMuxSources.indexOf(mux);
					muxInput1 = mux;
				}
				if (registerName.equals(reg2.getName())) {
					muxIdx2 = _availableMuxSources.indexOf(mux);
					muxInput2 = mux;
				}
			}
		}

		_availableMuxSources.set(muxIdx1, muxInput2);
		_availableMuxSources.set(muxIdx2, muxInput1);
		postEvent(MachineConfigMuxEvent.eventExchanged(null, muxInput1, muxInput2, muxIdx1, muxIdx2));
	}

	/**
	 * Gets the register ({@link RegisterExtension}) at the specified index.
	 *
	 * @param index
	 *          the index of the {@code RegisterExtension} to get
	 * @return
	 *          the {@code RegisterExtension} at the specified index
	 */
	public RegisterExtension getRegisterExtension(int index) {
		return _extendedRegisters.get(index);
	}

	/**
	 * Gets the base register ({@link RegisterExtension}) at the specified index.
	 *
	 * @param index
	 *          the index of the {@code RegisterExtension} to get
	 * @return
	 *          the {@code RegisterExtension} at the specified index
	 */
	public RegisterExtension getBaseRegister(int index) {
		return _baseRegisters.get(index);
	}

	/**
	 * Searches a register ({@link RegisterExtension}) with the specified name in
	 * the list of the extended registers.
	 *
	 * @param name
	 *          the name of the {@code RegisterExtension} to search
	 * @return
	 *          the {@code RegisterExtension} with the specified name, if it exists, {@code null} otherwise
	 */
	public RegisterExtension findRegisterExtension(String name) {
		for (RegisterExtension reg : _extendedRegisters) {
			if (reg.getName().equalsIgnoreCase(name)) {
				return reg;
			}
		}
		return null;
	}

	/**
	 * Searches a register ({@link RegisterExtension}) with the specified name in
	 * the list of the base registers.
	 *
	 * @param name
	 *          the name of the {@code RegisterExtension} to search
	 * @return
	 *          the {@code RegisterExtension} with the specified name, if it exists, {@code null} otherwise
	 */
	public RegisterExtension findBaseRegister(String name) {
		for (RegisterExtension reg : _baseRegisters) {
			if (reg.getName().equalsIgnoreCase(name)) {
				return reg;
			}
		}
		return null;
	}

	/**
	 * Gets a list of all extended registers ({@link RegisterExtension}).
	 *
	 * @return
	 *          a list of all extended {@code RegisterExtension}s
	 */
	public List<RegisterExtension> getRegisterExtensions() {
		return _registersView;
	}

	/**
	 * Gets a list of all base registers ({@link RegisterExtension}).
	 *
	 * @return
	 *          a list of all base {@code RegisterExtension}s
	 */
	public List<RegisterExtension> getBaseRegisters() {
		return _baseRegisters;
	}

	/**
	 * Gets a list of all {@link MuxInput}s available to the machine.
	 *
	 * @return
	 *          a list of all available {@code MuxInput}s
	 */
	public List<MuxInput> getAvailableSources() {
		return _availableMuxSourcesView;
	}

	/**
	 * Gets a list of all {@link MuxInput}s of the specified multiplexer ({@link MuxType}).
	 *
	 * @param mux
	 *          the {@code MuxType} for which all {@code MuxInput}s should be returned
	 * @return
	 *          a list of all {@code MuxInput}s of the specified multiplexer
	 */
	public List<MuxInput> getMuxSources(MuxType mux) {
		switch (mux) {
			case A:
				return _muxSourcesAView;
			case B:
				return _muxSourcesBView;
		}
		throw new IllegalArgumentException(mux.toString());
	}

	/**
	 * Gets a list of all internal {@link MuxInput}s of the specified multiplexer ({@link MuxType}).
	 *
	 * @param mux
	 *          the {@code MuxType} for which all internal {@code MuxInput}s should be returned
	 * @return
	 *          a list of all internal {@code MuxInput}s of the specified multiplexer
	 */
	private List<MuxInput> getMuxSourcesInternal(MuxType mux) {
		switch (mux) {
			case A:
				return _muxSourcesA;
			case B:
				return _muxSourcesB;
		}
		throw new IllegalArgumentException(mux.toString());
	}

	/**
	 * Adds the specified {@link MuxInput} to the specified multiplexer ({@link MuxType}).
	 *
	 * @param mux
	 *          the {@code MuxType} where the {@code MuxInput} should be added
	 * @param source
	 *          the {@code MuxInput} to add
	 */
	public void addMuxSource(MuxType mux, MuxInput source) {
		List<MuxInput> muxList = getMuxSourcesInternal(mux);

		// it is allowed to have duplicate inputs at the same multiplexer
		// if (muxList.contains(source))
		// throw new IllegalStateException("Mux " + mux + " already contains " + source);

		muxList.add(source);
		postEvent(MachineConfigMuxEvent.eventAdded(mux, source, muxList.size() - 1));
	}

	/**
	 * Removes all occurrences of the specified {@link MuxInput} from the specified
	 * multiplexer ({@link MuxType}).
	 *
	 * @param mux
	 *          the {@code MuxType} where the {@code MuxInput} should be removed
	 * @param source
	 *          the {@code MuxInput} to remove
	 */
	public void removeMuxSource(MuxType mux, MuxInput source) {
		List<MuxInput> muxList = getMuxSourcesInternal(mux);
		int index;
		do {
			index = muxList.lastIndexOf(source);
			muxList.remove(index);
			postEvent(MachineConfigMuxEvent.eventRemoved(mux, source, index));
		}
		while (index != -1);
	}

	/**
	 * Removes the {@link MuxInput} at the specified index from the specified multiplexer
	 * ({@link MuxType}).
	 *
	 * @param mux
	 *          the {@code MuxType} where the {@code MuxInput} should be removed
	 * @param index
	 *          the index of the {@code MuxInput} to remove
	 */
	public void removeMuxSource(MuxType mux, int index) {
		List<MuxInput> muxList = getMuxSourcesInternal(mux);
		MuxInput source = muxList.remove(index);
		postEvent(MachineConfigMuxEvent.eventRemoved(mux, source, index));
	}

	/**
	 * Sets the specified {@link MuxInput} at the specified index of the specified
	 * multiplexer ({@link MuxType}).
	 *
	 * @param mux
	 *          the {@code MuxType} where the {@code MuxInput} should be set
	 * @param index
	 *          the index at which the {@code MuxInput} should be set
	 * @param source
	 *          the {@code MuxInput} to set
	 */
	public void setMuxSource(MuxType mux, int index, MuxInput source) {
		List<MuxInput> muxList = getMuxSourcesInternal(mux);
		MuxInput oldSource = muxList.get(index);
		muxList.set(index, source);
		postEvent(MachineConfigMuxEvent.eventReplaced(mux, oldSource, source, index));
	}

	/**
	 * Exchanges two {@link MuxInput}s of the specified multiplexer ({@link MuxType}).
	 *
	 * @param mux
	 *          the {@code MuxType} where the {@code MuxInput}s should be replaced
	 * @param index1
	 *          the index of the first {@code MuxInput}
	 * @param index2
	 *          the index of the second {@code MuxInput}
	 */
	public void exchangeMuxSources(MuxType mux, int index1, int index2) {
		if (index1 == index2) {
			return;
		}

		List<MuxInput> list = getMuxSourcesInternal(mux);
		MuxInput input1 = list.get(index1);
		MuxInput input2 = list.get(index2);
		list.set(index2, input1);
		list.set(index1, input2);

		postEvent(MachineConfigMuxEvent.eventExchanged(mux, input1, input2, index1, index2));
	}

	/**
	 * Registers a new {@link MachineConfigListener}.
	 *
	 * @param listener
	 *          the {@code MachineConfigListener} to register
	 */
	public void addMachineConfigListener(MachineConfigListener listener) {
		if (!_listeners.contains(listener)) {
			_listeners.add(listener);
		}
	}

	/**
	 * Removes a {@link MachineConfigListener} from the list of listeners.
	 *
	 * @param listener
	 *          the {@code MachineConfigListener} to remove
	 */
	public void removeMachineConfigListener(MachineConfigListener listener) {
		_listeners.remove(listener);
	}

	/**
	 * Notifies all registered {@link MachineConfigListener}s of the specified
	 * {@link MachineConfigEvent}.
	 *
	 * @param e
	 *          the {@code MachineConfigEvent} the listeners have to be notified of
	 */
	protected void postEvent(MachineConfigEvent e) {
		for (MachineConfigListener l : _listeners) {
			l.processEvent(e);
		}
	}

	@Override
	public String toString() {
		return "MachineConfiguration [alu=" + _alu + ", registers=" + _extendedRegisters
			+ ", mux.A=" + _muxSourcesA + ", mux.B=" + _muxSourcesB + "]";
	}
}