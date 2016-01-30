package de.uni_hannover.sra.minimax_simulator.model.configuration;

import com.google.common.collect.ImmutableList;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.ConstantMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.RegisterMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterSize;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;

import java.util.*;

/**
 * The {@code MinimaxConfigurationBuilder} is used to create an instance of the {@link MachineConfiguration}
 * of a {@link de.uni_hannover.sra.minimax_simulator.model.machine.minimax.MinimaxMachine}.
 *
 * @author Martin L&uuml;ck
 */
public class MinimaxConfigurationBuilder implements MachineConfigurationBuilder {

    private final List<AluOperation> aluOperations;
    private final List<RegisterExtension> baseRegisters;
    private final List<RegisterExtension> registerExtensions;
    private final List<MuxInput> allowedMuxInputs;
    private final Map<MuxType, List<MuxInput>> selectedMuxInputs;

    /**
     * Creates a new instance of the {@code MinimaxConfigurationBuilder}
     * with empty lists and gets all {@link MuxType}s via {@link MuxType#values()}.
     */
    public MinimaxConfigurationBuilder() {
        aluOperations = new ArrayList<>();
        baseRegisters = new ArrayList<>();
        registerExtensions = new ArrayList<>();
        allowedMuxInputs = new ArrayList<>();
        selectedMuxInputs = new EnumMap<>(MuxType.class);

        for (MuxType m : MuxType.values()) {
            selectedMuxInputs.put(m, new ArrayList<>());
        }
    }

    @Override
    public void addAluOperations(List<AluOperation> aluOperations) {
        this.aluOperations.addAll(aluOperations);
    }

    @Override
    public void addRegisterExtensions(List<RegisterExtension> registerExtensions) {
        this.registerExtensions.addAll(registerExtensions);
    }

    @Override
    public ImmutableList<MuxInput> getMuxInputs() {
        return ImmutableList.copyOf(allowedMuxInputs);
    }

    @Override
    public ImmutableList<MuxInput> getAllowedMuxInputs() {
        return ImmutableList.copyOf(allowedMuxInputs);
    }

    @Override
    public ImmutableList<MuxInput> getSelectedMuxInputs(MuxType mux) {
        return ImmutableList.copyOf(selectedMuxInputs.get(mux));
    }

    @Override
    public void addMuxInputs(MuxType mux, List<MuxInput> muxInputs) {
        selectedMuxInputs.put(mux, muxInputs);
    }

    /**
     * Adds the default {@link AluOperation}s to the machine.<br>
     * <br>
     * The default operations are:<br>
     * <ul>
     *     <li>A ADD B</li>
     *     <li>B SUB A</li>
     *     <li>TRANS.A</li>
     *     <li>TRANS.B</li>
     * </ul>
     */
    private void addDefaultAluOperations() {
        aluOperations.add(AluOperation.A_ADD_B);
        aluOperations.add(AluOperation.B_SUB_A);
        aluOperations.add(AluOperation.TRANS_A);
        aluOperations.add(AluOperation.TRANS_B);
    }

    /**
     * Adds the default extended registers ({@link RegisterExtension}) to the machine.
     *
     */
    private void addDefaultRegisterExtensions() {
        // actually the base machine has no default extended registers
    }

    /**
     * Adds all registers ({@link RegisterExtension}) to the list of allowed {@link MuxInput}s.
     */
    private void addRegistersAsMuxSources() {
        for (RegisterExtension reg : registerExtensions) {
            if (reg.getSize() == RegisterSize.BITS_24) {
                continue;
            }

            allowedMuxInputs.add(new RegisterMuxInput(reg.getName()));
        }
        for (RegisterExtension reg : baseRegisters) {
            if (reg.getSize() == RegisterSize.BITS_24) {
                continue;
            }

            String name = reg.getName();
            if ("IR".equals(name)) {
                name = "AT";
            }
            allowedMuxInputs.add(new RegisterMuxInput(reg.getName(), name));
        }

        ListIterator<MuxInput> inputIter;
        for (inputIter = selectedMuxInputs.get(MuxType.A).listIterator(); inputIter.hasNext(); ) {
            if ("IR".equals(inputIter.next().getName())) {
                inputIter.set(new RegisterMuxInput("IR", "AT"));
            }
        }
        for (inputIter = selectedMuxInputs.get(MuxType.B).listIterator(); inputIter.hasNext(); ) {
            if ("IR".equals(inputIter.next().getName())) {
                inputIter.set(new RegisterMuxInput("IR", "AT"));
            }
        }
    }

