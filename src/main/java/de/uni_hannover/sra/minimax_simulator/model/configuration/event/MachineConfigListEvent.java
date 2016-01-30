package de.uni_hannover.sra.minimax_simulator.model.configuration.event;

import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;

/**
 * A {@code MachineConfigListEvent} is a {@link MachineConfigEvent} of which the
 * {@link MachineConfigListener}s will be notified.
 *
 * @param <T>
 *          the class of the changed machine component
 *
 * @author Martin L&uuml;ck
 */
public abstract class MachineConfigListEvent<T> implements MachineConfigEvent {

    /**
     * The first machine component affected by the event.<br>
     * <br>
     * <table summary="MachineConfigListEvent.index2" style="text-align: center; border-spacing: 10px">
     *  <tr>
     *      <th>event type</th>
     *      <th>meaning</th>
     *  </tr>
     *  <tr>
     *      <td>{@link de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.EventType#ELEMENT_ADDED}</td>
     *      <td>the the new machine component</td>
     *  </tr>
     *  <tr>
     *      <td>{@link de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.EventType#ELEMENT_REMOVED}</td>
     *      <td>the machine component that was removed</td>
     *  </tr>
     *  <tr>
     *      <td>{@link de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.EventType#ELEMENT_REPLACED}</td>
     *      <td>the replaced machine component</td>
     *  </tr>
     *  <tr>
     *      <td>{@link de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.EventType#ELEMENTS_EXCHANGED}</td>
     *      <td>the first machine component to swap</td>
     *  </tr>
     * </table>
     */
    public T element;

    /**
     * The second machine component affected by the event.<br>
     * <br>
     * <table summary="MachineConfigListEvent.index2" style="text-align: center; border-spacing: 10px">
     *  <tr>
     *      <th>event type</th>
     *      <th>meaning</th>
     *  </tr>
     *  <tr>
     *      <td>{@link de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.EventType#ELEMENT_REPLACED}</td>
     *      <td>the new machine component</td>
     *  </tr>
     *  <tr>
     *      <td>{@link de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.EventType#ELEMENTS_EXCHANGED}</td>
     *      <td>the second machine component to swap</td>
     *  </tr>
     * </table>
     */
    public T element2;

    /**
     * The index of the first machine component affected by the event.<br>
     * <br>
     * <table summary="MachineConfigListEvent.index2" style="text-align: center; border-spacing: 10px">
     *  <tr>
     *      <th>event type</th>
     *      <th>meaning</th>
     *  </tr>
     *  <tr>
     *      <td>{@link de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.EventType#ELEMENT_ADDED}</td>
     *      <td>the index of the new machine component</td>
     *  </tr>
     *  <tr>
     *      <td>{@link de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.EventType#ELEMENT_REMOVED}</td>
     *      <td>the index of the machine component that was removed</td>
     *  </tr>
     *  <tr>
     *      <td>{@link de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.EventType#ELEMENT_REPLACED}</td>
     *      <td>the index of the replaced machine component</td>
     *  </tr>
     *  <tr>
     *      <td>{@link de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.EventType#ELEMENTS_EXCHANGED}</td>
     *      <td>the first index to swap</td>
     *  </tr>
     * </table>
     */
    public int index;

    /**
     * The index of the second machine component affected by the event.<br>
     * <br>
     * <table summary="MachineConfigListEvent.index2" style="text-align: center; border-spacing: 10px">
     *  <tr>
     *      <th>event type</th>
     *      <th>meaning</th>
     *  </tr>
     *  <tr>
     *      <td>{@link de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent.EventType#ELEMENTS_EXCHANGED}</td>
     *      <td>the second index to swap</td>
     *  </tr>
     * </table>
     */
    public int index2;

    /** The type of the event. */
    public EventType type;

    // private because the events are not instantiable from outside
    private MachineConfigListEvent() {

    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "; " + type + "; " + element
            + (element2 != null ? "/" + element2 : "") + ", " + index
            + (index2 != -1 ? "/" + index2 : "");
    }

    /**
     * Enumeration for the type of the {@code MachineConfigListEvent}.
     */
    public static enum EventType {
        ELEMENT_ADDED,
        ELEMENT_REMOVED,
        ELEMENT_REPLACED,
        ELEMENTS_EXCHANGED
    }

    /**
     * Populates an {@code EventType.ELEMENT_ADDED} {@code MachineConfigListEvent} with all necessary information.
     *
     * @param o
     *          the added machine component
     * @param index
     *          the index of the added machine component
     * @param e
     *          the event of the change
     * @param <T>
     *          the class of the added machine component
     * @param <U>
     *          the {@code MachineConfigListEvent} subclass related to {@code <T>}
     * @return
     *          the event containing all necessary information
     */
    public static <T, U extends MachineConfigListEvent<T>> U eventAdded(T o, int index, U e) {
        e.element = o;
        e.index = index;
        e.type = EventType.ELEMENT_ADDED;
        return e;
    }

