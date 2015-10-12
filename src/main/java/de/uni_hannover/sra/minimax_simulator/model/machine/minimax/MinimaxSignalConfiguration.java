package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;


import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.MachineConfigAluEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.MachineConfigMuxEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.MachineConfigRegisterEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListener;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.signal.*;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MinimaxSignalConfiguration implements SignalConfiguration,
		MachineConfigListener
{
	private static class WriteEnableSignalType extends BinarySignalType
	{
		public WriteEnableSignalType(String id, String name)
		{
			super(id, name, false);
		}

		@Override
		public String getSignalName(int index)
		{
			return index == 1 ? "write" : "";
		}
	}

	private class AluCtrlSignalType extends DefaultSignalType
	{
		public AluCtrlSignalType(String name)
		{
			super(BaseControlPort.ALU_CTRL.name(), name, _config.getAluOperations().size(), true);
		}

		@Override
		public String getSignalName(int index)
		{
			return _config.getAluOperation(index).getOperationName();
		}
	}

	private static class MuxInputSignalType extends DefaultSignalType
	{
		private final List<MuxInput> _inputs;

		public MuxInputSignalType(String id, String name, List<MuxInput> inputs)
		{
			super(id, name, inputs.size(), true);
			_inputs = inputs;
		}

		@Override
		public String getSignalName(int index)
		{
			return _inputs.get(index).getName();
		}
	}

	private final List<SignalType>				_signalTypes;
	private final List<SignalConfigListener>	_listeners;

	private final MachineConfiguration			_config;

	public MinimaxSignalConfiguration(MachineConfiguration config) {
		_signalTypes = new ArrayList<SignalType>();
		_listeners = new ArrayList<SignalConfigListener>();

		_config = config;
		_config.addMachineConfigListener(this);
		updateSignals();
	}

	@Override
	public List<SignalType> getSignalTypes()
	{
		return Collections.unmodifiableList(_signalTypes);
	}

	@Override
	public void addSignalType(int index, SignalType signal)
	{
		_signalTypes.add(index, signal);
	}

	@Override
	public void removeSignalType(int index)
	{
		_signalTypes.remove(index);
	}

	@Override
	public void exchangeSignalsType(int index1, int index2)
	{
		Collections.swap(_signalTypes, index1, index2);
	}

	@Override
	public void replaceSignalType(int index, SignalType signal)
	{
		_signalTypes.set(index, signal);
	}

	private void updateSignals()
	{
		List<SignalType> l = _signalTypes;
		l.clear();

		SignalType signal;

		TextResource res = Main.getTextResource("signal");

		// ALU select signals
		signal = new MuxInputSignalType(BaseControlPort.ALU_SELECT_A.name(), res.get("col.aluselA"),
			_config.getMuxSources(MuxType.A));
		l.add(signal);

		signal = new MuxInputSignalType(BaseControlPort.ALU_SELECT_B.name(), res.get("col.aluselB"),
			_config.getMuxSources(MuxType.B));
		l.add(signal);

		// Memory signals
		signal = new BinarySignalType(BaseControlPort.MDR_SEL.name(), res.get("col.mdrsel"), true)
		{
			@Override
			public String getSignalName(int index)
			{
				return index == 1 ? "MEM.DO" : "ALU.result";
			}
		};
		l.add(signal);

		signal = new BinarySignalType(BaseControlPort.MEM_CS.name(), res.get("col.memcs"), false)
		{
			@Override
			public String getSignalName(int index)
			{
				return index == 1 ? "enable" : "";
			}
		};
		l.add(signal);

		signal = new BinarySignalType(BaseControlPort.MEM_RW.name(), res.get("col.memrw"), true)
		{
			@Override
			public String getSignalName(int index)
			{
				return index == 1 ? "read" : "write";
			}
		};
		l.add(signal);

		// ALU ctrl
		signal = new AluCtrlSignalType(res.get("col.aluctrl"));
		l.add(signal);

		for (RegisterExtension register : _config.getBaseRegisters())
		{
			String nameW = register.getName() + ".W";
			l.add(new WriteEnableSignalType(nameW, nameW));
		}
		for (RegisterExtension register : _config.getRegisterExtensions())
		{
			String nameW = register.getName() + ".W";
			l.add(new WriteEnableSignalType(nameW, nameW));
		}
	}

	@Override
	public void addSignalConfigListener(SignalConfigListener l)
	{
		if (!_listeners.contains(l))
			_listeners.add(l);
	}

	@Override
	public void removeSignalConfigListener(SignalConfigListener l)
	{
		_listeners.remove(l);
	}

	@Override
	public void processEvent(MachineConfigEvent event)
	{
		if (event instanceof MachineConfigRegisterEvent)
		{
			// Columns changed
			updateSignals();
			fireStructureChanged();
		}
		else if (event instanceof MachineConfigAluEvent)
		{
			// Different bit width for alu ctrl signal
			updateSignals();
			fireStructureChanged();
		}
		else if (event instanceof MachineConfigMuxEvent)
		{
			// Different bit width for alu select signals
			updateSignals();
			fireStructureChanged();
		}
	}

	private void fireStructureChanged()
	{
		for (SignalConfigListener l : _listeners)
			l.signalStructureChanged();
	}
}