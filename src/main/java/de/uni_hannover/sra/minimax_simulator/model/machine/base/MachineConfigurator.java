package de.uni_hannover.sra.minimax_simulator.model.machine.base;

import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.MachineConfigAluEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.MachineConfigMuxEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.MachineConfigRegisterEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListener;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;

/**
 * The {@code MachineConfigurator} configures a {@link ConfigurableMachine} therefore it implements {@link MachineConfigListener}
 * to process the changes made to the {@link MachineConfiguration} of the {@code ConfigurableMachine}.
 *
 * @author Martin L&uuml;ck
 */
public class MachineConfigurator implements MachineConfigListener {

	private final ConfigurableMachine _machine;

	/**
	 * Constructs a new {@code MachineConfigurator} with the specified {@link ConfigurableMachine}
	 * and {@link MachineConfiguration}.
	 *
	 * @param machine
	 *          the {@code ConfigurableMachine} the {@code MachineConfigurator} configures
	 * @param config
	 *          the {@code MachineConfiguration} of the {@code ConfigurableMachine}
	 */
	public MachineConfigurator(ConfigurableMachine machine, MachineConfiguration config) {
		_machine = machine;

		// Fetch all extensions which are already added.

		_machine.getAluOperations().addAll(config.getAluOperations());
		_machine.getRegisterExtensions().addAll(config.getRegisterExtensions());
		for (MuxType mux : MuxType.values()) {
			_machine.getMuxInputExtensions(mux).addAll(config.getMuxSources(mux));
		}
	}

	@Override
	public void processEvent(MachineConfigEvent event) {
		if (event instanceof MachineConfigAluEvent) {
			MachineConfigAluEvent a = (MachineConfigAluEvent) event;
			switch (a.type) {
				case ELEMENT_ADDED:
					_machine.getAluOperations().add(a.element);
					break;
				case ELEMENT_REMOVED:
					_machine.getAluOperations().remove(a.index);
					break;
				case ELEMENTS_EXCHANGED:
					_machine.getAluOperations().swap(a.index, a.index2);
					break;
				case ELEMENT_REPLACED:
					// cannot happen with ALU operations
					throw new AssertionError();
			}
		}
		else if (event instanceof MachineConfigRegisterEvent) {
			MachineConfigRegisterEvent r = (MachineConfigRegisterEvent) event;
			switch (r.type) {
				case ELEMENT_ADDED:
					_machine.getRegisterExtensions().add(r.element);
					break;
				case ELEMENT_REMOVED:
					_machine.getRegisterExtensions().remove(r.index);
					break;
				case ELEMENT_REPLACED:
					_machine.getRegisterExtensions().set(r.index, r.element);
					break;
				case ELEMENTS_EXCHANGED:
					_machine.getRegisterExtensions().swap(r.index, r.index2);
					break;
			}
		}
		else if (event instanceof MachineConfigMuxEvent) {
			MachineConfigMuxEvent m = (MachineConfigMuxEvent) event;

			// We are only interested in the mux inputs which are added
			// to an multiplexer, not in the general available list.
			if (m.mux == null) {
				return;
			}

			ExtensionList<MuxInput> list = _machine.getMuxInputExtensions(m.mux);

			switch (m.type) {
				case ELEMENT_ADDED:
					list.add(m.element);
					break;
				case ELEMENT_REMOVED:
					list.remove(m.index);
					break;
				case ELEMENT_REPLACED:
					list.set(m.index, m.element);
					break;
				case ELEMENTS_EXCHANGED:
					list.swap(m.index, m.index2);
					break;
			}
		}
	}
}