    /**
     * Populates an {@code EventType.ELEMENT_REMOVED} {@code MachineConfigListEvent} with all necessary information.
     *
     * @param o
     *          the removed machine component
     * @param index
     *          the index of the removed machine component
     * @param e
     *          the event of the change
     * @param <T>
     *          the class of the removed machine component
     * @param <U>
     *          the {@code MachineConfigListEvent} subclass related to {@code <T>}
     * @return
     *          the event containing all necessary information
     */
    public static <T, U extends MachineConfigListEvent<T>> U eventRemoved(T o, int index, U e) {
        e.element = o;
        e.index = index;
        e.type = EventType.ELEMENT_REMOVED;
        return e;
    }

    /**
     * Populates an {@code EventType.ELEMENT_REPLACED} {@code MachineConfigListEvent} with all necessary information.
     *
     * @param oldElem
     *          the replaced machine component
     * @param newElem
     *          the new machine component
     * @param index
     *          the index of the replaced machine component
     * @param e
     *          the event of the change
     * @param <T>
     *          the class of the machine components
     * @param <U>
     *          the {@code MachineConfigListEvent} subclass related to {@code <T>}
     * @return
     *          the event containing all necessary information
     */
    public static <T, U extends MachineConfigListEvent<T>> U eventReplaced(T oldElem, T newElem, int index, U e) {
        e.element = newElem;
        e.element2 = oldElem;
        e.index = index;
        e.type = EventType.ELEMENT_REPLACED;
        return e;
    }

    /**
     * Populates an {@code EventType.ELEMENTS_EXCHANGED} {@code MachineConfigListEvent} with all necessary information.
     *
     * @param elem1
     *          the first machine component
     * @param elem2
     *          the second machine component
     * @param index1
     *          the index of the first machine component
     * @param index2
     *          the index of the second machine component
     * @param e
     *          the event of the change
     * @param <T>
     *          the class of the machine components
     * @param <U>
     *          the {@code MachineConfigListEvent} subclass related to {@code <T>}
     * @return
     *          the event containing all necessary information
     */
    public static <T, U extends MachineConfigListEvent<T>> U eventExchanged(T elem1, T elem2, int index1, int index2, U e) {
        e.element = elem1;
        e.element2 = elem2;
        e.index = index1;
        e.index2 = index2;
        e.type = EventType.ELEMENTS_EXCHANGED;
        return e;
    }

    /**
     * An event indicating a change of the ALU.
     */
    public static class MachineConfigAluEvent extends MachineConfigListEvent<AluOperation> {

        /**
         * Populates the event "ALU operation added" with the necessary information.
         *
         * @param aluOp
         *          the added {@link AluOperation}
         * @param index
         *          the index of the added {@code AluOperation}
         * @return
         *          the event containing all necessary information
         */
        public static MachineConfigAluEvent eventAdded(AluOperation aluOp, int index) {
            MachineConfigAluEvent e = new MachineConfigAluEvent();
            return eventAdded(aluOp, index, e);
        }

        /**
         * Populates the event "ALU operation removed" with the necessary information.
         *
         * @param aluOp
         *          the removed {@link AluOperation}
         * @param index
         *          the index of the removed {@code AluOperation}
         * @return
         *          the event containing all necessary information
         */
        public static MachineConfigAluEvent eventRemoved(AluOperation aluOp, int index) {
            MachineConfigAluEvent e = new MachineConfigAluEvent();
            return eventRemoved(aluOp, index, e);
        }

        /**
         * Populates the event "ALU operations exchanged" with the necessary information.
         *
         * @param alu1
         *          the first {@link AluOperation}
         * @param alu2
         *          the second {@code AluOperation}
         * @param index1
         *          the index of the first {@code AluOperation}
         * @param index2
         *          the index of the second {@code AluOperation}
         * @return
         *          the event containing all necessary information
         */
        public static MachineConfigAluEvent eventExchanged(AluOperation alu1, AluOperation alu2, int index1, int index2) {
            MachineConfigAluEvent e = new MachineConfigAluEvent();
            return eventExchanged(alu1, alu2, index1, index2, e);
        }
    }

    /**
     * An event indicating a change of the registers ({@link RegisterExtension}).
     */
    public static class MachineConfigRegisterEvent extends MachineConfigListEvent<RegisterExtension> {

        /**
         * Populates the event "register added" with the necessary information.
         *
         * @param register
         *          the added {@link RegisterExtension}
         * @param index
         *          the index of the added {@code RegisterExtension}
         * @return
         *          the event containing all necessary information
         */
        public static MachineConfigRegisterEvent eventAdded(RegisterExtension register, int index) {
            MachineConfigRegisterEvent e = new MachineConfigRegisterEvent();
            return eventAdded(register, index, e);
        }

