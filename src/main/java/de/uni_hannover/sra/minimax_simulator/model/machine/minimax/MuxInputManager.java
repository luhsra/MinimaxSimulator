package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import com.google.common.collect.ImmutableList;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.ExtensionList;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.IngoingPin;

import java.util.Collection;

/**
 * A manager for the {@link ExtensionList} of the machine's {@link MuxInput}s.
 */
interface MuxInputManager extends ExtensionList<MuxInput> {

    /**
     * Data structure of a stored {@link MuxInput}.
     */
    public class InputEntry {

        protected MuxInput input;
        protected IngoingPin pin;
        protected String pinId;

        @Override
        public String toString() {
            return pinId + "<-" + input;
        }
    }

    /**
     * Gets the {@link MuxType} the manager belongs to.
     *
     * @return
     *          the {@code MuxType}
     */
    public MuxType getMuxType();

    /**
     * Gets the {@link MuxInput}s the manager manages.
     *
     * @return
     *          a list of the {@code MuxInputs}
     */
    public ImmutableList<InputEntry> getMuxInputs();

    @Override
    public void add(MuxInput input);

    @Override
    public void addAll(Collection<? extends MuxInput> inputs);

    @Override
    public void remove(int index);

    @Override
    public void swap(int index1, int index2);

    @Override
    public void set(int index, MuxInput input);

    /**
     * Registers the specified {@link MuxInputGroupManager}.
     *
     * @param inputGroupManager
     *          the {@code MuxInputGroupManager} to register
     */
    public void registerGroupManager(MuxInputGroupManager inputGroupManager);
}