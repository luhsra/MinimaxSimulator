package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands;

import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;

import java.util.ArrayList;
import java.util.List;

/**
 * This {@link Command} is used for deleting a {@link RegisterExtension}.
 *
 * @author Philipp Rohde
 */
public class RegisterRemovedCommand extends Command {

    private final RegisterExtension register;
    private final MachineConfiguration config;
    private final List<RegisterExtension> registers;
    private final List<MuxInput> muxA;
    private final List<MuxInput> muxB;

    /**
     * Creates the {@code RegisterRemovedCommand} instance for the change.
     *
     * @param register
     *         the register to delete
     * @param config
     *         the {@code MachineConfiguration} of the simulated machine
     */
    public RegisterRemovedCommand(RegisterExtension register, MachineConfiguration config) {
        super("register.removed");
        this.register = register;
        this.config = config;

        this.registers = new ArrayList<>();
        for (RegisterExtension reg : config.getRegisterExtensions()) {
            this.registers.add(reg);
        }

        this.muxA = new ArrayList<>();
        for (MuxInput input : config.getMuxSources(MuxType.A)) {
            this.muxA.add(input);
        }

        this.muxB = new ArrayList<>();
        for (MuxInput input : config.getMuxSources(MuxType.B)) {
            this.muxB.add(input);
        }
    }

    @Override
    public void execute() {
        config.removeRegisterExtension(register);
    }

    @Override
    public void undo() {
        for (int i = config.getRegisterExtensions().size() - 1; i >= 0; i--) {
            config.removeRegisterExtension(i);
        }
        for (RegisterExtension reg : registers) {
            config.addRegisterExtension(reg);
        }

        MuxType mux = MuxType.A;
        for (int j = 0; j < config.getMuxSources(mux).size(); j++) {
            config.setMuxSource(mux, j, muxA.get(j));
        }

        mux = MuxType.B;
        for (int k = 0; k < config.getMuxSources(mux).size(); k++) {
            config.setMuxSource(mux, k, muxB.get(k));
        }
    }
}
