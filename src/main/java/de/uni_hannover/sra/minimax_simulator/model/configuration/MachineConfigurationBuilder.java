package de.uni_hannover.sra.minimax_simulator.model.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.ConstantMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.RegisterMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterSize;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;

// TODO: should be an interface rather than know details of Minimax internals
public class MachineConfigurationBuilder
{
	private final List<AluOperation>			_aluOperations;
	private final List<RegisterExtension>		_baseRegisters;
	private final List<RegisterExtension>		_registerExtensions;
	private final List<MuxInput>				_allowedMuxInputs;
	private final Map<MuxType, List<MuxInput>>	_selectedMuxInputs;

	public MachineConfigurationBuilder()
	{
		_aluOperations = new ArrayList<AluOperation>();
		_baseRegisters = new ArrayList<RegisterExtension>();
		_registerExtensions = new ArrayList<RegisterExtension>();
		_allowedMuxInputs = new ArrayList<MuxInput>();
		_selectedMuxInputs = new HashMap<MuxType, List<MuxInput>>();

		for (MuxType m : MuxType.values())
			_selectedMuxInputs.put(m, new ArrayList<MuxInput>());
	}

	public List<AluOperation> getAluOperations()
	{
		return _aluOperations;
	}

	public List<RegisterExtension> getRegisterExtensions()
	{
		return _registerExtensions;
	}

	public List<RegisterExtension> getBaseRegisters()
	{
		return _baseRegisters;
	}

	public List<MuxInput> getMuxInputs()
	{
		return _allowedMuxInputs;
	}

	public List<MuxInput> getAllowedMuxInputs()
	{
		return _allowedMuxInputs;
	}

	public List<MuxInput> getSelectedMuxInputs(MuxType mux)
	{
		return _selectedMuxInputs.get(mux);
	}

	private void addDefaultAluOperations()
	{
		_aluOperations.add(AluOperation.A_ADD_B);
		_aluOperations.add(AluOperation.B_SUB_A);
	}

	private void addDefaultRegisterExtensions(TextResource textResource)
	{
		_registerExtensions.add(new RegisterExtension("ACCU", RegisterSize.BITS_32,
			textResource.get("register.accu.description"), true));
	}

	private void addRegistersAsMuxSources()
	{
		for (RegisterExtension reg : _registerExtensions)
		{
			if (reg.getSize() == RegisterSize.BITS_24)
				continue;

			_allowedMuxInputs.add(new RegisterMuxInput(reg.getName()));
		}
		for (RegisterExtension reg : _baseRegisters)
		{
			if (reg.getSize() == RegisterSize.BITS_24)
				continue;

			String name = reg.getName();
			if (name.equals("IR"))
				name = "AT";
			_allowedMuxInputs.add(new RegisterMuxInput(reg.getName(), name));
		}

		ListIterator<MuxInput> inputIter;
		for (inputIter = _selectedMuxInputs.get(MuxType.A).listIterator(); inputIter.hasNext();)
		{
			if (inputIter.next().getName().equals("IR"))
			{
				inputIter.set(new RegisterMuxInput("IR", "AT"));
			}
		}
		for (inputIter = _selectedMuxInputs.get(MuxType.B).listIterator(); inputIter.hasNext();)
		{
			if (inputIter.next().getName().equals("IR"))
			{
				inputIter.set(new RegisterMuxInput("IR", "AT"));
			}
		}
	}

	private void addDefaultSelectedMuxSources()
	{
		List<MuxInput> listA = _selectedMuxInputs.get(MuxType.A);
		listA.add(new ConstantMuxInput(0));
		listA.add(new ConstantMuxInput(1));
		listA.add(new RegisterMuxInput("ACCU"));

		List<MuxInput> listB = _selectedMuxInputs.get(MuxType.B);
		listB.add(new RegisterMuxInput("MDR"));
		listB.add(new RegisterMuxInput("IR", "AT"));
		listB.add(new RegisterMuxInput("PC"));
		listB.add(new RegisterMuxInput("ACCU"));
	}

	public void addDefaultBaseRegisters(TextResource textResource)
	{
		_baseRegisters.add(new RegisterExtension("PC", RegisterSize.BITS_32,
			textResource.get("register.pc.description"), false));
		_baseRegisters.add(new RegisterExtension("IR", RegisterSize.BITS_32,
			textResource.get("register.ir.description"), false));
		_baseRegisters.add(new RegisterExtension("MDR", RegisterSize.BITS_32,
			textResource.get("register.mdr.description"), false));
		_baseRegisters.add(new RegisterExtension("MAR", RegisterSize.BITS_24,
			textResource.get("register.mar.description"), false));
	}

	public MachineConfigurationBuilder loadDefaultValues(
			TextResource registerDescriptionResource)
	{
		_aluOperations.clear();
		addDefaultAluOperations();

		_baseRegisters.clear();
		addDefaultBaseRegisters(registerDescriptionResource);

		_registerExtensions.clear();
		addDefaultRegisterExtensions(registerDescriptionResource);

		for (List<MuxInput> list : _selectedMuxInputs.values())
			list.clear();
		addDefaultSelectedMuxSources();

		return this;
	}

	public MachineConfiguration build()
	{
		addRegistersAsMuxSources();

		MachineConfiguration conf = new MachineConfiguration(_aluOperations,
			_baseRegisters, _registerExtensions, _allowedMuxInputs, _selectedMuxInputs);
		return conf;
	}
}