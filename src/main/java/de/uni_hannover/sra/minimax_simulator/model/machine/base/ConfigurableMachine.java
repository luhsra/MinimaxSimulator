package de.uni_hannover.sra.minimax_simulator.model.machine.base;

import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;

public interface ConfigurableMachine extends Machine
{
	public ExtensionList<AluOperation> getAluOperations();

	public ExtensionList<RegisterExtension> getRegisterExtensions();

	public ExtensionList<MuxInput> getMuxInputExtensions(MuxType type);
}