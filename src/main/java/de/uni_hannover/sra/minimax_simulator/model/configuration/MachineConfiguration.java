package de.uni_hannover.sra.minimax_simulator.model.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

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

/**
 * This class represents the configuration of a register machine. <br>
 * <br>
 * Represented machines consist at least of some registers (which can be divided into base registers
 * and extension (user) registers), an ALU for the execution of binary operations and two
 * multiplexers providing the input for the ALU. <br>
 * <br>
 * The purpose of this class is loose the coupling between a user-modifiable, export-friendly
 * configuration of a machine and its actual simulation, representation and persistation. <br>
 * Instances are created using an {@link MachineConfigurationBuilder}. <br>
 * This class makes use of the <i>Listener</i> pattern: If the user wishes to visualize or simulate
 * a concrete machine represented by an instance of this class, client classes have to register as
 * {@link MachineConfigListener} to the instance and then sychronize to it on their own.
 * 
 * @author Martin
 * 
 */
// is final since the only constructor is package-private
public final class MachineConfiguration
{
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

	MachineConfiguration(List<AluOperation> aluOperations,
			List<RegisterExtension> baseRegisters,
			List<RegisterExtension> extendedRegisters, List<MuxInput> availableMuxInput,
			Map<MuxType, List<MuxInput>> selectedMuxInput)
	{
		_listeners = new ArrayList<MachineConfigListener>(5);

		_alu = new ArrayList<AluOperation>(aluOperations);
		_aluView = Collections.unmodifiableList(_alu);

		if (_alu.contains(null))
			throw new NullPointerException("Alu operations cannot contain null");

		_baseRegisters = ImmutableList.copyOf(baseRegisters);

		// Implicitly check for null
		for (RegisterExtension reg : _baseRegisters)
			if (reg.isExtended())
				throw new IllegalArgumentException("Base register cannot have isExtended");

		// Implicitly check for null
		_extendedRegisters = new ArrayList<RegisterExtension>(extendedRegisters);
		_registersView = Collections.unmodifiableList(_extendedRegisters);

		for (RegisterExtension reg : _extendedRegisters)
			if (!reg.isExtended())
				throw new IllegalArgumentException(
					"Extended register must have isExtended set");

		_availableMuxSources = new ArrayList<MuxInput>(availableMuxInput);
		_availableMuxSourcesView = Collections.unmodifiableList(_availableMuxSources);

		List<MuxInput> sourcesA = selectedMuxInput.get(MuxType.A);
		List<MuxInput> sourcesB = selectedMuxInput.get(MuxType.B);

		if (_availableMuxSources.contains(null))
			throw new NullPointerException("Mux inputs cannot contain null");

		_muxSourcesA = new ArrayList<MuxInput>(sourcesA);
		_muxSourcesB = new ArrayList<MuxInput>(sourcesB);
		_muxSourcesAView = Collections.unmodifiableList(_muxSourcesA);
		_muxSourcesBView = Collections.unmodifiableList(_muxSourcesB);
	}

	public void addAluOperation(AluOperation aluOp)
	{
		if (_alu.contains(aluOp))
			throw new IllegalStateException("Already contains " + aluOp);

		_alu.add(aluOp);
		postEvent(MachineConfigAluEvent.eventAdded(aluOp, _alu.size() - 1));
	}

	public void removeAluOperation(AluOperation aluOp)
	{
		int index = _alu.indexOf(aluOp);
		if (index == -1)
			throw new IllegalStateException(aluOp + " not in list");

		_alu.remove(index);
		postEvent(MachineConfigAluEvent.eventRemoved(aluOp, index));
	}

	public void exchangeAluOperations(int index1, int index2)
	{
		if (index1 == index2)
			return;

		AluOperation alu1 = _alu.get(index1);
		AluOperation alu2 = _alu.get(index2);
		_alu.set(index2, alu1);
		_alu.set(index1, alu2);

		postEvent(MachineConfigAluEvent.eventExchanged(alu1, alu2, index1, index2));
	}

