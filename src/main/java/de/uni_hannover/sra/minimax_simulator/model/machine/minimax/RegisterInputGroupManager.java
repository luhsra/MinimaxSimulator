package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;


import com.google.common.collect.Lists;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.RegisterMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.MuxInputManager.InputEntry;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.AbstractGroup;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.DefaultLayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.IngoingPin;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Junction;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.OutgoingPin;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Wire;
import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.ConstraintBuilder;

import java.util.*;
import java.util.Map.Entry;

/**
 * {@link MuxInputGroupManager} for {@link RegisterMuxInput}s.
 *
 * @author Martin L&uuml;ck
 */
class RegisterInputGroupManager extends RegisterManager implements MuxInputGroupManager {

    private final MachineTopology circuitRegistry;
    private final GroupManager groupManager;

    private final SortedMap<MuxType, List<InputEntry>> inputEntriesByMux;
    private final Map<String, List<InputEntry>> inputsByRegisterId;

    /**
     * Constructs a new {@code RegisterInputGroupManager} for the specified {@link MinimaxMachine}.
     *
     * @param machine
     *          the Minimax machine
     */
    public RegisterInputGroupManager(MinimaxMachine machine) {
        super(machine.getGroupManager());

        groupManager = machine.getGroupManager();
        circuitRegistry = machine.getTopology();

        inputEntriesByMux = new TreeMap<>();
        for (MuxType type : MuxType.values()) {
            inputEntriesByMux.put(type, new ArrayList<>());
        }

        inputsByRegisterId = new HashMap<>();
    }

    @Override
    public void update(MuxInputManager muxInputs) {
        // replace inputs of given multiplexer
        clearMuxInputs(muxInputs);

        for (Entry<String, List<InputEntry>> registerInputs : inputsByRegisterId.entrySet()) {
            destroyGroups(registerInputs.getKey(), registerInputs.getValue());
        }

        // rebuild input list per register
        inputsByRegisterId.clear();
        for (List<InputEntry> entries : inputEntriesByMux.values()) {
            for (InputEntry entry : entries) {
                if (entry.input instanceof RegisterMuxInput) {
                    RegisterMuxInput reg = (RegisterMuxInput) entry.input;

                    String registerId = getRegisterId(reg);
                    if (registerId == null) {
                        throw new IllegalStateException("No register with name " + reg.getRegisterName());
                    }

                    List<InputEntry> regInputs = inputsByRegisterId.get(registerId);
                    if (regInputs == null) {
                        regInputs = new ArrayList<>();
                        inputsByRegisterId.put(registerId, regInputs);
                    }
                    regInputs.add(entry);
                }
            }
        }

        for (Entry<String, List<InputEntry>> registerInputs : inputsByRegisterId.entrySet()) {
            createGroups(registerInputs.getKey(), registerInputs.getValue());
        }
    }

    /**
     * Clears the {@code MuxInputs} contained by the specified {@code MuxInputManager}.
     *
     * @param muxInputs
     *          the {@code MuxInputManager} holding the {@code MuxInput}s
     */
    private void clearMuxInputs(MuxInputManager muxInputs) {
        List<InputEntry> entries = inputEntriesByMux.get(muxInputs.getMuxType());
        entries.clear();
        entries.addAll(muxInputs.getMuxInputs());
    }

    /**
     * Creates {@link RegisterInputGroup}s for the specified entries.
     *
     *
     * @param registerId
     *          the ID of the register
     * @param entries
     *          the {@link InputEntry} list
     */
    private void createGroups(String registerId, List<InputEntry> entries) {
        String sourceJunctionId = registerId + Parts._OUT_JUNCTION;
        Junction sourceJunction = circuitRegistry.getCircuit(Junction.class, sourceJunctionId);

        for (InputEntry entry : Lists.reverse(entries)) {
            RegisterInputGroup group = new RegisterInputGroup(entry, sourceJunction, sourceJunctionId);
            groupManager.initializeGroup(entry.pinId + Parts._REGISTER, group);

            sourceJunction = group.junction;
            sourceJunctionId = group.sourceJunctionId;
        }
    }

    /**
     * Removes the {@link RegisterInputGroup}s for the specified entries.
     *
     * @param registerId
     *          the ID of the register
     * @param entries
     *          the {@link InputEntry} list
     */
    private void destroyGroups(String registerId, List<InputEntry> entries) {
        for (InputEntry entry : entries) {
            groupManager.removeGroup(entry.pinId + Parts._REGISTER);
        }

        Junction registerJunction = circuitRegistry.getCircuit(Junction.class, registerId + Parts._OUT_JUNCTION);
        registerJunction.getDataOuts().clear();
    }

    /**
     * Groups the parts of a {@link RegisterMuxInput}.
     */
    private class RegisterInputGroup extends AbstractGroup {

        protected IngoingPin   pin;
        protected String       pinId;
        protected Junction     junction;
        protected String       junctionId;
        protected Junction     sourceJunction;
        protected String       sourceJunctionId;

        /**
         * Constructs a new {@code RegisterInputGroup} with the specified entry, source junction and source junction ID.
         *
         * @param entry
         *          the {@link InputEntry}
         * @param sourceJunction
         *          the source {@link Junction}
         * @param sourceJunctionId
         *          the ID of the source {@code Junction}
         */
        public RegisterInputGroup(InputEntry entry, Junction sourceJunction, String sourceJunctionId) {
            pin = entry.pin;
            pinId = entry.pinId;
            junction = new Junction(1);
            junctionId = pinId + Parts._JUNCTION;
            this.sourceJunction = sourceJunction;
            this.sourceJunctionId = sourceJunctionId;
        }

        @Override
        public void initialize(MachineTopology cr, FontMetricsProvider fontProvider) {
            Wire wireOut = new Wire(2, junction.getDataOuts().get(0), pin);
            add(junction, junctionId);
            addWire(wireOut, junctionId + Parts._WIRE_DATA_OUT);

            OutgoingPin sourcePin = new OutgoingPin(sourceJunction);
            sourceJunction.getDataOuts().add(sourcePin);

            Wire wireIn = new Wire(2, sourcePin, junction.getDataIn());
            addWire(wireIn, junctionId + Parts._WIRE_DATA_IN);
        }

        @Override
        public boolean hasLayouts() {
            return true;
        }

        @Override
        public LayoutSet createLayouts() {
            String junction = pinId + Parts._JUNCTION;
            String wireOut = pinId + Parts._JUNCTION + Parts._WIRE_DATA_OUT;
            String wireIn = pinId + Parts._JUNCTION + Parts._WIRE_DATA_IN;

            ConstraintBuilder cb = new ConstraintBuilder();
            DefaultLayoutSet set = new DefaultLayoutSet();
            set.addLayout(junction, cb.alignVertically(pinId).alignHorizontally(sourceJunctionId));
            set.addLayout(wireOut + ".0", cb.align(junction));
            set.addLayout(wireOut + ".1", cb.align(pinId));
            set.addLayout(wireIn + ".0", cb.align(sourceJunctionId));
            set.addLayout(wireIn + ".1", cb.align(junction));

            return set;
        }
    }
}