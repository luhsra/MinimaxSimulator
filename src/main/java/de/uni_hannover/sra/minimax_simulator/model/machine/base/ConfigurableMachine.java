package de.uni_hannover.sra.minimax_simulator.model.machine.base;

import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;

/**
 * A {@code ConfigurableMachine} is a {@link Machine} which is configurable.
 *
 * @author Martin L&uuml;ck
 */
public interface ConfigurableMachine extends Machine {

	/**
	 * Gets all {@link AluOperation}s of the machine.
	 *
	 * @return
	 *          an {@link ExtensionList} of the machine's {@code AluOperation}s
	 */
	public ExtensionList<AluOperation> getAluOperations();

	/**
	 * Gets all registers ({@link RegisterExtension}) of the machine.
	 *
	 * @return
	 *          an {@link ExtensionList} of the machine's {@code RegisterExtension}s
	 */
	public ExtensionList<RegisterExtension> getRegisterExtensions();

	/**
	 * Gets all {@link MuxInput}s of the specified multiplexer.
	 *
	 * @param type
	 *          the multiplexer whose inputs will be returned
	 * @return
	 *          an {@link ExtensionList} of the multiplexer's {@code MuxInput}s
	 */
	public ExtensionList<MuxInput> getMuxInputExtensions(MuxType type);
}