	public AluOperation getAluOperation(int index)
	{
		return _alu.get(index);
	}

	public List<AluOperation> getAluOperations()
	{
		return _aluView;
	}

	public void addRegisterExtension(RegisterExtension register)
	{
		if (_extendedRegisters.contains(register))
			throw new IllegalStateException("Already contains " + register);

		if (!register.isExtended())
			throw new IllegalArgumentException("Can only add extended registers");

		_extendedRegisters.add(register);
		postEvent(MachineConfigRegisterEvent.eventAdded(register,
			_extendedRegisters.size() - 1));

		RegisterMuxInput rm = new RegisterMuxInput(register.getName());
		_availableMuxSources.add(rm);
		postEvent(MachineConfigMuxEvent.eventAdded(null, rm,
			_availableMuxSources.size() - 1));
	}

	public void removeRegisterExtension(RegisterExtension register)
	{
		int index = _extendedRegisters.indexOf(register);
		if (index == -1)
			throw new IllegalStateException(register + " not in list");

		removeRegister(register, index);
	}

	public void removeRegisterExtension(int index)
	{
		RegisterExtension register = _extendedRegisters.get(index);

		removeRegister(register, index);
	}

	private void removeRegister(RegisterExtension register, int index)
	{
		removeAllMuxInputsOfRegister(register.getName(), null, _availableMuxSources);

		for (MuxType type : MuxType.values())
			replaceAllMuxInputsOfRegister(register.getName(), type,
				getMuxSourcesInternal(type));

		_extendedRegisters.remove(index);
		postEvent(MachineConfigRegisterEvent.eventRemoved(register, index));
	}

	private void removeAllMuxInputsOfRegister(String registerName, MuxType type,
			List<MuxInput> list)
	{
		for (ListIterator<MuxInput> muxIterator = list.listIterator(); muxIterator.hasNext();)
		{
			int i = muxIterator.nextIndex();
			MuxInput input = muxIterator.next();

			if (input instanceof RegisterMuxInput
				&& ((RegisterMuxInput) input).getRegisterName().equals(registerName))
			{
				muxIterator.remove();
				postEvent(MachineConfigMuxEvent.eventRemoved(type, input, i));
			}
		}
	}

	private void replaceAllMuxInputsOfRegister(String registerName, MuxType type,
			List<MuxInput> list)
	{
		for (ListIterator<MuxInput> muxIterator = list.listIterator(); muxIterator.hasNext();)
		{
			int i = muxIterator.nextIndex();
			MuxInput input = muxIterator.next();

			if (input instanceof RegisterMuxInput
				&& ((RegisterMuxInput) input).getRegisterName().equals(registerName))
			{
				muxIterator.set(NullMuxInput.INSTANCE);
				postEvent(MachineConfigMuxEvent.eventReplaced(type, input,
					NullMuxInput.INSTANCE, i));
			}
		}
	}

	private void replaceAllMuxInputsOfRegister(String oldRegisterName,
			String newRegisterName, MuxType type, List<MuxInput> list)
	{
		for (ListIterator<MuxInput> muxIterator = list.listIterator(); muxIterator.hasNext();)
		{
			int i = muxIterator.nextIndex();
			MuxInput input = muxIterator.next();

			if (input instanceof RegisterMuxInput
				&& ((RegisterMuxInput) input).getRegisterName().equals(oldRegisterName))
			{
				RegisterMuxInput newInput = new RegisterMuxInput(newRegisterName);
				muxIterator.set(newInput);
				postEvent(MachineConfigMuxEvent.eventReplaced(type, input, newInput, i));
			}
		}
	}