    /**
     * Adds the default selected {@link MuxInput}s for the multiplexer ({@link MuxType}).<br>
     * <br>
     * The default {@code MuxInput}s are:<br>
     * <table summary="default selected mux sources" style="text-align: center; border-spacing: 10px">
     *  <tr>
     *      <th>MuxType.A</th>
     *      <th>MuxType.B</th>
     *  </tr>
     *  <tr>
     *      <td>0</td>
     *      <td>MDR</td>
     *  </tr>
     *  <tr>
     *      <td>1</td>
     *      <td>PC</td>
     *  </tr>
     *  <tr>
     *      <td>ACCU</td>
     *      <td>AT</td>
     *  </tr>
     *  <tr>
     *      <td>&nbsp;</td>
     *      <td>ACCU</td>
     *  </tr>
     * </table>
     */
    private void addDefaultSelectedMuxSources() {
        List<MuxInput> listA = selectedMuxInputs.get(MuxType.A);
        listA.add(new ConstantMuxInput(0));
        listA.add(new ConstantMuxInput(1));
        listA.add(new RegisterMuxInput("ACCU"));

        List<MuxInput> listB = selectedMuxInputs.get(MuxType.B);
        listB.add(new RegisterMuxInput("MDR"));
        listB.add(new RegisterMuxInput("PC"));
        listB.add(new RegisterMuxInput("IR", "AT"));
        listB.add(new RegisterMuxInput("ACCU"));
    }

    /**
     * Adds the default base registers ({@link RegisterExtension}) to the machine.<br>
     * <br>
     * The default base {@code RegisterExtension}s are:<br>
     * <ul>
     *     <li>PC</li>
     *     <li>IR</li>
     *     <li>MDR</li>
     *     <li>MAR</li>
     *     <li>ACCU</li>
     * </ul>
     *
     * @param textResource
     *          the {@link TextResource} used for getting localized texts
     */
    @Override
    public void addDefaultBaseRegisters(TextResource textResource) {
        baseRegisters.add(new RegisterExtension("PC", RegisterSize.BITS_32, textResource.get("register.pc.description"), false));
        baseRegisters.add(new RegisterExtension("IR", RegisterSize.BITS_32, textResource.get("register.ir.description"), false));
        baseRegisters.add(new RegisterExtension("MDR", RegisterSize.BITS_32, textResource.get("register.mdr.description"), false));
        baseRegisters.add(new RegisterExtension("MAR", RegisterSize.BITS_24, textResource.get("register.mar.description"), false));
        baseRegisters.add(new RegisterExtension("ACCU", RegisterSize.BITS_32, textResource.get("register.accu.description"), false));
    }

    @Override
    public MachineConfigurationBuilder loadDefaultValues(TextResource registerDescriptionResource) {
        aluOperations.clear();
        addDefaultAluOperations();

        baseRegisters.clear();
        addDefaultBaseRegisters(registerDescriptionResource);

        registerExtensions.clear();
        addDefaultRegisterExtensions();

        selectedMuxInputs.values().forEach(List<MuxInput>::clear);
        addDefaultSelectedMuxSources();

        return this;
    }

    @Override
    public MachineConfiguration build() {
        addRegistersAsMuxSources();
        return new MachineConfiguration(aluOperations, baseRegisters, registerExtensions, allowedMuxInputs, selectedMuxInputs);
    }
}