package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.RegisterMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.DefaultRegisterGroup;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.ExtendedRegisterGroup;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.Group;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.MdrRegisterGroup;

import java.util.HashMap;
import java.util.Map;

class RegisterManager
{
	enum RegisterType
	{
		MDR,
		BASE,
		EXTENDED;
	}

	private final GroupManager						_groupManager;
	private final Map<String, String>				_registerIds;

	public RegisterManager(GroupManager groupManager)
	{
		_groupManager = groupManager;
		_registerIds = new HashMap<String, String>();
	}

	public String addRegister(RegisterType type, String registerName)
	{
		String registerId;

		switch (type)
		{
			case BASE:
			case MDR:
				registerId = registerName;
				break;
			default:
				registerId = buildRegisterId(registerName);
				break;
		}

		_registerIds.put(registerName, registerId);

		Group group;
		switch (type)
		{
			case BASE:
				group = new DefaultRegisterGroup(registerId);
				break;
			case MDR:
				group = new MdrRegisterGroup(registerId);
				break;
			case EXTENDED:
				group = new ExtendedRegisterGroup(registerId, registerName);
				break;
			default:
				throw new AssertionError();
		}

		// Use register id as name for the group
		_groupManager.initializeGroup(registerId, group);

		return registerId;
	}

	public String removeRegister(String registerName)
	{
		String registerId = _registerIds.remove(registerName);
		if (registerId == null)
			throw new IllegalStateException("Unknown register name: " + registerName);

		_groupManager.removeGroup(registerId);

		return registerId;
	}

	String getRegisterId(RegisterMuxInput input)
	{
		return _registerIds.get(input.getRegisterName());
	}

	String getRegisterId(String name)
	{
		return _registerIds.get(name);
	}

	Map<String, String> getRegisterIdsByName()
	{
		return _registerIds;
	}

	private String buildRegisterId(String registerName)
	{
		return Parts.REGISTER_ + "<" + registerName + ">";
	}
}