package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.RegisterMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.DefaultRegisterGroup;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.ExtendedRegisterGroup;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.Group;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.MdrRegisterGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the registers of a {@link MinimaxMachine}.
 *
 * @author Martin L&uuml;ck
 */
class RegisterManager {

    /**
     * The type of the register.
     */
    enum RegisterType {
        MDR,
        BASE,
        EXTENDED
    }

    private final GroupManager groupManager;
    private final Map<String, String> registerIds;

    /**
     * Constructs a new {@code RegisterManager} using the specified {@link GroupManager}.
     *
     * @param groupManager
     *          the group manager
     */
    public RegisterManager(GroupManager groupManager) {
        this.groupManager = groupManager;
        registerIds = new HashMap<>();
    }

    /**
     * Adds a register with the specified {@link RegisterType} and name.
     *
     * @param type
     *          the type of the register
     * @param registerName
     *          the name of the register
     * @return
     *          the ID of the register
     */
    public String addRegister(RegisterType type, String registerName) {
        String registerId;

        switch (type) {
            case BASE:
            case MDR:
                registerId = registerName;
                break;
            default:
                registerId = buildRegisterId(registerName);
                break;
        }

        registerIds.put(registerName, registerId);

        Group group;
        switch (type) {
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

        // use register id as name for the group
        groupManager.initializeGroup(registerId, group);

        return registerId;
    }

    /**
     * Removes the register with the specified name.
     *
     * @param registerName
     *          the name of the register to remove
     * @return
     *          the ID of the removed register
     */
    public String removeRegister(String registerName) {
        String registerId = registerIds.remove(registerName);
        if (registerId == null) {
            throw new IllegalStateException("Unknown register name: " + registerName);
        }

        groupManager.removeGroup(registerId);

        return registerId;
    }

    /**
     * Gets the register ID of the specified {@link RegisterMuxInput}.
     *
     * @param input
     *          the {@code RegisterMuxInput}
     * @return
     *          the ID of the register related to the {@code RegisterMuxInput}
     */
    String getRegisterId(RegisterMuxInput input) {
        return registerIds.get(input.getRegisterName());
    }

    /**
     * Gets the register ID of the register with the specified name.
     *
     * @param name
     *          the name of the register
     * @return
     *          the ID of the register
     */
    String getRegisterId(String name) {
        return registerIds.get(name);
    }

    /**
     * Gets the names and IDs of the registers.
     *
     * @return
     *          a map containing the names and IDs of the registers
     */
    Map<String, String> getRegisterIdsByName() {
        return registerIds;
    }

    /**
     * Builds a register ID for the specified register name.
     *
     * @param registerName
     *          the name of the register
     * @return
     *          the built ID of the register
     */
    private String buildRegisterId(String registerName) {
        return Parts.REGISTER_ + "<" + registerName + ">";
    }
}