        /**
         * Populates the event "register removed" with the necessary information.
         *
         * @param register
         *          the removed {@link RegisterExtension}
         * @param index
         *          the index of the removed {@code RegisterExtension}
         * @return
         *          the event containing all necessary information
         */
        public static MachineConfigRegisterEvent eventRemoved(RegisterExtension register, int index) {
            MachineConfigRegisterEvent e = new MachineConfigRegisterEvent();
            return eventRemoved(register, index, e);
        }

        /**
         * Populates the event "register replaced" with the necessary information.
         *
         * @param oldRegister
         *          the replaced {@link RegisterExtension}
         * @param newRegister
         *          the new {@code RegisterExtension}
         * @param index
         *          the index of the {@code RegisterExtension} to be replaced
         * @return
         *          the event containing all necessary information
         */
        public static MachineConfigRegisterEvent eventReplaced(RegisterExtension oldRegister, RegisterExtension newRegister, int index) {
            MachineConfigRegisterEvent e = new MachineConfigRegisterEvent();
            return eventReplaced(oldRegister, newRegister, index, e);
        }

        /**
         * Populated the event "registers exchanged" with the necessary information.
         *
         * @param register1
         *          the first {@link RegisterExtension}
         * @param register2
         *          the second {@code RegisterExtension}
         * @param index1
         *          the index of the first {@code RegisterExtension}
         * @param index2
         *          the index of the second {@code RegisterExtension}
         * @return
         *          the event containing all necessary information
         */
        public static MachineConfigRegisterEvent eventExchanged(RegisterExtension register1, RegisterExtension register2, int index1, int index2) {
            MachineConfigRegisterEvent e = new MachineConfigRegisterEvent();
            return eventExchanged(register1, register2, index1, index2, e);
        }
    }

    /**
     * An event indicating a change of the multiplexer.
     */
    public static class MachineConfigMuxEvent extends MachineConfigListEvent<MuxInput> {

        /** The affected multiplexer. */
        public MuxType mux;

        /**
         * Populates the event "mux input added" with the necessary information.
         *
         * @param mux
         *          the affected multiplexer
         * @param source
         *          the added {@link MuxInput}
         * @param index
         *          the index of the added {@code MuxInput}
         * @return
         *          the event containing all necessary information
         */
        public static MachineConfigMuxEvent eventAdded(MuxType mux, MuxInput source, int index) {
            MachineConfigMuxEvent e = new MachineConfigMuxEvent();
            e.mux = mux;
            return eventAdded(source, index, e);
        }

        /**
         * Populates the event "mux input removed" with the necessary information.
         *
         * @param mux
         *          the affected multiplexer
         * @param source
         *          the removed {@link MuxInput}
         * @param index
         *          the index of the removed {@code MuxInput}
         * @return
         *          the event containing all necessary information
         */
        public static MachineConfigMuxEvent eventRemoved(MuxType mux, MuxInput source, int index) {
            MachineConfigMuxEvent e = new MachineConfigMuxEvent();
            e.mux = mux;
            return eventRemoved(source, index, e);
        }

        /**
         * Populates the event "mux input replaced" with the necessary information.
         *
         * @param mux
         *          the affected multiplexer
         * @param oldSource
         *          the replaced {@link MuxInput}
         * @param newSource
         *          the new {@code MuxInput}
         * @param index
         *          the index of the {@code MuxInput} to be replaced
         * @return
         *          the event containing all necessary information
         */
        public static MachineConfigMuxEvent eventReplaced(MuxType mux, MuxInput oldSource, MuxInput newSource, int index) {
            MachineConfigMuxEvent e = new MachineConfigMuxEvent();
            e.mux = mux;
            return eventReplaced(oldSource, newSource, index, e);
        }

        /**
         * Populates the event "mux inputs exchanged" with the necessary information.
         *
         * @param mux
         *          the affected multiplexer
         * @param source1
         *          the first {@link MuxInput}
         * @param source2
         *          the second {@code MuxInput}
         * @param index1
         *          the index of the first {@code MuxInput}
         * @param index2
         *          the index of the second {@code MuxInput}
         * @return
         *          the event containing all necessary information
         */
        public static MachineConfigMuxEvent eventExchanged(MuxType mux, MuxInput source1, MuxInput source2, int index1, int index2) {
            MachineConfigMuxEvent e = new MachineConfigMuxEvent();
            e.mux = mux;
            return eventExchanged(source1, source2, index1, index2, e);
        }

        @Override
        public String toString() {
            return super.toString() + "; mux=" + mux;
        }
    }
}