	public void setRegisterExtension(int index, RegisterExtension register)
	{
		if (!register.isExtended())
			throw new IllegalArgumentException("Can only add extended registers");

		RegisterExtension oldRegister = _extendedRegisters.get(index);

		if (!oldRegister.equals(register) && _extendedRegisters.contains(register))
			throw new IllegalStateException("Already contains " + register);

		// Fetch the indices of the mux inputs that currently use this register
		Map<MuxType, List<Integer>> indicesInUse = new EnumMap<MuxType, List<Integer>>(
			MuxType.class);
		for (MuxType type : MuxType.values())
		{
			indicesInUse.put(
				type,
				fetchRegisterInputIndices(oldRegister.getName(),
					getMuxSourcesInternal(type)));
		}

		// Replace the mux inputs that are currently using the register with null
		for (MuxType type : MuxType.values())
		{
			List<MuxInput> inputs = getMuxSourcesInternal(type);
			for (int inputIndex : indicesInUse.get(type))
			{
				MuxInput oldInput = inputs.get(inputIndex);
				inputs.set(inputIndex, NullMuxInput.INSTANCE);
				postEvent(MachineConfigMuxEvent.eventReplaced(type, oldInput,
					NullMuxInput.INSTANCE, inputIndex));
			}
		}

		// Now, since no mux input is pointing to the register, actually replace the register.
		_extendedRegisters.set(index, register);

		// Notify clients that the register was replaced
		postEvent(MachineConfigRegisterEvent.eventReplaced(oldRegister, register, index));

		// Replace the available mux input for this register since the register changed
		replaceAllMuxInputsOfRegister(oldRegister.getName(), register.getName(), null,
			_availableMuxSources);

		// Put the new mux input at the places set to null before
		RegisterMuxInput newInput = new RegisterMuxInput(register.getName());
		for (MuxType type : MuxType.values())
		{
			List<MuxInput> inputs = getMuxSourcesInternal(type);
			for (Integer inputIndex : indicesInUse.get(type))
			{
				MuxInput oldInput = inputs.get(inputIndex);
				inputs.set(inputIndex.intValue(), newInput);
				postEvent(MachineConfigMuxEvent.eventReplaced(type, oldInput, newInput,
					inputIndex));
			}
		}
	}

	private List<Integer> fetchRegisterInputIndices(String registerName,
			List<MuxInput> list)
	{
		List<Integer> result = new ArrayList<Integer>();
		ListIterator<MuxInput> iter = list.listIterator();
		while (iter.hasNext())
		{
			int idx = iter.nextIndex();
			MuxInput mux = iter.next();
			if (mux instanceof RegisterMuxInput)
			{
				if (((RegisterMuxInput) mux).getRegisterName().equals(registerName))
				{
					result.add(idx);
				}
			}
		}
		return result;
	}

	public void exchangeRegisterExtensions(int index1, int index2)
	{
		if (index1 == index2)
			return;

		RegisterExtension reg1 = _extendedRegisters.get(index1);
		RegisterExtension reg2 = _extendedRegisters.get(index2);
		_extendedRegisters.set(index2, reg1);
		_extendedRegisters.set(index1, reg2);

		postEvent(MachineConfigRegisterEvent.eventExchanged(reg1, reg2, index1, index2));

		int muxIdx1 = -1;
		int muxIdx2 = -1;
		MuxInput muxInput1 = null;
		MuxInput muxInput2 = null;
		for (MuxInput mux : _availableMuxSources)
		{
			if (mux instanceof RegisterMuxInput)
			{
				String registerName = ((RegisterMuxInput) mux).getRegisterName();
				if (registerName.equals(reg1.getName()))
				{
					muxIdx1 = _availableMuxSources.indexOf(mux);
					muxInput1 = mux;
				}
				if (registerName.equals(reg2.getName()))
				{
					muxIdx2 = _availableMuxSources.indexOf(mux);
					muxInput2 = mux;
				}
			}
		}

		_availableMuxSources.set(muxIdx1, muxInput2);
		_availableMuxSources.set(muxIdx2, muxInput1);
		postEvent(MachineConfigMuxEvent.eventExchanged(null, muxInput1, muxInput2,
			muxIdx1, muxIdx2));
	}

