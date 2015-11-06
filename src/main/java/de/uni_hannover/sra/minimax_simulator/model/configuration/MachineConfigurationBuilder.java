package de.uni_hannover.sra.minimax_simulator.model.configuration;

import com.google.common.collect.ImmutableList;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;

import java.util.List;

/**
 * The {@code MachineConfigurationBuilder} is used to create an instance of the {@link MachineConfiguration}.
 *
 * @author Philipp Rohde
 */
public interface MachineConfigurationBuilder {

    /**
     * Adds the specified {@link AluOperation}s to the machine.
     *
     * @param aluOperations
     *          a list of the {@code AluOperation}s to add
     */
    public void addAluOperations(List<AluOperation> aluOperations);

    /**
     * Adds the specified extended registers ({@link RegisterExtension}) to the machine.
     *
     * @param registerExtensions
     *          a list of the extended {@code RegisterExtension}s to add
     */
    public void addRegisterExtensions(List<RegisterExtension> registerExtensions);

    /**
     * Gets a list of all {@link MuxInput}s.
     *
     * @return
     *          a list of all {@code MuxInput}s
     */
    public ImmutableList<MuxInput> getMuxInputs();

    /**
     * Gets a list of all allowed {@link MuxInput}s.
     *
     * @return
     *          a list of all allowed {@code MuxInput}s
     */
    public ImmutableList<MuxInput> getAllowedMuxInputs();

    /**
     * Gets a list of all {@link MuxInput}s selected by the specified
     * multiplexer ({@link MuxType}).
     *
     * @param mux
     *          the {@code MuxType} to get all {@code MuxInput}s for
     * @return
     *          a list of all {@code MuxInputs} selected by the {@code MuxType}
     */
    public ImmutableList<MuxInput> getSelectedMuxInputs(MuxType mux);

    /**
     * Adds the specified {@link MuxInput}s to the specified multiplexer.
     *
     * @param mux
     *          the multiplexer to add the {@code MuxInput}s to
     * @param muxInputs
     *          a list of the {@code MuxInput}s to add
     */
    public void addMuxInputs(MuxType mux, List<MuxInput> muxInputs);

    /**
     * Adds the default base registers ({@link RegisterExtension}) to the machine.
     *
     * @param textResource
     *          the {@link TextResource} used for getting localized texts
     */
    public void addDefaultBaseRegisters(TextResource textResource);

    /**
     * Loads the default values for {@link AluOperation}, extended and base registers ({@link RegisterExtension}) and
     * {@link MuxInput}s. This creates the basic Minimax machine.
     *
     * @param registerDescriptionResource
     *          the {@link TextResource} used for getting localized texts
     * @return
     *          the {@code MachineConfigurationBuilder} instance with the default values
     */
    public MachineConfigurationBuilder loadDefaultValues(TextResource registerDescriptionResource);

    /**
     * Builds a {@link MachineConfiguration} with the values already set to the instance of the
     * {@code MachineConfigurationBuilder} after setting all registers ({@link RegisterExtension})
     * as {@link MuxInput}s.
     *
     * @return
     *          the built {@code MachineConfiguration}
     */
    public MachineConfiguration build();
}