	public RegisterExtension getRegisterExtension(int index)
	{
		return _extendedRegisters.get(index);
	}

	public RegisterExtension getBaseRegister(int index)
	{
		return _baseRegisters.get(index);
	}

	public RegisterExtension findRegisterExtension(String name)
	{
		for (RegisterExtension reg : _extendedRegisters)
			if (reg.getName().equalsIgnoreCase(name))
				return reg;
		return null;
	}

	public RegisterExtension findBaseRegister(String name)
	{
		for (RegisterExtension reg : _baseRegisters)
			if (reg.getName().equalsIgnoreCase(name))
				return reg;
		return null;
	}

	public List<RegisterExtension> getRegisterExtensions()
	{
		return _registersView;
	}

	public List<RegisterExtension> getBaseRegisters()
	{
		return _baseRegisters;
	}

	public List<MuxInput> getAvailableSources()
	{
		return _availableMuxSourcesView;
	}

	public List<MuxInput> getMuxSources(MuxType mux)
	{
		switch (mux)
		{
			case A:
				return _muxSourcesAView;
			case B:
				return _muxSourcesBView;
		}
		throw new IllegalArgumentException(mux.toString());
	}

	private List<MuxInput> getMuxSourcesInternal(MuxType mux)
	{
		switch (mux)
		{
			case A:
				return _muxSourcesA;
			case B:
				return _muxSourcesB;
		}
		throw new IllegalArgumentException(mux.toString());
	}

	public void addMuxSource(MuxType mux, MuxInput source)
	{
		List<MuxInput> muxList = getMuxSourcesInternal(mux);

		// It is allowed to have duplicate inputs at the same multiplexer
		// if (muxList.contains(source))
		// throw new IllegalStateException("Mux " + mux + " already contains " + source);

		muxList.add(source);
		postEvent(MachineConfigMuxEvent.eventAdded(mux, source, muxList.size() - 1));
	}

	public void removeMuxSource(MuxType mux, MuxInput source)
	{
		List<MuxInput> muxList = getMuxSourcesInternal(mux);
		int index;
		do
		{
			index = muxList.lastIndexOf(source);
			muxList.remove(index);
			postEvent(MachineConfigMuxEvent.eventRemoved(mux, source, index));
		}
		while (index != -1);
	}

	public void removeMuxSource(MuxType mux, int index)
	{
		List<MuxInput> muxList = getMuxSourcesInternal(mux);
		MuxInput source = muxList.remove(index);
		postEvent(MachineConfigMuxEvent.eventRemoved(mux, source, index));
	}

	public void setMuxSource(MuxType mux, int index, MuxInput source)
	{
		List<MuxInput> muxList = getMuxSourcesInternal(mux);
		MuxInput oldSource = muxList.get(index);
		muxList.set(index, source);
		postEvent(MachineConfigMuxEvent.eventReplaced(mux, oldSource, source, index));
	}

	public void exchangeMuxSources(MuxType mux, int index1, int index2)
	{
		if (index1 == index2)
			return;

		List<MuxInput> list = getMuxSourcesInternal(mux);
		MuxInput input1 = list.get(index1);
		MuxInput input2 = list.get(index2);
		list.set(index2, input1);
		list.set(index1, input2);

		postEvent(MachineConfigMuxEvent.eventExchanged(mux, input1, input2, index1,
			index2));
	}

	public void addMachineConfigListener(MachineConfigListener listener)
	{
		if (!_listeners.contains(listener))
			_listeners.add(listener);
	}

	public void removeMachineConfigListener(MachineConfigListener listener)
	{
		_listeners.remove(listener);
	}

	protected void postEvent(MachineConfigEvent e)
	{
		for (MachineConfigListener l : _listeners)
			l.processEvent(e);
	}

	@Override
	public String toString()
	{
		return "MachineConfiguratien [alu=" + _alu + ", registers=" + _extendedRegisters
			+ ", mux.A=" + _muxSourcesA + ", mux.B=" + _muxSourcesB + "]";
	}
}